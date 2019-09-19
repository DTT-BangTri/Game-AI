package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 *
 * This class is essentially a stripped down version of Map.Entry
 * implemented inside Java's HashMap class in. It uses primitives
 * instead of objects for efficiency.
 */
public final class Entry {
	final long key;
	final int hash;
	byte value;
	Entry next;

	Entry(final int h, final long k, final byte v, final Entry n) {
		this.value = v;
		this.next = n;
		this.key = k;
		this.hash = h;
	}

	public long getKey() {
		return this.key;
	}

	public byte getValue() {
		return this.value;
	}

	public byte setValue(final byte newValue) {
		final byte oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Entry))
			return false;
		final Entry e = (Entry) o;
		if (this.key == e.key) {
			if (this.value == e.value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return keyHashCode(this.key);
	}

	@Override
	public String toString() {
		return this.key + " = " + this.value;
	}

	/**
	 * http://www.concentric.net/~ttwang/tech/inthash.htm
	 */
	public static int keyHashCode(long key) {
		key = (~key) + (key << 18); // key = (key << 18) - key - 1;
		key = key ^ (key >>> 31);
		key = key * 21; // key = (key + (key << 2)) + (key << 4);
		key = key ^ (key >>> 11);
		key = key + (key << 6);
		key = key ^ (key >>> 22);
		return (int) key;
	}

	public static int valueHashCode(final byte value) {
		return value;
	}
}
