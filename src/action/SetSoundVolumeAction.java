package action;

import resource.Sound;

class SetSoundVolumeAction extends Action {
	private final SoundValue sound;
	private final Object volume;
	@Override
	public boolean consumesFrame() { return false; }

	public SetSoundVolumeAction(SoundValue sound, Object volume) {
		this.sound = sound;
		this.volume = volume;
	}

	@Override
	public void start() {
		Sound actualSound = (Sound) getVariable(sound.getName());
		actualSound.setVolume( (float) resolveDouble(volume) );
		finish();
	}
}