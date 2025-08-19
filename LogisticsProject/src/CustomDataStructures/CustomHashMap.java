package CustomDataStructures;

/**
 * A lightweight hash map implementation using separate chaining with singly
 * linked entries.
 * <p>
 * Buckets are stored in a {@code DoublyLinkedList} as a fixed-size table of
 * head pointers
 * (initialized to {@link #INITIAL_CAPACITY} null heads). Each bucket head
 * points to a
 * chain of {@code HashMapEntry<K,V>} via {@code next} references.
 * </p>
 * <p>
 * Characteristics:
 * </p>
 * <ul>
 * <li><b>Collision strategy:</b> Separate chaining (singly linked list per
 * bucket).</li>
 * <li><b>Null handling:</b> Supports {@code null} keys and values; hashing
 * treats a null key as 0.</li>
 * <li><b>Resizing:</b> No automatic resizing by load factor; capacity remains
 * constant unless
 * explicitly extended to fit an index (defensive).</li>
 * <li><b>Complexity:</b> Average expected O(1) for put/get under uniform
 * hashing; O(n) in worst-case chains.</li>
 * </ul>
 *
 * @param <K> key type
 * @param <V> value type
 */
public class CustomHashMap<K, V> {
    /**
     * Table of buckets. Each position holds the head {@code HashMapEntry<K,V>} of a
     * singly linked chain,
     * or {@code null} if the bucket is empty.
     */
    private DoublyLinkedList<HashMapEntry<K, V>> table;

    /**
     * Count of key-value mappings present in the map.
     */
    private int size;

    /**
     * Initial number of buckets created at construction.
     */
    private static final int INITIAL_CAPACITY = 16;

    // --- add this helper ---
    /**
     * Null-safe equality check used for comparing keys and values throughout the
     * map.
     * <p>
     * This ensures consistent behavior for {@code null} keys/values without
     * duplicating checks.
     * </p>
     *
     * @param a left operand
     * @param b right operand
     * @return true if both are the same reference or equal by {@code equals}, with
     *         null-safety
     */
    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
    // -----------------------

    /**
     * Constructs an empty map with {@link #INITIAL_CAPACITY} buckets, each
     * initialized to {@code null}.
     */
    public CustomHashMap() {
        table = new DoublyLinkedList<>();
        // Pre-size the table with null bucket heads to avoid index checks during normal
        // use.
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            table.insertBack(null);
        }
        size = 0;
    }

    /**
     * Computes a non-negative bucket index for the given key based on the current
     * table size.
     * <p>
     * For a {@code null} key, hash is treated as 0. The result is always in range
     * {@code [0, size)}.
     * </p>
     *
     * @param key key whose bucket index is to be computed (may be null)
     * @return bucket index within the table
     */
    private int safeIndex(K key) {
        int n = table.size();
        if (n == 0)
            return 0; // defensive
        int h = (key == null) ? 0 : key.hashCode();
        int nonNegative = h & 0x7fffffff;
        return nonNegative % n;
    }

    /**
     * Ensures the table can address the given index by appending null buckets if
     * necessary.
     * <p>
     * Note: This implementation does not rehash existing entries; it only grows the
     * underlying bucket list if the computed index exceeds current bounds
     * (defensive).
     * </p>
     *
     * @param index target index to be valid for access
     */
    private void ensureCapacity(int index) {
        while (index >= table.size()) {
            table.insertBack(null);
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     * <p>
     * If the map previously contained a mapping for the key, the old value is
     * replaced.
     * Collisions are handled via insertion at the head of the bucket chain.
     * </p>
     *
     * @param key   key with which the specified value is to be associated (may be
     *              null)
     * @param value value to be associated with the specified key (may be null)
     */
    public void put(K key, V value) {
        int index = safeIndex(key);
        ensureCapacity(index);

        // Head of the chain for this bucket.
        HashMapEntry<K, V> head = table.getElement(index);
        HashMapEntry<K, V> current = head;

        // Search for existing key to replace value in-place.
        while (current != null) {
            if (eq(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        // Key not found: insert new entry at the head for O(1) insertion.
        HashMapEntry<K, V> newEntry = new HashMapEntry<>(key, value, head);
        table.setElement(index, newEntry);
        size++;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if
     * this map contains no mapping.
     *
     * @param key key whose associated value is to be returned (may be null)
     * @return the value mapped to {@code key}, or {@code null} if not present
     */
    public V get(K key) {
        int index = safeIndex(key);
        if (index >= table.size())
            return null;

        HashMapEntry<K, V> current = table.getElement(index);
        while (current != null) {
            if (eq(current.getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested (may be null)
     * @return {@code true} if a mapping exists, otherwise {@code false}
     */
    public boolean containsKey(K key) {
        int index = safeIndex(key);
        if (index >= table.size())
            return false;

        HashMapEntry<K, V> current = table.getElement(index);
        while (current != null) {
            if (eq(current.getKey(), key)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the specified
     * value.
     * <p>
     * Runs in O(n) time as it must scan all buckets and entries.
     * </p>
     *
     * @param value value whose presence is to be tested (may be null)
     * @return {@code true} if any entry has the specified value, otherwise
     *         {@code false}
     */
    public boolean containsValue(V value) {
        for (int i = 0; i < table.size(); i++) {
            HashMapEntry<K, V> current = table.getElement(i);
            while (current != null) {
                if (eq(current.getValue(), value)) {
                    return true;
                }
                current = current.getNext();
            }
        }
        return false;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map (may be null)
     * @return {@code true} if the entry was found and removed, otherwise
     *         {@code false}
     */
    public boolean remove(K key) {
        int index = safeIndex(key);
        if (index >= table.size())
            return false;

        HashMapEntry<K, V> current = table.getElement(index);
        HashMapEntry<K, V> prev = null;

        // Find the node to remove, tracking the previous node to stitch the chain.
        while (current != null) {
            if (eq(current.getKey(), key)) {
                if (prev == null) {
                    // Removing head of chain: update bucket head.
                    table.setElement(index, current.getNext());
                } else {
                    // Bypass current node.
                    prev.setNext(current.getNext());
                }
                size--;
                return true;
            }
            prev = current;
            current = current.getNext();
        }
        return false;
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this
     * call returns.
     * <p>
     * Re-initializes the table to {@link #INITIAL_CAPACITY} with null heads.
     * </p>
     */
    public void clear() {
        table = new DoublyLinkedList<>();
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            table.insertBack(null);
        }
        size = 0;
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Prints all key-value pairs to standard output.
     * <p>
     * Iterates buckets in table order and chains within each bucket; no insertion
     * order guarantees.
     * Intended for debugging/inspection.
     * </p>
     */
    public void display() {
        for (int i = 0; i < table.size(); i++) {
            HashMapEntry<K, V> current = table.getElement(i);
            while (current != null) {
                System.out.println("Key: " + current.getKey() + ", Value: " + current.getValue());
                current = current.getNext();
            }
        }
    }
}
