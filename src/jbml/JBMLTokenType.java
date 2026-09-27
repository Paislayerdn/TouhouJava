package jbml;

public enum JBMLTokenType {
	// Structure
	SCENE,
	WORLD,
	SKY,
	EVENT,
	END,

	// Declarations
	CAMERA,
	OBJECT,
	PLANE,

	// Commands
	SET,
	CHANGE,
	TWEEN,
	CALL,
	PARALLEL,
	FOREVER,
	WAIT,

	// Literals
	IDENTIFIER,
	STRING,
	NUMBER,
	TRUE,
	FALSE,

	// Special
	EOF
}