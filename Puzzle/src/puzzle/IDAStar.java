package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IDAStar extends Algorithm {
	private Queue<BFSNode> queue;
	private DFSWorker[] workers;

	@Override
	void solvePuzzle(final long currentState, final int numOfThreads, final BitSet walls) {
		if (numOfThreads > 1) {
			solveMultiThreaded(currentState, numOfThreads, walls);
		} else {
			solveSingleThreaded(currentState, walls);
		}
	}

	private void solveMultiThreaded(final long currentState, final int numOfThreads,
			final BitSet walls) {
		if (PuzzleConfiguration.isVerbose()) {
			System.err.print("Creating starting positons for " + numOfThreads + " threads...");
		}
		findStartingPositions(currentState, numOfThreads, walls);
		initialMovesEstimate = movesRequired = Node.h(currentState);
		if (PuzzleConfiguration.isVerbose()) {
			System.err.println("done");
		}
		if (!solved) {
			final int numElements = this.queue.size();
			this.workers = new DFSWorker[numElements];
			for (int i = numElements - 1; i >= 0; --i) {
				this.workers[i] = new DFSWorker(walls);
			}
			do {
				if (PuzzleConfiguration.isVerbose()) {
					System.out.print("\nSearching paths of depth " + movesRequired + "...");
				}
				final ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
				final Iterator<BFSNode> it = this.queue.iterator();
				int index = 0;
				while (it.hasNext()) {
					final BFSNode node = it.next();
					final String currentPath = node.getPath();
					final DFSWorker worker = this.workers[index++];
					worker.setConfig(node.boardConfig, currentPath, movesRequired,
							currentPath.length() - 1);
					executor.execute(worker);
				}
				executor.shutdown();
				try {
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				} catch (final InterruptedException ie) {
					stop();
				}
				if (!solved) {
					movesRequired += 2;
				}
			} while (running);
		}
	}

	private void solveSingleThreaded(final long currentState, final BitSet walls) {
		initialMovesEstimate = movesRequired = Node.h(currentState);
		this.workers = new DFSWorker[1];
		final DFSWorker dfsWorker = new DFSWorker(walls);
		// Add to array so GUI can poll it for the stats in real time.
		this.workers[0] = dfsWorker;
		do {
			if (PuzzleConfiguration.isVerbose()) {
				System.out.print("\nSearching paths of depth " + movesRequired + "...");
			}
			dfsWorker.setConfig(currentState, "X", movesRequired, 0);
			dfsWorker.run();
			if (!solved) {
				movesRequired += 2;
			}
		} while (running);
	}

	private void completeBFS(final BFSNode node) {
		solved = true;
		shortestPath = node.getShortestPath();
		if (PuzzleConfiguration.isVerbose()) {
			System.out.println("done.");
		}
	}

	/**
	 * Performs a breadth-first search starting at currentState, finding
	 * howMany unique states from which to start the threads.
	 */
	private void findStartingPositions(final long currentState, final int howMany,
			final BitSet walls) {
		BFSNode currentNode = new BFSNode(currentState, true);
		currentNode.cost = 0;
		if (currentNode.boardConfig == Node.goalState) {
			completeBFS(currentNode);
			return;
		}
		this.queue = new LinkedList<BFSNode>();
		if (howMany == 1) {
			this.queue.add(currentNode);
			return;
		}
		int previousMovesRequired = 0;
		while (currentNode != null) {
			final char fromDirection = currentNode.direction;
			if (fromDirection != 'R') {
				final BFSNode left = currentNode.moveLeftNode(null);
				if (left != null && !walls.get(Node.posOfSpace(left.boardConfig))) {
					++numberExpanded;
					if (left.boardConfig == Node.goalState) {
						completeBFS(left);
						return;
					} else {
						this.queue.add(left);
					}
				}
			}
			if (fromDirection != 'L') {
				final BFSNode right = currentNode.moveRightNode(null);
				if (right != null && !walls.get(Node.posOfSpace(right.boardConfig))) {
					++numberExpanded;
					if (right.boardConfig == Node.goalState) {
						completeBFS(right);
						return;
					} else {
						this.queue.add(right);
					}
				}
			}
			if (fromDirection != 'D') {
				final BFSNode up = currentNode.moveUpNode(null);
				if (up != null && !walls.get(Node.posOfSpace(up.boardConfig))) {
					++numberExpanded;
					if (up.boardConfig == Node.goalState) {
						completeBFS(up);
						return;
					} else {
						this.queue.add(up);
					}
				}
			}
			if (fromDirection != 'U') {
				final BFSNode down = currentNode.moveDownNode(null);
				if (down != null && !walls.get(Node.posOfSpace(down.boardConfig))) {
					++numberExpanded;
					if (down.boardConfig == Node.goalState) {
						completeBFS(down);
						return;
					} else {
						this.queue.add(down);
					}
				}
			}
			currentNode = this.queue.peek();
			if (currentNode != null) {
				final byte movesRequired = currentNode.cost;
				if (movesRequired > previousMovesRequired) {
					previousMovesRequired = movesRequired;
					// Remove duplicate states while preserving the order of the
					// queue. We want to make sure that each thread we start is
					// working on a unique path.
					this.queue = new LinkedList<BFSNode>(new LinkedHashSet<BFSNode>(this.queue));
					if (this.queue.size() >= howMany) {
						break;
					}
				} else {
					currentNode = this.queue.poll();
				}
			}
		}
	}

	@Override
	public void cleanup() {
	}
}
