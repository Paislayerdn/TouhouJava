package state;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Depict;
import resource.ResourceLoader;

import main.Settings;

public class TitleScreen implements GameState {
    private final BufferedImage background;
    private final BufferedImage reimu;
    private final BufferedImage marisa;
    private final BufferedImage game1;
    private final BufferedImage game2;

    public TitleScreen() {
        background = ResourceLoader.image("TSC1");
        reimu = ResourceLoader.image("TSC2F");
        marisa = ResourceLoader.image("TSC3F");
        game1 = ResourceLoader.image("TSC4");
        game2 = ResourceLoader.image("TSC5");
    }

    @Override
    public void update() {
        
    }

    @Override
    public void draw(Graphics2D g2) {
		AffineTransform old = g2.getTransform();

		g2.translate(
			Settings.BASE_WIDTH / 2.0,
			Settings.BASE_HEIGHT / 2.0
		);
		g2.scale(0.5, 0.5);

		Depict.image(g2, background);
		Depict.image(g2, reimu);
		Depict.image(g2, marisa);
		Depict.image(g2, game1);
		Depict.image(g2, game2);

		g2.setTransform(old);
	}
}