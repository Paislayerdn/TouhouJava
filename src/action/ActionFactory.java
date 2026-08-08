package action;

@FunctionalInterface
public interface ActionFactory {
	Action create();
}