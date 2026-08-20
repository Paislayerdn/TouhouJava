package resource;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	private final AudioData data;

	private Clip clip;
	private float volume = 0.0f;

	public Music(AudioData data) {
		this.data = data;
	}

	public void play() {
		if (clip == null) {
			clip = createClip();

			if (clip == null) {
				return;
			}
		}

		clip.stop();
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		if (clip == null) {
			return;
		}

		clip.stop();
		clip.setFramePosition(0);
	}

	private Clip createClip() {
		try {
			ByteArrayInputStream input =
				new ByteArrayInputStream(data.data);

			AudioInputStream audio =
				new AudioInputStream( input, data.format, data.data.length / data.format.getFrameSize() );

			Clip clip = AudioSystem.getClip();
			clip.open(audio);

			setClipVolume(clip, volume);

			audio.close();

			return clip;

		} catch (IOException | LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setClipVolume(Clip clip, float volume) {
		FloatControl control = (FloatControl) clip.getControl( FloatControl.Type.MASTER_GAIN );
		control.setValue(volume);
	}

	public void setVolume(float volume) {
		this.volume = volume;

		if (clip != null) {
			setClipVolume(clip, volume);
		}
	}
}