package resource;

import java.net.URL;

final class ResourcePath {
	private ResourcePath() {}

	public static final String IMAGE = "/assets/images/";
	public static final String SOUND = "/assets/sounds/";
	public static final String MUSIC = "/assets/music/";
	public static final String FONT  = "/assets/fonts/";
}

public final class ResourceFinder {
	private ResourceFinder() {}

	public static URL find(
			String folder,
			String name,
			String... extensions
	) {
		for (String extension : extensions) {
			String path = folder + name + extension;
			
			URL url = ResourceFinder.class.getResource(path);

			if (url != null) {
				return url;
			}

		}
		return null;
	}
}