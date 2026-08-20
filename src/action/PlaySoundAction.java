package action;

import resource.Sound;

class PlaySoundAction extends Action {
	private final SoundValue sound;
	@Override
	public boolean consumesFrame() { return false; }

	public PlaySoundAction(SoundValue sound) {
		this.sound = sound;
	}

	@Override
	public void start() {
		Sound actualSound = (Sound) getContext().get(sound.getName());

		actualSound.play();
		finish();
	}
}