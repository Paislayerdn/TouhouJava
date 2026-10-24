package background3d;

import action.Action;
import action.ActionContext;
import action.ActionTarget;
import entity.Position3D;
import entity.Orientation3D;
import graphics.Renderer;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import resource.ResourceLoader;
import action.JSL;

public final class Background3D extends ActionTarget {
	private static final float OBJECT_SIZE = 100.0f;
	private static final int MAX_RENDERED_BORDER_COPIES = 343;
	private static final AtomicLong NEXT_SPAWN_ID = new AtomicLong();

	private final BackgroundCamera camera = new BackgroundCamera();
	private final List<BackgroundObject> objects = new ArrayList<>();
	private final List<BackgroundObject> worldObjects = new ArrayList<>();
	private final List<BackgroundObject> skyObjects = new ArrayList<>();
	private final Map<String, BackgroundObject> objectMap = new HashMap<>();
	private LuaTable luaScene;
	private LuaTable luaEvents;
	private BufferedImage fallbackImage;
	private float borderX, borderY, borderZ;

	public Background3D() {
		setName("BackgroundScene");
	}

	public void load(String name) {
		objects.clear();
		worldObjects.clear();
		skyObjects.clear();
		objectMap.clear();
		setActionContext(new ActionContext());
		camera.setActionContext(new ActionContext(getActionContext()));
		resetCamera();
		borderX = borderY = borderZ = 0;
		fallbackImage = null;

		Globals globals = JSL.registerJScratch();
		LuaValue result = globals.load(ResourceLoader.backgroundLua(name), name + ".lua").call();
		if (!result.istable()) {
			throw new IllegalArgumentException("Background Lua file '" + name
				+ "' must return a scene table");
		}
		luaScene = result.checktable();
		loadSceneData(luaScene);
		luaEvents = tableOrEmpty(luaScene.get("events"));
	}

	private void loadSceneData(LuaTable scene) {
		LuaValue fallback = scene.get("fallback");
		if (!fallback.isnil()) fallbackImage = ResourceLoader.backgroundImage(fallback.checkjstring());

		LuaTable border = tableOrEmpty(scene.get("border"));
		borderX = number(border.get("x"), 0);
		borderY = number(border.get("y"), 0);
		borderZ = number(border.get("z"), 0);

		loadCamera(tableOrEmpty(scene.get("camera")));
		getActionContext().putLocal("camera", camera);
		camera.setActionContext(new ActionContext(getActionContext()));

		LuaTable world = tableOrEmpty(scene.get("world"));
		loadObjects(world, false);
		LuaTable sky = tableOrEmpty(scene.get("sky"));
		loadObjects(sky, true);

		for (BackgroundObject object : worldObjects) wrapWorldObject(object);
		scene.set("camera", CoerceJavaToLua.coerce(camera));
	}

	private void loadCamera(LuaTable source) {
		applyPosition(camera.getPosition(), tableOrEmpty(source.get("position")));
		applyOrientation(camera.getOrientation(), tableOrEmpty(source.get("orientation")));
		LuaValue fogColor = source.get("fogColor");
		if (fogColor.istable()) {
			LuaTable color = fogColor.checktable();
			camera.setFogColor(number(color.get(1), 0), number(color.get(2), 0),
				number(color.get(3), 0));
		}
		camera.setFogDistance(number(source.get("fogDistance"), 0));
	}

	private void loadObjects(LuaTable source, boolean sky) {
		LuaValue key = LuaValue.NIL;
		while (true) {
			Varargs entry = source.next(key);
			key = entry.arg1();
			if (key.isnil()) break;
			if (!key.isstring() || !entry.arg(2).istable()) {
				throw new IllegalArgumentException("Scene object entries must map string names to tables");
			}

			String objectName = key.checkjstring();
			LuaTable descriptor = entry.arg(2).checktable();
			String type = descriptor.get("type").optjstring("object");
			boolean tiled = type.equalsIgnoreCase("plane");
			if (!tiled && !type.equalsIgnoreCase("object")
					&& !type.equalsIgnoreCase("backgroundobject")) {
				throw new IllegalArgumentException("Unknown background object type '" + type
					+ "' for " + objectName);
			}

			BackgroundObject object = new BackgroundObject(objectName, 0, 0, 0,
				OBJECT_SIZE, OBJECT_SIZE, tiled);
			applyPosition(object.getPosition(), tableOrEmpty(descriptor.get("position")));
			applyOrientation(object.getOrientation(), tableOrEmpty(descriptor.get("orientation")));
			LuaValue costume = descriptor.get("costume");
			if (!costume.isnil()) object.getAppearance().setCostume(
				ResourceLoader.backgroundImage(costume.checkjstring()));

			addObject(object, sky);
			object.setActionContext(new ActionContext(getActionContext()));
			getActionContext().putLocal(objectName, object);
			source.set(key, CoerceJavaToLua.coerce(object));
		}
	}

	private static void applyPosition(Position3D position, LuaTable source) {
		position.set(number(source.get("x"), position.getX()),
			number(source.get("y"), position.getY()),
			number(source.get("z"), position.getZ()));
	}

	private static void applyOrientation(Orientation3D orientation, LuaTable source) {
		orientation.set(number(source.get("pitch"), orientation.getPitch()),
			number(source.get("yaw"), orientation.getYaw()),
			number(source.get("roll"), orientation.getRoll()));
	}

	private static LuaTable tableOrEmpty(LuaValue value) {
		return value.istable() ? value.checktable() : new LuaTable();
	}

	private static float number(LuaValue value, float fallback) {
		return value.isnumber() ? value.tofloat() : fallback;
	}

	private void resetCamera() {
		camera.setXYZ(0, 0, 0);
		camera.setRotation(0, 0, 0);
		camera.zoom = 1.0f;
		camera.setFogColor(0, 0, 0);
		camera.setFogDistance(0);
	}

	public void event(String name) {
		if (luaEvents == null) throw new IllegalStateException("No background scene is loaded");
		LuaValue event = luaEvents.get(name);
		if (event.isnil()) throw new IllegalArgumentException(
			"Background event '" + name + "' does not exist");
		if (!event.isfunction()) throw new IllegalArgumentException(
			"Background event '" + name + "' must be a function");

		LuaValue result = event.call(CoerceJavaToLua.coerce(luaScene));
		if (result.isnil()) return;
		Action action = (Action) result.checkuserdata(Action.class);
		getActionRunner().add(action, this);
	}

	@Override
	public ActionTarget spawn(String className, String instanceName) {
		if (className.equalsIgnoreCase("object")
				|| className.equalsIgnoreCase("backgroundobject")
				|| className.equalsIgnoreCase("plane")) {
			boolean tiled = className.equalsIgnoreCase("plane");
			String uniqueName = className + "#" + NEXT_SPAWN_ID.incrementAndGet();
			BackgroundObject object = new BackgroundObject(uniqueName, 0, 0, 0,
				OBJECT_SIZE, OBJECT_SIZE, tiled);
			addObject(object, false);
			return object;
		}
		return super.spawn(className, instanceName);
	}

	private void addObject(BackgroundObject object, boolean sky) {
		String key = object.getName();
		if (objectMap.putIfAbsent(key, object) != null) {
			throw new IllegalArgumentException("Duplicate background object: " + key);
		}
		objects.add(object);
		(sky ? skyObjects : worldObjects).add(object);
		if (!sky) wrapWorldObject(object);
	}

	public void update() {
		camera.updateActions();
		for (BackgroundObject object : List.copyOf(objects)) {
			object.updateActions();
			if (worldObjects.contains(object)) wrapWorldObject(object);
		}
		updateActions();
	}

	public void render(Renderer renderer) {
		if (renderer.usesBackgroundFallback()) {
			if (fallbackImage != null) renderer.backgroundFallback(fallbackImage);
			return;
		}
		renderer.beginBackground(camera);
		for (BackgroundObject object : worldObjects) {
			if (object.tiledPlane) drawIfVisible(renderer, object);
			else forEachWorldCopy(object, copy -> drawIfVisible(renderer, copy));
		}
		for (BackgroundObject object : skyObjects) {
			float[] offset = cameraRelativeOffset(object.getX(), object.getY(), object.getZ());
			BackgroundObject cameraLocked = object.copyAt(
				camera.getX() + offset[0], camera.getY() + offset[1], camera.getZ() + offset[2]);
			cameraLocked.setRotation(camera.getPitch() + object.getPitch(),
				camera.getYaw() + object.getYaw(), camera.getRoll() + object.getRoll());
			drawIfVisible(renderer, cameraLocked);
		}
		renderer.endBackground();
	}

	private float[] cameraRelativeOffset(float x, float y, float z) {
		double pitch = Math.toRadians(camera.getPitch());
		double yaw = Math.toRadians(camera.getYaw());
		double roll = Math.toRadians(camera.getRoll());
		float y1 = (float) (y * Math.cos(pitch) - z * Math.sin(pitch));
		float z1 = (float) (y * Math.sin(pitch) + z * Math.cos(pitch));
		float x2 = (float) (x * Math.cos(yaw) + z1 * Math.sin(yaw));
		float z2 = (float) (-x * Math.sin(yaw) + z1 * Math.cos(yaw));
		return new float[] {
			(float) (x2 * Math.cos(roll) - y1 * Math.sin(roll)),
			(float) (x2 * Math.sin(roll) + y1 * Math.cos(roll)), z2
		};
	}

	private void forEachWorldCopy(BackgroundObject object,
		java.util.function.Consumer<BackgroundObject> consumer) {
		int centerX = tileIndex(camera.getX(), borderX);
		int centerY = tileIndex(camera.getY(), borderY);
		int centerZ = tileIndex(camera.getZ(), borderZ);
		int minX = borderX > 0 ? centerX - 1 : 0;
		int maxX = borderX > 0 ? centerX + 1 : 0;
		int minY = borderY > 0 ? centerY - 1 : 0;
		int maxY = borderY > 0 ? centerY + 1 : 0;
		int minZ = borderZ > 0 ? centerZ - 1 : 0;
		int maxZ = borderZ > 0 ? centerZ + 1 : 0;
		int copies = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
		if (copies > MAX_RENDERED_BORDER_COPIES) throw new IllegalStateException(
			"Border settings request too many background tile copies");

		for (int ix = minX; ix <= maxX; ix++) {
			for (int iy = minY; iy <= maxY; iy++) {
				for (int iz = minZ; iz <= maxZ; iz++) {
					consumer.accept(object.copyAt(
						object.getX() + ix * borderX,
						object.getY() + iy * borderY,
						object.getZ() + iz * borderZ));
				}
			}
		}
	}

	private static int tileIndex(float coordinate, float period) {
		if (period <= 0) return 0;
		return (int) Math.floor((coordinate + period / 2.0f) / period);
	}

	private static void drawIfVisible(Renderer renderer, BackgroundObject object) {
		if (object.visible) renderer.backgroundObject(object);
	}

	private void wrapWorldObject(BackgroundObject object) {
		if (borderX > 0) object.setX(wrap(object.getX(), borderX));
		if (borderY > 0) object.setY(wrap(object.getY(), borderY));
		if (borderZ > 0) object.setZ(wrap(object.getZ(), borderZ));
	}

	private static float wrap(float value, float period) {
		return (float) (value - Math.floor((value + period / 2.0f) / period) * period);
	}

	public BackgroundCamera getCamera() { return camera; }
	public List<BackgroundObject> getObjects() { return objects; }
	public void add(BackgroundObject object) { addObject(object, false); }
	public void remove(BackgroundObject object) {
		objects.remove(object);
		worldObjects.remove(object);
		skyObjects.remove(object);
		objectMap.remove(object.getName(), object);
	}
}
