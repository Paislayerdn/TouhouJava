package state.gameplay;

public interface GameplayEvent {
	void start();
	void update();
	boolean isFinished();
}