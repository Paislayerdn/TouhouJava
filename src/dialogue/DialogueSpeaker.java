	package dialogue;

public class DialogueSpeaker {
	private final String name;
	private String displayName;
	private String expression;

	private float x;
	private float y;

	public DialogueSpeaker(String name) {
		this.name = name;
		this.displayName = name;
	}

	public String getName() { return name; }
	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }
	
	public String getExpression() { return expression; }
	public void setExpression(String expression) { this.expression = expression; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	public void setPosition(float x, float y) { this.x = x; this.y = y; }
}