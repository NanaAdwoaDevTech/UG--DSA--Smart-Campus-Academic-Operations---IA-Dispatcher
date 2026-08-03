/*
 * Name: Jeff Akubea
 * Student ID: 22411677
 * Assigned Component: CustomHashTable
 * ID Derivation Rule: First prime > last 3 digits (677) = 683
 */
public class CustomHashTable<K, V> {

    // Verification Variable derived from Student ID: 22411677 -> 683
    private static final int INITIAL_CAPACITY = 683;

    // Simple node class for chaining
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node<K, V>[] table;
    private int count;

    @SuppressWarnings("unchecked")
    public CustomHashTable() {
        table = new Node[INITIAL_CAPACITY];
        count = 0;
    }

    // Hash function to find the index
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    // Insert or update a key-value pair
    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        int index = hash(key);
        Node<K, V> head = table[index];

        // Check if key already exists, then update
        Node<K, V> current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Otherwise, insert new node at the front of the bucket list
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = head;
        table[index] = newNode;
        count++;
    }

    // Get value by key
    public V get(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    // Remove a key-value pair
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                count--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    // Check if key exists
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Get number of elements
    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    // Main method for testing
    public static void main(String[] args) {
        CustomHashTable<String, Integer> map = new CustomHashTable<>();

        // Test normal inputs with UG context keys
        map.put("LOC001", 100);
        map.put("LOC002", 200);
        System.out.println("Get LOC001 (Expected 100): " + map.get("LOC001"));
        System.out.println("Get LOC002 (Expected 200): " + map.get("LOC002"));

        // Test boundary / updates
        map.put("LOC001", 150);
        System.out.println("Get updated LOC001 (Expected 150): " + map.get("LOC001"));
        System.out.println("Size (Expected 2): " + map.size());

        // Test invalid / empty inputs
        System.out.println("Contains 'LOC999' (Expected false): " + map.containsKey("LOC999"));
        System.out.println("Remove missing key (Expected null): " + map.remove("LOC999"));

        map.remove("LOC002");
        System.out.println("Size after removal (Expected 1): " + map.size());
    }
}