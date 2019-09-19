package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.util.BitSet;
import java.util.HashMap;

public final class AStar extends Algorithm {
	private HashMap<Integer, AStarNode> openMap, closedMap;

	@Override
	void solvePuzzle(final long currentState, final int numOfThreads, final BitSet walls) {
		initialMovesEstimate = NOT_APPLICABLE;
		AStarNode currentConfig = new AStarNode(currentState);
		this.openMap = new HashMap<Integer, AStarNode>();
		this.closedMap = new HashMap<Integer, AStarNode>();
		final FibonacciHeap openHeap = new FibonacciHeap();
		int previous = 0;
		while (running) {
			++numberVisited;
			movesRequired = currentConfig.g();
			if (movesRequired > previous) {
				if (PuzzleConfiguration.isVerbose()) {
					System.out.print("\nSearching paths of length " + movesRequired + " moves...");
				}
				previous = movesRequired;
			}
			if (currentConfig.isGoalState()) {
				if (PuzzleConfiguration.isVerbose()) {
					System.out.println("done.");
				}
				shortestPath = currentConfig.getPath();
				solved = true;
				return;
			}
			AStarNode successor;
			successor = currentConfig.moveLeft();
			if (successor != null && !walls.get(successor.posOfSpace())) {
				final int hashCode = successor.hashCode(), cost = successor.cost;
				final AStarNode leftEl = this.closedMap.get(hashCode);
				if (leftEl == null) {
					final AStarNode leftEl2 = this.openMap.get(hashCode);
					if (leftEl2 == null) {
						openHeap.insert(successor);
						this.openMap.put(hashCode, successor);
						++numberExpanded;
					} else if (cost < leftEl2.cost) {
						openHeap.decreaseKey(leftEl2, cost);
					}
				}
			}
			successor = currentConfig.moveRight();
			if (successor != null && !walls.get(successor.posOfSpace())) {
				final int hashCode = successor.hashCode(), cost = successor.cost;
				final AStarNode rightEl = this.closedMap.get(hashCode);
				if (rightEl == null) {
					final AStarNode rightEl2 = this.openMap.get(hashCode);
					if (rightEl2 == null) {
						openHeap.insert(successor);
						this.openMap.put(hashCode, successor);
						++numberExpanded;
					} else if (cost < rightEl2.cost) {
						openHeap.decreaseKey(rightEl2, cost);
					}
				}
			}
			successor = currentConfig.moveUp();
			if (successor != null && !walls.get(successor.posOfSpace())) {
				final int hashCode = successor.hashCode(), cost = successor.cost;
				final AStarNode upEl = this.closedMap.get(hashCode);
				if (upEl == null) {
					final AStarNode upEl2 = this.openMap.get(hashCode);
					if (upEl2 == null) {
						openHeap.insert(successor);
						this.openMap.put(hashCode, successor);
						++numberExpanded;
					} else if (cost < upEl2.cost) {
						openHeap.decreaseKey(upEl2, cost);
					}
				}
			}
			successor = currentConfig.moveDown();
			if (successor != null && !walls.get(successor.posOfSpace())) {
				final int hashCode = successor.hashCode(), cost = successor.cost;
				final AStarNode downEl = this.closedMap.get(hashCode);
				if (downEl == null) {
					final AStarNode downEl2 = this.openMap.get(hashCode);
					if (downEl2 == null) {
						openHeap.insert(successor);
						this.openMap.put(hashCode, successor);
						++numberExpanded;
					} else if (cost < downEl2.cost) {
						openHeap.decreaseKey(downEl2, cost);
					}
				}
			}
			this.closedMap.put(currentConfig.hashCode(), currentConfig);
			currentConfig = openHeap.removeMin();
		}
	}

	@Override
	public void cleanup() {
		this.openMap = null;
		this.closedMap = null;
	}
}
