package action;

public interface ScriptPropertySetter {
	default String[] getAdditionalScriptProperties() { return new String[0]; }
	default Object getScriptProperty(String name) { return null; }
	boolean setScriptProperty(String name, Object value);
}
