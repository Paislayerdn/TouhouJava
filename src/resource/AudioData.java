package resource;

import javax.sound.sampled.AudioFormat;

public final class AudioData {
	final AudioFormat format;
	final byte[] data;

	AudioData(AudioFormat format, byte[] data) {
		this.format = format;
		this.data = data;
	}
}