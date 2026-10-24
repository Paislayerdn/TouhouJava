package dialogue;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import graphics.Renderer;
import graphics.TextBox;
import graphics.TextAlign;

import state.Playing;

import resource.ResourceLoader;
import resource.Sound;

import main.Input;

public final class DialogueRunner {
	private final Playing parent;
	private final Dialogue dialogue;

	private int commandIndex;
	private DialogueThing currentSpeaker;

	private final TextBox textBox;

	private boolean waitingForInput;
	private boolean skipping;
	private final Sound advanceSound;
	
	public DialogueRunner(Playing parent, String name) {
		this( parent, DialogueParser.parse( ResourceLoader.dialogue(name) ) );
	}
	public DialogueRunner(Playing parent, Dialogue dialogue) {
		this.parent = parent;
		this.dialogue = dialogue;
		this.advanceSound = ResourceLoader.sound("[TH] Fires");
		this.advanceSound.setVolume(-12.0f);
		
		this.textBox = new TextBox("",
			0, 0,
			800, 200, TextAlign.CENTER
		);
		this.textBox.setGlyphSize(75.0f);
	}
	
	public Object resolveVariable(String name) {
		LocalDateTime now = LocalDateTime.now();

		return switch (name) {
			case "" -> "%";
			case "/" -> "/";

			case "playtime" -> -1; // TODO

			case "boss" -> parent.getBoss().getName();
			case "player" -> parent.getPlayer().getName();

			case "second" -> String.format("%02d", now.getSecond());
			case "minute" -> String.format("%02d", now.getMinute());
			case "hour" -> String.format("%02d", now.getHour());

			case "day" -> now.getDayOfMonth();
			case "dayth" -> getDayWithSuffix(now.getDayOfMonth());

			case "month" -> now.getMonth()
				.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

			case "year" -> now.getYear();

			default -> null;
		};
	}
	private String getDayWithSuffix(int day) {
		String suffix;

		if (day >= 11 && day <= 13) {
			suffix = "th";
		} else {
			switch (day % 10) {
				case 1 -> suffix = "st";
				case 2 -> suffix = "nd";
				case 3 -> suffix = "rd";
				default -> suffix = "th";
			}
		}

		return day + suffix;
	}

	
	public Dialogue getDialogue() { return dialogue; }
	
	public DialogueThing getCurrentSpeaker() { return currentSpeaker; }
	public void setCurrentSpeaker(DialogueThing speaker) { currentSpeaker = speaker; }
	public void removeCurrentSpeaker() {
		if (currentSpeaker == null) return;

		dialogue.removeSpeaker(currentSpeaker.getName());
		currentSpeaker = null;
	}
	public void setCurrentSpeakerName(String displayName) {
		currentSpeaker.setDisplayName(displayName);
	}
	
	public String getCurrentText() { return textBox.getText(); }
	public void setCurrentText(String text) { textBox.setText(text); }
	
	public void waitForAdvance() { waitingForInput = !skipping; }

	public void update() {
		if (Input.tap.Q || Input.hold.Q) {
			skipping = true;
			waitingForInput = false;
		}

		for (DialogueThing speaker : dialogue.getSpeakers()) {
			speaker.update();
		}

		if (waitingForInput) {
			if (Input.tap.Z) {
				advanceSound.play();
			} else if (!skipping) {
				return;
			}

			waitingForInput = false;
		}

		while (!waitingForInput
				&& commandIndex < dialogue.getCommands().size()) {

			DialogueCommand command =
				dialogue.getCommands().get(commandIndex++);

			command.execute(this);
		}
	}
	
	public void draw(Renderer renderer) {
		for (DialogueThing speaker : dialogue.getSpeakers())
			speaker.draw(renderer);

		textBox.draw(renderer);
	}
	
	public boolean isSkipping() { return skipping; }

	public void playSound(String filename) {
		ResourceLoader.sound(filename).play();
	}

	public boolean isFinished() {
		return commandIndex >= dialogue.getCommands().size()
			&& !waitingForInput;
	}
}
