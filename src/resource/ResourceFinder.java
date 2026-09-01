package resource;

import java.net.URL;

public final class ResourceFinder {
	
	public static final String IMAGE = "/assets/images/";
	public static final String SOUND = "/assets/sounds/";
	public static final String MUSIC = "/assets/music/";
	public static final String FONT = "/assets/fonts/";
	public static final String DIALOGUE = "/assets/dialogue/";
	public static final String SPELL = "/spell/";
	
	private ResourceFinder() {}

	public static URL find(
			String folder,
			String name,
			String... extensions
	) {
		for (String extension : extensions) {
			String path = folder + name + extension;
			
			URL url = ResourceFinder.class.getResource(path);

			if (url != null) { return url; }
		}
		return null;
	}
}