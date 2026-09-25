package graphics;

import java.awt.image.BufferedImage;
import entity.Thing;

public interface Renderer {
	void clear();
	
	void scale(float x, float y);

	void beginPlayfield();
	void endPlayfield();

	void beginTitle();
	void endTitle();

	void thing(Thing thing);

	void image(BufferedImage image, float x, float y);
	void image(BufferedImage image,	float x, float y,	float width, float height);
	void rectangleOutline(float x,	float y, float width, float height);
	void circleOutline(float x,	float y, float radius);
}