package dialogue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialogue {
	private final Map<String, DialogueSpeaker> speakers;
	private final List<DialogueCommand> commands;

	public Dialogue() {
		this.speakers = new HashMap<>();
		this.commands = new ArrayList<>();
	}

	public Collection<DialogueSpeaker> getAllSpeakers() {
		return speakers.values();
	}
	public DialogueSpeaker getSpeaker(String name) {
		return getOrCreateSpeaker(name);
	}
	public DialogueSpeaker getOrCreateSpeaker(String name) {
		return speakers.computeIfAbsent(name, DialogueSpeaker::new);
	}
	public void removeSpeaker(String name) {
		speakers.remove(name);
	}

	public void addCommand(DialogueCommand command) { commands.add(command); }

	public List<DialogueCommand> getCommands() { return commands; }
}