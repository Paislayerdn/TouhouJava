package dialogue;

public class DialogueSpeaker {
	private final String name;
	private String displayName;
	private String expression;

	private double x;
	private double y;

	public DialogueSpeaker(String name) {
		this.name = name;
		this.displayName = name;
	}

	public String getName() { return name; }
	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }
	
	public String getExpression() { return expression; }
	public void setExpression(String expression) { this.expression = expression; }
	
	public double getX() { return x; }
	public double getY() { return y; }
	public void setPosition(double x, double y) { this.x = x; this.y = y; }
}