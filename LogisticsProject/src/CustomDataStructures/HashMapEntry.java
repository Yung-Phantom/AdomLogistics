package CustomDataStructures;

/**
 * Represents a single key-value mapping stored within a bucket of a custom
 * HashMap.
 * <p>
 * Implements a singly linked list structure via {@link #next} to handle
 * collisions
 * using separate chaining. Each {@code HashMapEntry} stores:
 * </p>
 * <ul>
 * <li>The key associated with the mapping</li>
 * <li>The value associated with that key</li>
 * <li>A reference to the next entry in the chain (or {@code null} if none)</li>
 * </ul>
 *
 * <h3>Usage Notes:</h3>
 * <ul>
 * <li>Equality is determined solely by the key (via
 * {@link #equals(Object)}).</li>
 * <li>{@code null} keys are supported; their hash code is treated as
 * {@code 0}.</li>
 * <li>{@link #setValue(Object)} returns the previous value, mirroring standard
 * Map.Entry behavior.</li>
 * </ul>
 *
 * @param <K> the type of keys maintained by this entry
 * @param <V> the type of mapped values
 */
public class HashMapEntry<K, V> {

    /**
     * The key for this mapping. May be {@code null}.
     */
    private K key;

    /**
     * The value associated with the key. May be {@code null}.
     */
    private V value;

    /**
     * The next entry in the same bucket chain, or {@code null} if this is the last
     * entry.
     */
    private HashMapEntry<K, V> next;

    /**
     * Constructs a new {@code HashMapEntry} with the specified key, value, and
     * next-node reference.
     *
     * @param key   the key for this entry (may be {@code null})
     * @param value the value for this entry (may be {@code null})
     * @param next  the next entry in the bucket chain, or {@code null} if none
     */
    public HashMapEntry(K key, V value, HashMapEntry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key (may be {@code null})
     */
    public K getKey() {
        return key;
    }

    /**
     * Updates the key for this entry.
     *
     * @param key the new key to set (may be {@code null})
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value (may be {@code null})
     */
    public V getValue() {
        return value;
    }

    /**
     * Replaces the value corresponding to this entry with the specified value.
     *
     * @param newVal the new value to set (may be {@code null})
     * @return the previous value associated with the key (may be {@code null})
     */
    public V setValue(V newVal) {
        V oldVal = value;
        value = newVal;
        return oldVal;
    }

    /**
     * Returns the next entry in the chain.
     *
     * @return the next entry, or {@code null} if this is the last
     */
    public HashMapEntry<K, V> getNext() {
        return next;
    }

    /**
     * Sets the next entry in the chain.
     *
     * @param next the new next entry, or {@code null} if this is the last
     */
    public void setNext(HashMapEntry<K, V> next) {
        this.next = next;
    }

    /**
     * Null-safe equality check for keys and values.
     *
     * @param a first object
     * @param b second object
     * @return true if both are null or equal
     */
    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Compares this entry to the specified object.
     * Equality is based only on the key, not the value.
     *
     * @param obj the object to compare with
     * @return true if the object is a {@code HashMapEntry} with an equal key
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        HashMapEntry<?, ?> other = (HashMapEntry<?, ?>) obj;
        return eq(this.key, other.key);
    }

    /**
     * Returns the hash code for this entry's key.
     *
     * @return the key's hash code, or {@code 0} if the key is null
     */
    @Override
    public int hashCode() {
        return (key == null) ? 0 : key.hashCode();
    }

    /**
     * Returns a string representation of the form {@code {key=value}}.
     *
     * @return string representation of this entry
     */
    @Override
    public String toString() {
        return "{" + key + "=" + value + "}";
    }
}
