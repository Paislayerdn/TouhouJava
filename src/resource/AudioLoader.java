package resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class AudioLoader {

	private static final String[] EXTENSIONS = {
		".wav",
		".aiff",
		".au"
	};

	// Utility class
	private AudioLoader() {}

	// Default for Sound
	public static AudioData load(String name) {
		return load(ResourceFinder.SOUND, name);
	}
	public static AudioData load(String path, String name) {
		try {
			URL url = ResourceFinder.find(
				path,
				name,
				EXTENSIONS
			);

			if (url == null) {
				System.out.println("[AudioLoader] Cannot find audio: " + path + name);
				return null;
			}

			try (AudioInputStream audio =
					AudioSystem.getAudioInputStream(url)) {

				AudioFormat format = audio.getFormat();

				ByteArrayOutputStream output = new ByteArrayOutputStream();

				byte[] buffer = new byte[4096];
				int bytesRead;

				while ((bytesRead = audio.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}

				return new AudioData( format, output.toByteArray() );
			}

		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}