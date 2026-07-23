package resource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.sampled.*;

public class Sound {
	private static final String SOUND_PATH = "/assets/sounds/";
	private static final String[] EXTENSIONS = {
		".wav",
		".aiff",
		".au"
	};
	private final String name;

	private final ArrayList<Clip> clips = new ArrayList<>();

	private float volume = 0.0f;

	public Sound(String name) {
		this.name = name;
	}
	
	public void play() {
		cleanup();
		Clip clip = findAvailableClip();

		if (clip == null) {
			clip = createClip();
			if (clip == null) {
				return;
			}

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
			URL url = ResourceFinder.find(SOUND_PATH, name, EXTENSIONS);
			if (url == null) {
				System.out.println("[Sound] Cannot find sound: " + name);
				return null;
			}

			try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)) {
				Clip clip = AudioSystem.getClip();
				clip.open(audio);

				FloatControl control =
						(FloatControl) clip.getControl(
								FloatControl.Type.MASTER_GAIN);

				control.setValue(volume);

				return clip;
			}
		} catch (UnsupportedAudioFileException |
				 IOException |
				 LineUnavailableException e) {

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

	public void setVolume(float volume) {
		this.volume = volume;
		for (Clip clip : clips) {
			FloatControl control =
					(FloatControl) clip.getControl(
							FloatControl.Type.MASTER_GAIN);

			control.setValue(volume);
		}
	}
}