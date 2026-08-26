/*
 * Name: David Edu Turkson
 * Student ID: 22012947
 * Assigned Component: CustomBST.java (src/structures/CustomBST.java)
 * Note: MyList.java is a supporting helper class used by CustomBST.java
 */

package structures;

/**
 * MyList.java
 * -----------------------------------------------------------------------
 * Custom resizable array (replacement for java.util.ArrayList/List) so
 * that CustomBST does not depend on any built-in Java collection
 * classes.
 *
 * Backed by a plain T[] array (via Object[] internally, since generic
 * array creation isn't allowed directly in Java) that doubles in
 * capacity whenever it fills up.
 * -----------------------------------------------------------------------
 */
public class MyList<T> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 8;

    public MyList() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /** Appends a value to the end of the list, growing the backing array if full. */
    public void add(T value) {
        if (size == data.length) {
            grow();
        }
        data[size] = value;
        size++;
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            bigger[i] = data[i];
        }
        data = bigger;
    }

    /** Returns the element at the given index. */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
        }
        return (T) data[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Simple bracketed, comma-separated representation, matching List.toString() style. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
