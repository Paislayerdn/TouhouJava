package action;

public class SoundValue {
	private final String name;

	public SoundValue(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Action play() {
		return new PlaySoundAction(this);
	}

	public Action setVolume(Object volume) {
		return new SetSoundVolumeAction(this, volume);
	}
}