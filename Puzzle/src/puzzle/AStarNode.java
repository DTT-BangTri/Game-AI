package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
public final class AStarNode extends Node {
	public AStarNode parent, child, right, left, previous;
	public char direction = 'X';
	public int degree, cost;
	public boolean mark;
	private int g;
	private long boardConfig;

	public AStarNode(final long boardConfig) {
		this.boardConfig = boardConfig;
		this.right = this.left = this;
	}

	@Override
	public int hashCode() {
		return Entry.keyHashCode(this.boardConfig);
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof AStarNode) {
			final AStarNode other = (AStarNode) o;
			return (this.boardConfig == other.boardConfig);
		}
		return false;
	}

	public boolean isGoalState() {
		return this.boardConfig == PuzzleConfiguration.getGoalState();
	}

	public int g() {
		return this.g;
	}

	public int h() {
		return h(this.boardConfig);
	}

	public String getPath() {
		final StringBuilder path = new StringBuilder();
		AStarNode p = this;
		while (p.direction != 'X') {
			path.insert(0, p.direction);
			p = p.previous;
		}
		return path.toString();
	}

	public AStarNode copyOfNode() {
		final AStarNode s = new AStarNode(this.boardConfig);
		s.previous = this;
		s.g = this.g + 1;
		return s;
	}

	public AStarNode moveLeft() {
		final int posOfSpace = posOfSpace();
		if (posOfSpace % dimension == 0) {
			return null;
		}
		final AStarNode copy = copyOfNode();
		// Swap tile with space.
		final int posTimes4 = posOfSpace << 2, posMinusOneTimes4 = (posOfSpace - 1) << 2;
		final long space = (copy.boardConfig >> posTimes4) & 0xF, tile = (copy.boardConfig >> posMinusOneTimes4) & 0xF;
		final long zeroBitTile = (long) 0xF << posMinusOneTimes4;
		copy.boardConfig &= ~zeroBitTile;
		copy.boardConfig |= tile << posTimes4;
		copy.boardConfig |= space << posMinusOneTimes4;
		copy.direction = 'L';
		copy.cost = copy.g + copy.h();
		return copy;
	}

	public AStarNode moveRight() {
		final int posOfSpace = posOfSpace(), posPlusOne = posOfSpace + 1;
		if (posPlusOne % dimension == 0) {
			return null;
		}
		final AStarNode copy = copyOfNode();
		// Swap tile with space.
		final int posTimes4 = posOfSpace << 2, posPlusOneTimes4 = posPlusOne << 2;
		final long space = (copy.boardConfig >> posTimes4) & 0xF, tile = (copy.boardConfig >> posPlusOneTimes4) & 0xF;
		final long zeroBitTile = (long) 0xF << posPlusOneTimes4;
		copy.boardConfig &= ~zeroBitTile;
		copy.boardConfig |= tile << posTimes4;
		copy.boardConfig |= space << posPlusOneTimes4;
		copy.direction = 'R';
		copy.cost = copy.g + copy.h();
		return copy;
	}

	public AStarNode moveUp() {
		final int posOfSpace = posOfSpace();
		if (posOfSpace < dimension) {
			return null;
		}
		final AStarNode copy = copyOfNode();
		// Swap tile with space.
		final int posTimes4 = posOfSpace << 2, posMinusDimTimes4 = (posOfSpace - dimension) << 2;
		final long space = (copy.boardConfig >> posTimes4) & 0xF, tile = (copy.boardConfig >> posMinusDimTimes4) & 0xF;
		final long zeroBitTile = (long) 0xF << posMinusDimTimes4;
		copy.boardConfig &= ~zeroBitTile;
		copy.boardConfig |= tile << posTimes4;
		copy.boardConfig |= space << posMinusDimTimes4;
		copy.direction = 'U';
		copy.cost = copy.g + copy.h();
		return copy;
	}

	public AStarNode moveDown() {
		final int posOfSpace = posOfSpace();
		if (posOfSpace >= numOfTiles - dimension) {
			return null;
		}
		final AStarNode copy = copyOfNode();
		// Swap tile with space.
		final int posTimes4 = posOfSpace << 2, posPlusDimTimes4 = (posOfSpace + dimension) << 2;
		final long space = (copy.boardConfig >> posTimes4) & 0xF, tile = (copy.boardConfig >> posPlusDimTimes4) & 0xF;
		final long zeroBitTile = (long) 0xF << posPlusDimTimes4;
		copy.boardConfig &= ~zeroBitTile;
		copy.boardConfig |= tile << posTimes4;
		copy.boardConfig |= space << posPlusDimTimes4;
		copy.direction = 'D';
		copy.cost = copy.g + copy.h();
		return copy;
	}

	public int posOfSpace() {
		return posOfSpace(this.boardConfig);
	}
}
