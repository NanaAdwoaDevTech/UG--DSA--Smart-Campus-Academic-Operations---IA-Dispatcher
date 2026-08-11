package structures;
/*
 * Name: Donkoh Kwasi Kyei
 * Student ID: 22302834
 * Assigned Component: CustomArrayList (dynamic array data structure)
 * ID Derivation Rule: Initial array capacity = Sum of ID digits
 *                      (2+2+3+0+2+8+3+4) = 24
 */
public class CustomArrayList<T> {
 
    // Verification Variable derived from Student ID: 22302834 -> 24
    private static final int INITIAL_CAPACITY = 24;
 
    private Object[] data;
    private int size;
 
    /**
     * Builds the list with the ID-derived starting capacity.
     */
    public CustomArrayList() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }
 
    /**
     * Optional constructor for callers who want a different starting
     * capacity than the ID-derived default (still grows the same way).
     */
    public CustomArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative: " + capacity);
        }
        data = new Object[capacity == 0 ? 1 : capacity];
        size = 0;
    }
 
    /** Number of elements currently stored. */
    public int size() {
        return size;
    }
 
    /** True if the list has no elements. */
    public boolean isEmpty() {
        return size == 0;
    }
 
    /** Current backing-array capacity (not the element count). */
    public int capacity() {
        return data.length;
    }
 
    /**
     * Appends an element to the end of the list.
     * Amortized O(1) — occasionally triggers an O(n) resize.
     */
    public void add(T element) {
        ensureCapacity(size + 1);
        data[size] = element;
        size++;
    }
 
    /**
     * Inserts an element at a specific index, shifting everything
     * after it one slot to the right. O(n).
     */
    public void add(int index, T element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }
 
    /** Returns the element at the given index. O(1). */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }
 
    /** Overwrites the element at the given index, returning the old value. */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) data[index];
        data[index] = element;
        return old;
    }
 
    /**
     * Removes and returns the element at the given index, shifting
     * later elements left to fill the gap. O(n).
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        size--;
        data[size] = null; // avoid memory leak / stale reference
        return removed;
    }
 
    /**
     * Removes the first occurrence of the given element, if present.
     * Returns true if something was removed.
     */
    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx == -1) {
            return false;
        }
        remove(idx);
        return true;
    }
 
    /** Returns the index of the first occurrence, or -1 if not found. O(n). */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? data[i] == null : element.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }
 
    /** True if the list contains the given element. O(n). */
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }
 
    /** Removes every element but keeps the currently allocated capacity. */
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }
 
    /** Convenience: like remove(0), but throws a clearer exception when empty. */
    @SuppressWarnings("unchecked")
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("CustomArrayList is empty");
        }
        return remove(0);
    }
 
    /** Convenience: like remove(size - 1), but throws a clearer exception when empty. */
    @SuppressWarnings("unchecked")
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("CustomArrayList is empty");
        }
        return remove(size - 1);
    }
 
    // ---------------------------------------------------------------
    // Internal helpers
    // ---------------------------------------------------------------
 
    /**
     * Grows the backing array (doubling it) if the requested minimum
     * capacity would exceed the current one.
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) {
            return;
        }
        int newCapacity = Math.max(data.length * 2, minCapacity);
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
 
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + size);
        }
    }
 
    /** add(index, element) allows index == size (append at the end). */
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + size);
        }
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
 
    // ---------------------------------------------------------------
    // Local test runner (run this file directly to sanity-check it)
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        CustomArrayList<String> list = new CustomArrayList<>();
        System.out.println("Initial capacity: " + list.capacity()); // 24
 
        list.add("Accra");
        list.add("Kumasi");
        list.add("Tamale");
        list.add(1, "Takoradi"); // insert in the middle
        System.out.println("After adds: " + list);          // [Accra, Takoradi, Kumasi, Tamale]
        System.out.println("size() = " + list.size());       // 4
 
        System.out.println("get(2) = " + list.get(2));       // Kumasi
        list.set(2, "Ho");
        System.out.println("After set(2, Ho): " + list);     // [Accra, Takoradi, Ho, Tamale]
 
        System.out.println("indexOf(\"Tamale\") = " + list.indexOf("Tamale")); // 3
        System.out.println("contains(\"Cape Coast\") = " + list.contains("Cape Coast")); // false
 
        list.remove("Ho");
        System.out.println("After remove(\"Ho\"): " + list); // [Accra, Takoradi, Tamale]
 
        String removed = list.remove(0);
        System.out.println("Removed index 0 -> " + removed + ", list now: " + list);
 
        // Force a resize past the ID-derived initial capacity to prove growth works.
        CustomArrayList<Integer> growTest = new CustomArrayList<>();
        for (int i = 0; i < 30; i++) {
            growTest.add(i);
        }
        System.out.println("Grew to size " + growTest.size() + ", capacity " + growTest.capacity());
 
        System.out.println("All checks completed.");
    }
}
 
