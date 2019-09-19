package puzzle;

import java.util.BitSet;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
public final class DFSWorker implements Runnable {
	private long currentState, numberVisited, numberExpanded;
	private int depth, pos, yieldCount;
	private boolean solved;
	private char fromDirection;
	private final char[] path = new char[81];
	private final BitSet walls;

	public DFSWorker(BitSet walls) {
		this.walls = walls;
	}

	public void setConfig(final long currentState, final String pathStr, final int depth,
			final int pos) {
		this.currentState = currentState;
		this.depth = depth;
		this.pos = pos;
		int i = 0;
		// It's not necessary to initialize path elements each time this method
		// is called, since subsequent paths will always be longer by 2 moves.
		for (final int len = pathStr.length(); i < len; ++i) {
			this.path[i] = pathStr.charAt(i);
		}
		this.fromDirection = this.path[i - 1];
		this.numberVisited = this.numberExpanded = this.yieldCount = 0;
	}

	@Override
	public void run() {
		depthFirstSearch(this.currentState, this.fromDirection, this.depth, this.pos);
		Algorithm.numberVisited += this.numberVisited;
		Algorithm.numberExpanded += this.numberExpanded;
		if (this.solved) {
			Algorithm.shortestPath = getShortestPath();
		}
	}

	public String getShortestPath() {
		return String.copyValueOf(this.path).trim().substring(1);
	}

	private void depthFirstSearch(final long currentState, final char fromDirection,
			final int depth, final int pos) {
		if (!Algorithm.running)
			return;
		if (currentState == Node.goalState) {
			Algorithm.solved = true;
			Algorithm.running = false;
			this.path[pos] = fromDirection;
			this.solved = true;
			if (PuzzleConfiguration.isVerbose()) {
				System.out.println("done.");
			}
			return;
		}
		// It is necessary to yield periodically so the GUI threads can redraw
		// elements without noticeable pauses.
		if (this.yieldCount++ == 50000) {
			Algorithm.numberVisited += this.numberVisited;
			Algorithm.numberExpanded += this.numberExpanded;
			this.numberVisited = this.numberExpanded = this.yieldCount = 0;
			Thread.yield();
		}
		final int posOfSpace = Node.posOfSpace(currentState), posPlusOne = pos + 1;
		if (fromDirection != 'R') {
			final long successor = IDAStarNode.moveLeft(currentState, posOfSpace);
			if (successor != 0 && !this.walls.get(Node.posOfSpace(successor))) {
				++this.numberExpanded;
				if (posPlusOne + Node.h(successor) <= depth) {
					depthFirstSearch(successor, 'L', depth, posPlusOne);
				} else {
					++this.numberVisited;
				}
				if (this.solved) {
					this.path[pos] = fromDirection;
					return;
				}
			}
		}
		if (fromDirection != 'L') {
			final long successor = IDAStarNode.moveRight(currentState, posOfSpace);
			if (successor != 0 && !this.walls.get(Node.posOfSpace(successor))) {
				++this.numberExpanded;
				if (posPlusOne + Node.h(successor) <= depth) {
					depthFirstSearch(successor, 'R', depth, posPlusOne);
				} else {
					++this.numberVisited;
				}
				if (this.solved) {
					this.path[pos] = fromDirection;
					return;
				}
			}
		}
		if (fromDirection != 'D') {
			final long successor = IDAStarNode.moveUp(currentState, posOfSpace);
			if (successor != 0 && !this.walls.get(Node.posOfSpace(successor))) {
				++this.numberExpanded;
				if (posPlusOne + Node.h(successor) <= depth) {
					depthFirstSearch(successor, 'U', depth, posPlusOne);
				} else {
					++this.numberVisited;
				}
				if (this.solved) {
					this.path[pos] = fromDirection;
					return;
				}
			}
		}
		if (fromDirection != 'U') {
			final long successor = IDAStarNode.moveDown(currentState, posOfSpace);
			if (successor != 0 && !this.walls.get(Node.posOfSpace(successor))) {
				++this.numberExpanded;
				if (posPlusOne + Node.h(successor) <= depth) {
					depthFirstSearch(successor, 'D', depth, posPlusOne);
				} else {
					++this.numberVisited;
				}
				if (this.solved) {
					this.path[pos] = fromDirection;
					return;
				}
			}
		}
	}
}
