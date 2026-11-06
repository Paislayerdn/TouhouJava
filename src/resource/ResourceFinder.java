package resource;

import java.net.URL;

public final class ResourceFinder {
	public static final String IMAGE = "/assets/images/",
			TITLE_IMAGE = IMAGE + "title/",
			DIALOGUE_IMAGE = IMAGE + "dialogues/",
			BACKGROUND_IMAGE = IMAGE + "background/",
			SOUND = "/assets/sounds/",
			MUSIC = "/assets/music/",
			FONT = "/assets/fonts/",
			DIALOGUE = "/assets/dialogue/",
			SPELL = "/spell/",
			BACKGROUND = "/assets/background/";
	
	private ResourceFinder() {}

	public static URL find(String folder, String name) {
		return ResourceFinder.class.getResource(folder + name);
	}
	public static URL find(
			String folder,
			String name,
			String... extensions
	) {
		for (String extension : extensions) {
			String path = folder + name + extension;
			
			URL url = ResourceFinder.class.getResource(path);

			if (url != null) return url;
		}
		return null;
	}
}
