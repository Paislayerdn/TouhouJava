#version 120

uniform sampler2D tex;
uniform float brightness;
uniform float alpha;
uniform float color;

vec3 rgbToHsv(vec3 c);
vec3 hsvToRgb(vec3 c);

void main() {
	vec4 pixel = texture2D(tex, gl_TexCoord[0].st);

	// Hue / Color shifting
	if (pixel.a > 0.0) {
		vec3 hsv = rgbToHsv(pixel.rgb);

		if (hsv.y != 0.0) {
			hsv.x = fract(hsv.x + color / 256.0);
			pixel.rgb = hsvToRgb(hsv);
		}
	}

	// Brightness
	float factor = abs(brightness) / 100.0;
	if (brightness < 0.0) {
		pixel.rgb *= 1.0 - factor;
	} else {
		pixel.rgb += (1.0 - pixel.rgb) * factor;
	}
	
	// Ghost / Transparency
	pixel.a *= alpha;

	// Output Effect
	gl_FragColor = pixel;
}

// Converts a color from RGB representation to HSV.
// HSV makes hue rotation easy because hue is represented separately.
vec3 rgbToHsv(vec3 c) {
	float maxValue = max(c.r, max(c.g, c.b));
	float minValue = min(c.r, min(c.g, c.b));
	float delta = maxValue - minValue;

	float h = 0.0;
	float s = 0.0;
	float v = maxValue;

	if (delta != 0.0) {
		s = delta / maxValue;

		if (maxValue == c.r) {
			h = (c.g - c.b) / delta;
		} else if (maxValue == c.g) {
			h = 2.0 + (c.b - c.r) / delta;
		} else {
			h = 4.0 + (c.r - c.g) / delta;
		}

		h /= 6.0;

		if (h < 0.0) {
			h += 1.0;
		}
	}

	return vec3(h, s, v);
}

// Converts HSV back to RGB for rendering.
vec3 hsvToRgb(vec3 c) {
	float h = c.x * 6.0;
	float s = c.y;
	float v = c.z;

	float i = floor(h);
	float f = h - i;

	float p = v * (1.0 - s);
	float q = v * (1.0 - s * f);
	float t = v * (1.0 - s * (1.0 - f));

	if (i == 0.0) return vec3(v, t, p);
	if (i == 1.0) return vec3(q, v, p);
	if (i == 2.0) return vec3(p, v, t);
	if (i == 3.0) return vec3(p, q, v);
	if (i == 4.0) return vec3(t, p, v);

	return vec3(v, p, q);
}