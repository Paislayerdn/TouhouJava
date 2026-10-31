package state;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import action.Action;
import entity.Thing;
import graphics.Renderer;
import main.Game;
import main.Input;
import resource.Music;
import resource.ResourceLoader;
import state.title.OptionsScreen;

import static action.JScratch.*;

public class TitleScreen extends GameState {
	private static final float ART_SCALE = 0.5f;
	private static final int MENU_REVEAL_FRAME = 130;
	private static final int PLAY = 0;
	private static final int OPTION = 1;
	private static final int QUIT = 2;

	private final BufferedImage background;
	private final Thing border;
	private final Thing reimu, marisa;
	private final Thing logo;
	private final Thing selector;
	
	private static final float[] MENU_X = { 450, 500, 450 };
	private static final float[] MENU_Y = { -48, -218, -388 };
	private static final String[] MENU_ASSETS = {
		"TSCB1", "TSCB2", "TSCB3"
	};
	private final Thing[] menuItems = new Thing[MENU_ASSETS.length];
	private final BufferedImage sakuraImage;
	private final ArrayList<Thing> sakuras = new ArrayList<>();
	private final Object sakuraLock = new Object();
	private final Music bgm;

	private int sakuraSpawnTimer;
	private int elapsedFrames;
	private int selectedItem = PLAY;
	private boolean menuReady = false;

	public TitleScreen(Game game) {
		super(game);

		background = ResourceLoader.image("TSC1");
		border = new Thing(0, 0, "TSC4", 100);
		fadeInWithCharacters(border);
		border.run(Sequence(
			Var("t", 0),
			Forever("Sequence",
				SetBrightness(Add(5, Mul(5, Sin( Mul(5 ,Get("t") ) ) ) ) ),
				Set("t", Mod(Add(Get("t"), 1), 360)),
				Wait()
			)
		) );
		sakuraImage = ResourceLoader.image("BulletCircle");

		reimu = new Thing();
		reimu.run(Sequence(
			SetCostume("TSC2F"),
			SetBrightness(100),
			SetGhost(100),
			Warp(-400, 0),
			Wait(30),
			show()
		) );

		marisa = new Thing();
		marisa.getAppearance().setGhost(100);
		marisa.run(Sequence(
			SetCostume("TSC3F"),
			SetDesaturation(30),
			SetBrightness(100),
			SetGhost(100),
			Warp(-400, 0),
			Wait(30),
			show()
		) );

		logo = new Thing(0, 0, "TSC5", 100);
		fadeInWithCharacters(logo);
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i] = new Thing(
				MENU_X[i], MENU_Y[i], MENU_ASSETS[i], 100
			);
			menuItems[i].getAppearance().setDesaturation(100);
			menuItems[i].getAppearance().setGhost(100);
			fadeInWithCharacters(menuItems[i]);
		}

		selector = new Thing(
			0, MENU_Y[PLAY], "DiamondSelect", 100
		);
		selector.getAppearance().setGhost(100);
		selector.run(Sequence(
			Wait(MENU_REVEAL_FRAME),
			Tween("ghost", 0, 16)
		) );

		bgm = ResourceLoader.music("[TH20] Shrine Maiden Crowned with Glory Slow");
		bgm.setVolume(-15.0f);
		bgm.play();
	}

	private void fadeInWithCharacters(Thing thing) {
		thing.getAppearance().setGhost(100.0f);
		thing.run(Sequence(
			Wait(30),
			Tween("ghost", 100, 0, 100)
		) );
	}

	private List<Thing> getSakuras() {
		synchronized (sakuraLock) {
			return List.copyOf(sakuras);
		}
	}

	@Override
	public void update() {
		if (!parent.isFront(this)) return;

		reimu.update();
		marisa.update();
		border.update();
		logo.update();
		selector.update();
		for (Thing menuItem : menuItems) menuItem.update();

		synchronized (sakuraLock) {
			for (Thing sakura : sakuras) sakura.update();
			sakuras.removeIf(sakura -> !sakura.isAlive());
		}

		if (elapsedFrames < MENU_REVEAL_FRAME) {
			elapsedFrames++;
			if (elapsedFrames == MENU_REVEAL_FRAME) {
				menuReady = true;
				setSelectedItem(selectedItem);
			}
		}

		if (menuReady) {
			if (Input.tap.W || Input.tap.UP) moveSelection(-1);
			if (Input.tap.S || Input.tap.DOWN) moveSelection(1);
			if (Input.tap.Z) activateSelection();
		}

		if (sakuraSpawnTimer <= 0) {
			int count = 1 + (int)(Math.random() * 9);
			for (int i = 0; i < count; i++) spawnSakura();
			sakuraSpawnTimer = 30 + (int)(Math.random() * 60);
		} else {
			sakuraSpawnTimer--;
		}
	}

	private void moveSelection(int direction) {
		selectedItem = (selectedItem + direction + menuItems.length) % menuItems.length;
		setSelectedItem(selectedItem);
	}

	private void setSelectedItem(int selected) {
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i].run(Sequence(
				Tween("desaturation", i == selected ? 0 : 100, 6),
				Tween("brightness", i == selected ? 0 : -40, 6)
			) );
		}
		selector.run(GoTo(300, MENU_Y[selected], 6));
	}

	private void activateSelection() {
		switch (selectedItem) {
			case PLAY -> {
				bgm.stop();
				parent.getPlayingStats().reset();
				parent.pushState(new LoadingState(
					parent, LoadingState.Route.PLAYING, this
				));
			}
			case OPTION -> parent.pushState(new OptionsScreen(parent));
			case QUIT -> {
				bgm.stop();
				parent.quit();
			}
			default -> throw new IllegalStateException("Unknown title menu item");
		}
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		renderer.scale(ART_SCALE, ART_SCALE);
		renderer.image(background, 0, 0);
		reimu.draw(renderer);
		marisa.draw(renderer);
		logo.draw(renderer);
		for (Thing menuItem : menuItems) menuItem.draw(renderer);
		selector.draw(renderer);
		border.draw(renderer);

		renderer.scale(1.0f / ART_SCALE, 1.0f / ART_SCALE);
		for (Thing sakura : getSakuras()) sakura.draw(renderer);
		renderer.endTitle();
	}

	private void spawnSakura() {
		Thing sakura = new Thing();
		sakura.getAppearance().setGhost(100);
		sakura.run(Par(
			Sequence(
				SetGhost(100),
				SetCostume(sakuraImage),
				SetSize(Random(5, 12)),
				SetColor(240),
				ChangeColor(Random(-5, 5)),
				SetBrightness(Random(50, 75)),
				Var("targetGhost", Random(10, 40)),
				Var("dx", Div(Random(-5, 45), 10)),
				Var("dy", Div(Random(15, 55), -10)),
				Warp(999, 999),
				SetX(Random(-520, 440)),
				SetY(Random(365, 390)),
				Tween("ghost", Get("targetGhost"), 20),
				Forever("Sequence", Move(Get("dx"), Get("dy")), Wait())
			),
			Sequence(Wait(540), Tween("ghost", 100, 120), Destroy())
		));

		synchronized (sakuraLock) {
			sakuras.add(sakura);
		}
	}

	private Action show() {
		return Tween("x", 0, "ghost", 0, "brightness", 0, 100);
	}
}
