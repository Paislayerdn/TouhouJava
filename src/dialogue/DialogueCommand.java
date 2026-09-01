package dialogue;

@FunctionalInterface
public interface DialogueCommand {
	String SPEAKER = "/>";
	String EXPRESSION = "/e";
	String MOVE = "/@";
	String SFX = "/f";
	String TEXT = "/d";
	String MISC = "/x";

	void execute(DialogueRunner runner);
}