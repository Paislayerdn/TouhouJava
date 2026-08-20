package action;

import resource.ResourceLoader;
import resource.Sound;

class SoundAction extends Action {
	private final String name;
	private final String path;
	@Override
	public boolean consumesFrame() { return false; }

	public SoundAction(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	@Override
	public void start() {
		getContext().declare(
			name,
			ResourceLoader.sound(path)
		);

		finish();
	}
}