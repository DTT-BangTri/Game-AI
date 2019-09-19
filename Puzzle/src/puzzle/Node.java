package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
public abstract class Node {
	// The tile positions array contains the 4-bit position of the tile's location
	// within a 32-bit integer.
	// Example: CostTable0 contains only the positions of tiles 2, 3, and 4.
	//   So, when mapping the configuration 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0
	//   to an index in the table, you will get:
	//   0000..0000[4 bits for location of tile 4]
	//   [4 bits for location of tile 3][4 bits for location of tile 2]
	// The same technique is applied for the other two cost tables.
	static final int[] tilePositions = { -1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5 };
	static int dimension, numOfTiles, numOfTilesMinusOne, heuristic;
	static long goalState, goalStatePositions;

	static void initialize() {
		dimension = PuzzleConfiguration.getDimension();
		numOfTiles = PuzzleConfiguration.getNumOfTiles();
		numOfTilesMinusOne = Node.numOfTiles - 1;
		goalState = PuzzleConfiguration.getGoalState();
		goalStatePositions = PuzzleConfiguration.getGoalStatePositions();
		heuristic = PuzzleConfiguration.getHeuristic();
	}

	public static int h(final long boardConfig) {
		if (heuristic == PuzzleConfiguration.HEURISTIC_PD) {
			if (numOfTiles == 16) {
				int index0 = 0, index1 = 0, index2 = 0;
				// Create three different indexes that contain only the positions of
				// tiles applicable to the corresponding pattern database.
				for (int pos = numOfTilesMinusOne; pos >= 0; --pos) {
					final int tile = (int) ((boardConfig >> (pos << 2)) & 0xF);
					if (tile >= 2 && tile <= 4) {
						index0 |= pos << (tilePositions[tile] << 2);
					} else if (tile == 1 || tile == 5 || tile == 6 || tile == 9 || tile == 10
							|| tile == 13) {
						index1 |= pos << (tilePositions[tile] << 2);
					} else if (tile != 0) {
						index2 |= pos << (tilePositions[tile] << 2);
					}
				}
				return PuzzleConfiguration.costTable_15_puzzle_0[index0]
						+ PuzzleConfiguration.costTable_15_puzzle_1[index1]
						+ PuzzleConfiguration.costTable_15_puzzle_2[index2];
			} else {
				return PuzzleConfiguration.patternDatabase_8_puzzle.get(boardConfig);
			}
		}
		// Implements the Manhattan Distance heuristic
		int distance = 0;
		final long currentPositions = Utility.getPositionsAsLong(boardConfig, numOfTilesMinusOne);
		for (int pos = numOfTiles - 1; pos >= 0; --pos) {
			final int posTimes4 = pos << 2, goalStateTile = (int) ((goalState >> posTimes4) & 0xF), currentTile = (int) ((boardConfig >> posTimes4) & 0xF);
			if (goalStateTile != 0 && currentTile != goalStateTile) {
				final int currentPosition = Utility.getElementAt(currentPositions, goalStateTile), goalStatePosition = Utility
						.getElementAt(goalStatePositions, goalStateTile), currentX = currentPosition
						/ dimension, goalStateX = goalStatePosition / dimension, currentY = currentPosition
						% dimension, goalStateY = goalStatePosition % dimension;
				int val = Math.abs(currentX - goalStateX)+ Math.abs(currentY - goalStateY);
//				if (val1 < 0)
//					val1 *= -1;
//				if (val2 < 0)
//					val2 *= -1;
				distance += val;
			}
		}
		// Add linear conflicts to the Manhattan Distance, if chosen
		if (heuristic == PuzzleConfiguration.HEURISTIC_LC) {
			final int dimMinusOne = dimension - 1, lastIndexInColumn = dimension * dimMinusOne, conflictsArraySize = numOfTiles + 1;
			// Linear row conflicts
			final boolean[] rowConflicts = new boolean[conflictsArraySize];
			for (int row = 0; row < dimension; ++row) {
				final int lowerIndex = row * dimension, upperIndex = lowerIndex + dimMinusOne, lowerBound = lowerIndex + 1, upperBound = upperIndex + 1;
				for (int i = lowerIndex; i < upperIndex; ++i) {
					for (int j = i + 1; j <= upperIndex; ++j) {
						final byte iValue = (byte) ((boardConfig >> (i << 2)) & 0xF), jValue = (byte) ((boardConfig >> (j << 2)) & 0xF);
						if ((iValue >= lowerBound) && (iValue <= upperBound)
								&& (jValue >= lowerBound) && (jValue <= upperBound)
								&& (iValue > jValue)) {
							rowConflicts[iValue] = true;
							rowConflicts[jValue] = true;
							distance += 2;
						}
					}
				}
			}
			// Linear column conflicts
			final boolean[] colConflicts = new boolean[conflictsArraySize];
			final byte[] set = new byte[dimension];
			for (int col = 0; col < dimension; ++col) {
				final int lowerIndex = col, upperIndex = lowerIndex + lastIndexInColumn, lowerBound = lowerIndex + 1, upperBound = upperIndex + 1;
				int pos = 0;
				for (int i = lowerBound; i <= upperBound; i += dimension) {
					set[pos++] = (byte) i;
				}
				for (int i = lowerIndex; i <= upperIndex; i += dimension) {
					for (int j = i + dimension; j <= upperIndex; j += dimension) {
						final byte iValue = (byte) ((boardConfig >> (i << 2)) & 0xF), jValue = (byte) ((boardConfig >> (j << 2)) & 0xF);
						boolean iFound = false, jFound = false;
						for (int x = dimMinusOne; x >= 0; --x) {
							final byte val = set[x];
							if (iValue == val)
								iFound = true;
							if (jValue == val)
								jFound = true;
						}
						if (iFound && jFound && (iValue > jValue)) {
							colConflicts[iValue] = true;
							colConflicts[jValue] = true;
							distance += 2;
						}
					}
				}
			}
		}
		return distance;
	}

	public static int posOfSpace(final long boardConfig) {
		for (int i = numOfTiles - 1; i >= 0; --i) {
			if ((byte) ((boardConfig >> (i << 2)) & 0xF) == 0) {
				return i;
			}
		}
		return -1;
	}
}
