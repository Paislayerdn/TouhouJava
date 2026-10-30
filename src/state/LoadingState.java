package state;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import action.Action;
import static action.JScratch.*;
import entity.Thing;
import graphics.Renderer;
import main.Game;
import resource.ResourceLoader;
import main.Settings;
import state.loading.LoadingAssets;

public final class LoadingState extends GameState {
	public enum Route {
		TITLE,
		PLAYING
	}

	private final Route route;
	private static final int MINIMUM_FRAMES = 30;
	private static final int GRID_COLUMNS = 17;
	private static final int GRID_ROWS = 13;
	private static final float GRID_TILE_SIZE = 60;
	private static final float GRID_HALF_TILE_SIZE = GRID_TILE_SIZE / 2;
	private static final float GRID_SPACING_X = GRID_TILE_SIZE;
	private static final float GRID_SPACING_Y = GRID_TILE_SIZE;
	private static final float GRID_PERIOD_X = GRID_COLUMNS * GRID_SPACING_X;
	private static final float GRID_PERIOD_Y = GRID_ROWS * GRID_SPACING_Y;
	
	private final GameState coveredState;
	private final Deque<GameState> destinations = new ArrayDeque<>();
	private final ArrayList<Runnable> loadTasks = new ArrayList<>();
	private final ArrayList<Thing> things = new ArrayList<>();
	private final BufferedImage gridImage;
	private final Thing foreground;

	private Supplier<Action> releaseAction = LoadingState::defaultRelease;
	private int elapsedFrames;
	private int nextTask;
	private boolean releasing;

	public LoadingState(Game game, Route route) {
		this(game, route, null);
	}

	public LoadingState(Game game, Route route, GameState coveredState) {
		super(game);
		this.route = route;
		this.coveredState = coveredState;

		this.gridImage = ResourceLoader.image("Icon");
		createGrid();

		this.foreground = new Thing();
		things.add(foreground);

		setupRoute();
	}
	
	private void setupRoute() {
		switch (route) {
			case TITLE -> {
				LoadingAssets.preload(this, LoadingAssets.Route.TITLE);
				setForegroundImage("Test2Tree");
			}
			case PLAYING -> {
				LoadingAssets.preload(this, LoadingAssets.Route.PLAYING);
			}
		}
	}

	public void setForegroundImage(String name) {
		foreground.run(Sequence(
			SetCostume(ResourceLoader.backgroundImage(name)),
			SetSize(100),
			SetGhost(0),
			SetX(0),
			SetY(0)
		));
	}

	public void preloadImage(String name) {
		loadTasks.add(() -> ResourceLoader.image(name));
	}

	public void preloadSound(String name) {
		loadTasks.add(() -> ResourceLoader.sound(name));
	}

	public void preloadDialogue(String name) {
		loadTasks.add(() -> ResourceLoader.dialogue(name));
	}

	public void preloadBackgroundLua(String name) {
		loadTasks.add(() -> ResourceLoader.backgroundLua(name));
	}

	public void preloadLua(String name) {
		loadTasks.add(() -> ResourceLoader.lua(name));
	}

	public void setReleaseAction(Supplier<Action> releaseAction) {
		this.releaseAction = releaseAction;
	}

	public int getLoadedTaskCount() { return nextTask; }
	public int getTaskCount() { return loadTasks.size(); }

	@Override
	public void update() {
		elapsedFrames++;

		for (Thing thing : snapshotThings()) {
			thing.update();
		}
		things.removeIf(thing -> !thing.isAlive());

		if (!releasing) {
			if (nextTask < loadTasks.size()) {
				loadTasks.get(nextTask++).run();
				return;
			}

			if (elapsedFrames >= MINIMUM_FRAMES) {
				destinations.addLast(createDestination());
				releaseThings();
			}
			return;
		}

		if (things.isEmpty()) {
			if (!destinations.isEmpty()) {
				parent.pushBehindFront(destinations.removeFirst());
			}

			if (coveredState != null) {
				parent.removeState(coveredState);
			}

			finish();
		}
	}
	
	private GameState createDestination() {
		return switch (route) {
			case TITLE -> new TitleScreen(parent);
			case PLAYING -> new Playing(parent);
		};
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		for (Thing thing : snapshotThings()) {
			thing.draw(renderer);
		}
		renderer.endTitle();
	}

	private void createGrid() {
		for (int row = 0; row < GRID_ROWS; row++) {
			for (int column = 0; column < GRID_COLUMNS; column++) {
				Thing tile = new Thing();
				tile.run(Sequence(
					SetCostume(gridImage),
					SetSize(GRID_TILE_SIZE * 100 / gridImage.getWidth()),
					SetGhost(0),
					SetX(-Settings.HALF_WIDTH - GRID_HALF_TILE_SIZE),
					SetY(Settings.HALF_HEIGHT - GRID_HALF_TILE_SIZE),
					MoveX(column * GRID_SPACING_X),
					MoveY(-row * GRID_SPACING_Y),
					Var("phase", (row + column) * 4.0),
					Forever("Sequence",
						Move(1, 1),
						SetX(Sub(
							Mod(
								Add(Get("x"), Settings.HALF_WIDTH + GRID_HALF_TILE_SIZE),
								GRID_PERIOD_X
							),
							Settings.HALF_WIDTH + GRID_HALF_TILE_SIZE
						)),
						SetY(Sub(
							Mod(
								Add(Get("y"), Settings.HALF_HEIGHT + GRID_HALF_TILE_SIZE),
								GRID_PERIOD_Y
							),
							Settings.HALF_HEIGHT + GRID_HALF_TILE_SIZE
						)),
						ChangeColor(1),
						Wait(1)
					)
				));
				things.add(tile);
			}
		}
	}

	private void releaseThings() {
		releasing = true;
		for (Thing thing : things) {
			thing.run(releaseAction.get());
		}
	}

	private static Action defaultRelease() {
		return Sequence(
			Tween("ghost", 100, 30),
			Destroy()
		);
	}

	private List<Thing> snapshotThings() {
		return new ArrayList<>(things);
	}
}
