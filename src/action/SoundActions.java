package action;

import resource.ResourceLoader;
import resource.Sound;

final class SoundAction extends Action {
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
		getContext().declare( name, ResourceLoader.sound(path) );

		finish();
	}
}

final class SetSoundVolumeAction extends Action {
	private final String sound;
	private final Object volume;
	@Override
	public boolean consumesFrame() { return false; }

	public SetSoundVolumeAction(String sound, Object volume) {
		this.sound = sound;
		this.volume = volume;
	}

	@Override
	public void start() {
		Sound actualSound = (Sound) getVariable(sound);
		actualSound.setVolume( resolveFloat(volume) );
		finish();
	}
}

final class PlaySoundAction extends Action {
	private final String sound;
	@Override
	public boolean consumesFrame() { return false; }

	public PlaySoundAction(String sound) {
		this.sound = sound;
	}

	@Override
	public void start() {
		Sound actualSound = (Sound) getContext().get(sound);
		actualSound.play();
		finish();
	}
}