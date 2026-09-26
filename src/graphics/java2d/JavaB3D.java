package graphics.java2d;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.geom.Path2D;

import background3d.Background3D;
import background3d.BackgroundCamera;
import background3d.BackgroundObject;
import entity.Appearance;

public final class JavaB3D {
	private static final int MESH_SIZE = 16;
	private static final float FOCAL_LENGTH = 500.0f;

	private final Graphics2D g2;

	public JavaB3D(Graphics2D g2) {
		this.g2 = g2;
	}

	public void render(Background3D background) {
		BackgroundCamera camera = background.getCamera();

		for (BackgroundObject object : background.getObjects()) {
			if (!object.visible) {
				continue;
			}

			Appearance appearance = object.appearance;

			if (appearance.costume == null
				|| appearance.size == 0
				|| appearance.ghost == 100
			) {
				continue;
			}

			float halfWidth = object.width / 2.0f;
			float halfHeight = object.height / 2.0f;

			Point3[] corners = {
				new Point3(-halfWidth,  halfHeight, 0),
				new Point3( halfWidth,  halfHeight, 0),
				new Point3( halfWidth, -halfHeight, 0),
				new Point3(-halfWidth, -halfHeight, 0)
			};

			for (Point3 corner : corners) {
				rotate(
					corner,
					object.pitch,
					object.yaw,
					object.roll
				);

				corner.x += object.x - camera.x;
				corner.y += object.y - camera.y;
				corner.z += object.z - camera.z;
			}
			
			BufferedImage image = appearance.getRenderedCostume();
			
			drawTexturedPlane(
				image,
				object,
				camera,
				corners
			);
		}
	}
	private void drawTexturedPlane(
		BufferedImage image,
		BackgroundObject object,
		BackgroundCamera camera,
		Point3[] corners
	) {
		int width = image.getWidth();
		int height = image.getHeight();

		for (int row = 0; row < MESH_SIZE; row++) {
			float v0 = (float) row / MESH_SIZE;
			float v1 = (float) (row + 1) / MESH_SIZE;

			for (int col = 0; col < MESH_SIZE; col++) {
				float u0 = (float) col / MESH_SIZE;
				float u1 = (float) (col + 1) / MESH_SIZE;

				Point3 p00 = interpolatePlane(
					object, camera, corners, u0, v0
				);
				Point3 p10 = interpolatePlane(
					object, camera, corners, u1, v0
				);
				Point3 p11 = interpolatePlane(
					object, camera, corners, u1, v1
				);
				Point3 p01 = interpolatePlane(
					object, camera, corners, u0, v1
				);

				if (p00.z <= 0 || p10.z <= 0
					|| p11.z <= 0 || p01.z <= 0) {
					continue;
				}

				Point2 d00 = project(p00, camera.zoom);
				Point2 d10 = project(p10, camera.zoom);
				Point2 d11 = project(p11, camera.zoom);
				Point2 d01 = project(p01, camera.zoom);

				float sx0 = u0 * width;
				float sx1 = u1 * width;
				float sy0 = v0 * height;
				float sy1 = v1 * height;

				drawTriangle(
					image,
					sx0, sy0,
					sx1, sy0,
					sx1, sy1,
					d00, d10, d11
				);

				drawTriangle(
					image,
					sx0, sy0,
					sx1, sy1,
					sx0, sy1,
					d00, d11, d01
				);
			}
		}
	}
	
	private static Point3 interpolatePlane(
		BackgroundObject object,
		BackgroundCamera camera,
		Point3[] corners,
		float u,
		float v
	) {
		Point3 top = interpolate(
			corners[0],
			corners[1],
			u
		);

		Point3 bottom = interpolate(
			corners[3],
			corners[2],
			u
		);

		Point3 point = interpolate(
			top,
			bottom,
			v
		);

		point.x += object.x - camera.x;
		point.y += object.y - camera.y;
		point.z += object.z - camera.z;
		rotateCamera(
			point,
			camera.pitch,
			camera.yaw,
			camera.roll
		);

		return point;
	}
	private static Point3 interpolate(
			Point3 a,
			Point3 b,
			float t
		) {
			return new Point3(
				a.x + (b.x - a.x) * t,
				a.y + (b.y - a.y) * t,
				a.z + (b.z - a.z) * t
			);
		}
	private void drawTriangle(
		BufferedImage image,
		float sx0, float sy0,
		float sx1, float sy1,
		float sx2, float sy2,
		Point2 d0, Point2 d1, Point2 d2
	) {
		AffineTransform oldTransform = g2.getTransform();
		java.awt.Shape oldClip = g2.getClip();

		java.awt.geom.Path2D.Float clip =
			new java.awt.geom.Path2D.Float();

		clip.moveTo(d0.x, -d0.y);
		clip.lineTo(d1.x, -d1.y);
		clip.lineTo(d2.x, -d2.y);
		clip.closePath();

		g2.clip(clip);

		AffineTransform transform = createAffineTransform(
			sx0, image.getHeight() - sy0,
			sx1, image.getHeight() - sy1,
			sx2, image.getHeight() - sy2,
			d0.x, -d0.y,
			d1.x, -d1.y,
			d2.x, -d2.y
		);

		g2.drawImage(image, transform, null);

		g2.setClip(oldClip);
		g2.setTransform(oldTransform);
	}
	private static AffineTransform createAffineTransform(
		float sx0, float sy0,
		float sx1, float sy1,
		float sx2, float sy2,
		float dx0, float dy0,
		float dx1, float dy1,
		float dx2, float dy2
	) {
		float x1 = sx1 - sx0;
		float y1 = sy1 - sy0;
		float x2 = sx2 - sx0;
		float y2 = sy2 - sy0;

		float determinant =
			x1 * y2 - x2 * y1;

		float m00 = (
			(dx1 - dx0) * y2
			- (dx2 - dx0) * y1
		) / determinant;

		float m01 = (
			(dx2 - dx0) * x1
			- (dx1 - dx0) * x2
		) / determinant;

		float m10 = (
			(dy1 - dy0) * y2
			- (dy2 - dy0) * y1
		) / determinant;

		float m11 = (
			(dy2 - dy0) * x1
			- (dy1 - dy0) * x2
		) / determinant;

		float tx = dx0 - m00 * sx0 - m01 * sy0;
		float ty = dy0 - m10 * sx0 - m11 * sy0;

		return new AffineTransform(
			m00, m10,
			m01, m11,
			tx, ty
		);
	}
	
	private static void rotate(
		Point3 p,
		float pitch,
		float yaw,
		float roll
	) {
		rotateX(p, pitch);
		rotateY(p, yaw);
		rotateZ(p, roll);
	}
	private static void rotateCamera(
		Point3 p,
		float pitch,
		float yaw,
		float roll
	) {
		rotateZ(p, -roll);
		rotateY(p, -yaw);
		rotateX(p, -pitch);
	}
	private static void rotateX(Point3 p, float angle) {
		float rad = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float y = p.y * cos - p.z * sin;
		float z = p.y * sin + p.z * cos;

		p.y = y;
		p.z = z;
	}
	private static void rotateY(Point3 p, float angle) {
		float rad = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float x = p.x * cos + p.z * sin;
		float z = -p.x * sin + p.z * cos;

		p.x = x;
		p.z = z;
	}
	private static void rotateZ(Point3 p, float angle) {
		float rad = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float x = p.x * cos - p.y * sin;
		float y = p.x * sin + p.y * cos;

		p.x = x;
		p.y = y;
	}

	private static Point2 project(Point3 p, float zoom) {
		if (p.z <= 0.0f) {
			return null;
		}

		float perspective = FOCAL_LENGTH / p.z;

		return new Point2(
			p.x * perspective * zoom,
			p.y * perspective * zoom
		);
	}
	
	private static final class Point3 {
		float x;
		float y;
		float z;

		Point3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	private static final class Point2 {
		float x;
		float y;

		Point2(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}