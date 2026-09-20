package state;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import resource.ResourceLoader;
import resource.Music;

import entity.Thing;

import action.Action;
import static action.JScratch.*;
import graphics.Renderer;

public class TitleScreen implements GameState {
	private final BufferedImage sakuraImage;
	private final ArrayList<Thing> sakuras;
	private final Object sakuraLock = new Object();

	private final BufferedImage background;
	private final Thing reimu;
	private final Thing marisa;
	private final BufferedImage game1;
	private final BufferedImage game2;
	private Music bgm;
	
	private int sakuraSpawnTimer = 0;

	public TitleScreen() {
		background = ResourceLoader.image("TSC1");

		reimu = new Thing();
		reimu.run(
			Sequence(
				SetCostume("TSC2F"),
				SetBrightness(100),
				SetGhost(100),
				Warp(-400,0),
				Wait(30),
				show()
			)
		);
		marisa = new Thing();
		marisa.run(
			Sequence(
				SetCostume("TSC3F"),
				SetBrightness(100),
				SetGhost(100),
				Warp(-400,0),
				Wait(25),
				show()
			)
		);

		game1 = ResourceLoader.image("TSC4");
		game2 = ResourceLoader.image("TSC5");

		sakuraImage = ResourceLoader.image("CircleBullet");
		sakuras = new ArrayList<>();
		
		bgm = ResourceLoader.music("[TH20] Shrine Maiden Crowned with Glory Slow");
		bgm.setVolume(-5.0f);
		bgm.play();

	}
	
	private List<Thing> getSakuras() {
		synchronized (sakuraLock) {
			return List.copyOf(sakuras);
		}
	}

	@Override
	public void update() {
		reimu.update();
		marisa.update();

		synchronized (sakuraLock) {
			for (Thing sakura : sakuras) {
				sakura.update();
			}

			sakuras.removeIf(sakura -> !sakura.isAlive());
		}

		if (sakuraSpawnTimer <= 0) {
			int temp = 1 + (int)(Math.random() * 9);

			for (int i = 0; i < temp; i++) {
				spawnSakura();
			}

			sakuraSpawnTimer = 30 + (int)(Math.random() * 60);
		} else {
			sakuraSpawnTimer--;
		}
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		renderer.scale(0.5f, 0.5f);
		
		renderer.image(background, 0, 0);
		reimu.draw(renderer);
		marisa.draw(renderer);
		renderer.image(game1, 0, 0);
		renderer.image(game2, 0, 0);
		
		// Sakura layer
		renderer.scale(2.0f, 2.0f);
		for (Thing sakura : getSakuras()) {
			sakura.draw(renderer);
		}
		renderer.endTitle();
		
	}

	private void spawnSakura() {
		Thing sakura = new Thing();

		sakura.run(
			Par(
				Sequence(
					SetGhost(100),
					SetCostume(sakuraImage),
					SetSize( Random(5,12) ),
					SetColor(240),
					ChangeColor( Random(-5,5) ),
					SetBrightness( Random(50, 75) ),
					SetGhost( Random(10, 40) ),
					Var("dx", Div( Random(-5,45), 20 ) ),
					Var("dy", Div( Random(15,55), -20 ) ),
					Warp(999, 999),
					SetX( Random(-520, 440) ),
					SetY( Random(365, 390) ),
					Forever("Sequence",
						Move( Get("dx"), Get("dy") )
					)
				),
				Sequence(
					Wait(600),
					Destroy()
				)
			)
		);

		synchronized (sakuraLock) {
			sakuras.add(sakura);
		}
	}
	
	private Action show() {
		int iteration1 = 120;
		return Par(
			For("i", 1, iteration1,
				() -> Sequence(
					MoveX(400.0/iteration1),
					ChangeGhost(-100.0/iteration1),
					ChangeBrightness(-100.0/iteration1),
					Wait(1)
				)
			)
		);
	}
}