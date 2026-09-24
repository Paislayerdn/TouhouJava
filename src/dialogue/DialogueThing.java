package dialogue;

import entity.Thing;

public final class DialogueThing extends Thing {
	private String displayName;

	public DialogueThing(String name) {
		super();
		this.getAppearance().size /= 6;
		this.name = name;
		this.displayName = name;
	}

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }
}