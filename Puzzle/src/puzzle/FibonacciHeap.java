package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
public final class FibonacciHeap {
	private AStarNode min;
	private int n;

	public FibonacciHeap() {
	}

	public AStarNode min() {
		return this.min;
	}

	public AStarNode insert(final AStarNode toInsert) {
		if (this.min != null) {
			toInsert.left = this.min;
			toInsert.right = this.min.right;
			this.min.right = toInsert;
			toInsert.right.left = toInsert;
			if (toInsert.cost < this.min.cost) {
				this.min = toInsert;
			}
		} else {
			this.min = toInsert;
		}
		++this.n;
		return toInsert;
	}

	public void delete(final AStarNode node) {
		decreaseKey(node, Integer.MIN_VALUE);
		removeMin();
	}

	public boolean isEmpty() {
		return this.min == null;
	}

	public AStarNode removeMin() {
		final AStarNode z = this.min;
		if (z != null) {
			int i = z.degree;
			AStarNode x = z.child;
			while (i > 0) {
				final AStarNode nextChild = x.right;
				x.left.right = x.right;
				x.right.left = x.left;
				x.left = this.min;
				x.right = this.min.right;
				this.min.right = x;
				x.right.left = x;
				x.parent = null;
				x = nextChild;
				--i;
			}
			z.left.right = z.right;
			z.right.left = z.left;
			if (z == z.right) {
				this.min = null;
			} else {
				this.min = z.right;
				consolidate();
			}
			--this.n;
		}
		return z;
	}

	public void decreaseKey(final AStarNode x, final int c) {
		if (c > x.cost) {
			System.err.println("Error: new key is greater than current key.");
			return;
		}
		x.cost = c;
		final AStarNode y = x.parent;
		if ((y != null) && (x.cost < y.cost)) {
			cut(x, y);
			cascadingCut(y);
		}
		if (x.cost < this.min.cost) {
			this.min = x;
		}
	}

	public int size() {
		return this.n;
	}

	public FibonacciHeap union(final FibonacciHeap heap1, final FibonacciHeap heap2) {
		final FibonacciHeap heap = new FibonacciHeap();
		if ((heap1 != null) && (heap2 != null)) {
			heap.min = heap1.min;
			if (heap.min != null) {
				if (heap2.min != null) {
					heap.min.right.left = heap2.min.left;
					heap2.min.left.right = heap.min.right;
					heap.min.right = heap2.min;
					heap2.min.left = heap.min;
					if (heap2.min.cost < heap1.min.cost) {
						heap.min = heap2.min;
					}
				}
			} else {
				heap.min = heap2.min;
			}
			heap.n = heap1.n + heap2.n;
		}
		return heap;
	}

	private void cascadingCut(final AStarNode y) {
		final AStarNode z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}

	private void consolidate() {
		final int D = this.n + 1;
		final AStarNode A[] = new AStarNode[D];
		for (int i = 0; i < D; ++i) {
			A[i] = null;
		}
		int k = 0;
		AStarNode x = this.min;
		if (x != null) {
			++k;
			for (x = x.right; x != this.min; x = x.right) {
				++k;
			}
		}
		while (k > 0) {
			int d = x.degree;
			final AStarNode rightAStarNode = x.right;
			while (A[d] != null) {
				AStarNode y = A[d];
				if (x.cost > y.cost) {
					final AStarNode temp = y;
					y = x;
					x = temp;
				}
				link(y, x);
				A[d] = null;
				++d;
			}
			A[d] = x;
			x = rightAStarNode;
			--k;
		}
		this.min = null;
		for (int i = 0; i < D; ++i) {
			final AStarNode ai = A[i];
			if (ai != null) {
				if (this.min != null) {
					ai.left.right = ai.right;
					ai.right.left = ai.left;
					ai.left = this.min;
					ai.right = this.min.right;
					this.min.right = ai;
					ai.right.left = ai;
					if (ai.cost < this.min.cost) {
						this.min = ai;
					}
				} else {
					this.min = ai;
				}
			}
		}
	}

	private void cut(final AStarNode x, final AStarNode y) {
		x.left.right = x.right;
		x.right.left = x.left;
		y.degree--;
		if (y.child == x) {
			y.child = x.right;
		}
		if (y.degree == 0) {
			y.child = null;
		}
		x.left = this.min;
		x.right = this.min.right;
		this.min.right = x;
		x.right.left = x;
		x.parent = null;
		x.mark = false;
	}

	private void link(final AStarNode node1, final AStarNode node2) {
		node1.left.right = node1.right;
		node1.right.left = node1.left;
		node1.parent = node2;
		if (node2.child == null) {
			node2.child = node1;
			node1.right = node1;
			node1.left = node1;
		} else {
			node1.left = node2.child;
			node1.right = node2.child.right;
			node2.child.right = node1;
			node1.right.left = node1;
		}
		node2.degree++;
		node1.mark = false;
	}
}
