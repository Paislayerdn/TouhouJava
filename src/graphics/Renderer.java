package graphics;

import java.awt.image.BufferedImage;

import entity.Thing;
import background3d.*;

public interface Renderer {
	void clear();
	
	void scale(float x, float y);

	void beginTitle();
	void endTitle();

	void beginPlayfield();
	void endPlayfield();

	void thing(Thing thing);
	
	void beginBackground(BackgroundCamera camera);
	void endBackground();
	void backgroundObject(BackgroundObject object);

	void image(BufferedImage image, float x, float y);
	void image(BufferedImage image,	float x, float y,	float width, float height);
	void rectangleOutline(float x,	float y, float width, float height);
	void circleOutline(float x,	float y, float radius);
}