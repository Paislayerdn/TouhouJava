package resource;

import java.net.URL;

public final class ResourceFinder {
	private ResourceFinder() {

	}

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