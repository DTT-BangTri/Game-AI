package puzzle;

import java.util.BitSet;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
public abstract class Algorithm {
	public static final int NOT_APPLICABLE = -1;
	public volatile static long numberVisited, numberExpanded;
	private static long startTime, endTime;
	public static int initialMovesEstimate, movesRequired;
	public static boolean running, solved;
	public static String shortestPath;

	public static float getRunningTimeInSeconds() {
		final long diff = endTime - startTime;
		// Have seen negative numbers on systems with multiple processors.
		if (diff < 0)
			return 0.0f;
		return diff / 1000f;
	}

	public static float getElapsedTimeInSeconds() {
		final long diff = System.currentTimeMillis() - startTime;
		// Have seen negative numbers on systems with multiple processors.
		if (diff < 0)
			return 0.0f;
		return diff / 1000f;
	}

	abstract void solvePuzzle(final long currentState, final int numOfThreads, BitSet walls);

	public void solve(final long currentState, final int numOfThreads, final BitSet walls) {
		Node.initialize();
		initialize();
		solvePuzzle(currentState, numOfThreads, walls);
		markEndTime();
		running = false;
	}

	public abstract void cleanup();

	public void start() {
		running = true;
		solved = false;
	}

	public void stop() {
		running = false;
	}

	private static void initialize() {
		startTime = System.currentTimeMillis();
		solved = false;
		running = true;
		numberVisited = 0;
		numberExpanded = 0;
	}

	private static void markEndTime() {
		endTime = System.currentTimeMillis();
	}
}
