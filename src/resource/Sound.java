package resource;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.sampled.*;

public class Sound {
	private final AudioData data;
	private float volume = 0.0f;
	private final ArrayList<Clip> clips = new ArrayList<>();

	public Sound(AudioData data) {
		this.data = data;
	}
	
	public void play() {
		cleanup();
		Clip clip = findAvailableClip();

		if (clip == null) {
			clip = createClip();
			if (clip == null) { return; }
			clips.add(clip);
		}

		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	private Clip findAvailableClip() {
		for (Clip clip : clips) {
			if (!clip.isRunning()) {
				return clip;
			}
		}
		return null;
	}

	private Clip createClip() {
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(data.data);
			AudioInputStream audio =
					new AudioInputStream(input, data.format, data.data.length / data.format.getFrameSize() );

			Clip clip = AudioSystem.getClip();
			clip.open(audio);

			setClipVolume(clip, volume);

			return clip;

		} catch (IOException | LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void cleanup() {
		Iterator<Clip> iterator = clips.iterator();
		
		while (iterator.hasNext()) {
			Clip clip = iterator.next();

			if (!clip.isRunning()
					&& clip.getFramePosition() == clip.getFrameLength()) {
				clip.close();
				iterator.remove();
			}
		}
	}
	private void setClipVolume(Clip clip, float volume) {
		FloatControl control = (FloatControl) clip.getControl( FloatControl.Type.MASTER_GAIN );
		control.setValue(volume);
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		for (Clip clip : clips) {
			setClipVolume(clip, volume);
		}
	}
}