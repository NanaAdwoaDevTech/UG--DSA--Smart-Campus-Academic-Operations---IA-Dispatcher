package src.structures;

/**
 * CustomBST.java
 * -----------------------------------------------------------------------
 * Custom (non-self-balancing) Binary Search Tree implementation for the
 * DCIT 204/308 Joint DSA Semester Project (Ghana Smart Service Operations
 * Optimizer).
 *
 * ID Parameter Derivation Rule applied:
 *   Sum of ID digits = 27  ->  MAX_DEPTH_THRESHOLD = 27
 * -----------------------------------------------------------------------
 */
public class CustomBST<T extends Comparable<T>> {

    /** Derived from ID digit sum (27). Used by isSkewed()/checkDepth(). */
    public static final int MAX_DEPTH_THRESHOLD = 27;

    /** Internal node type. Kept private — callers interact via the tree API. */
    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root;
    private int size;

    public CustomBST() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }
}
