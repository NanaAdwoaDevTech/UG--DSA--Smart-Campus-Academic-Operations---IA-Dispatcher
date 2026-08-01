package src.structures;

import java.util.ArrayList;
import java.util.List;

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

    // ---------------------------------------------------------------
    // INSERT
    // ---------------------------------------------------------------

    /**
     * Inserts a value into the BST. Duplicates are rejected (returns false)
     * rather than silently ignored, so callers/tests can detect them -
     * this doubles as the "duplicate keys" edge case required by Section 10.
     */
    public boolean insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null value into CustomBST.");
        }
        if (root == null) {
            root = new Node<>(value);
            size++;
            return true;
        }
        Node<T> current = root;
        while (true) {
            int cmp = value.compareTo(current.value);
            if (cmp == 0) {
                return false; // duplicate — reject
            } else if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    // ---------------------------------------------------------------
    // SEARCH  (returns whether found, and can also expose the path)
    // ---------------------------------------------------------------

    /** Standard search: returns true if value exists in the tree. */
    public boolean search(T value) {
        return contains(value);
    }

    /**
     * Trace-friendly search: returns the ordered list of values visited on
     * the path from the root to the target (inclusive if found, otherwise
     * the path ends at the last node checked before falling off the tree).
     * This is the "search path" evidence Section 6 asks for.
     */
    public List<T> searchPath(T value) {
        List<T> path = new ArrayList<>();
        if (value == null) {
            return path;
        }
        Node<T> current = root;
        while (current != null) {
            path.add(current.value);
            int cmp = value.compareTo(current.value);
            if (cmp == 0) {
                break;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return path;
    }

    /** Simple boolean containment check (no path tracking, cheaper). */
    public boolean contains(T value) {
        if (value == null) return false;
        Node<T> current = root;
        while (current != null) {
            int cmp = value.compareTo(current.value);
            if (cmp == 0) return true;
            current = (cmp < 0) ? current.left : current.right;
        }
        return false;
    }

    // ---------------------------------------------------------------
    // INORDER TRAVERSAL  (sorted output evidence)
    // ---------------------------------------------------------------

    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    // ---------------------------------------------------------------
    // HEIGHT / DEPTH + ID-DERIVED THRESHOLD CHECK
    // ---------------------------------------------------------------

    /** Height of the tree (empty tree = -1, single node = 0). */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    /**
     * Checks the current tree height against MAX_DEPTH_THRESHOLD (27,
     * derived from ID digit sum). Returns true if the tree is still
     * within the healthy-depth bound.
     */
    public boolean checkDepthWithinThreshold() {
        return height() <= MAX_DEPTH_THRESHOLD;
    }

    /**
     * Flags a degenerate/skewed tree: true when height exceeds the
     * ID-derived threshold. Intended as evidence when comparing this
     * unbalanced BST against the balanced-tree / B-tree structures
     * required elsewhere in Section 6.
     */
    public boolean isSkewed() {
        return height() > MAX_DEPTH_THRESHOLD;
    }

    // ---------------------------------------------------------------
    // DEMO / TRACE OUTPUT (for report evidence — not required at runtime)
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        CustomBST<Integer> bst = new CustomBST<>();

        // Normal case
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10};
        for (int v : values) {
            bst.insert(v);
        }

        System.out.println("Inserted: " + java.util.Arrays.toString(values));
        System.out.println("Inorder (sorted) traversal: " + bst.inorderTraversal());
        System.out.println("Size: " + bst.size());
        System.out.println("Height: " + bst.height());
        System.out.println("MAX_DEPTH_THRESHOLD (from ID digit sum): " + MAX_DEPTH_THRESHOLD);
        System.out.println("Within depth threshold? " + bst.checkDepthWithinThreshold());

        // Search path evidence
        int target = 60;
        System.out.println("\nSearch path for " + target + ": " + bst.searchPath(target));
        System.out.println("Found " + target + "? " + bst.contains(target));

        int missing = 999;
        System.out.println("Search path for missing value " + missing + ": " + bst.searchPath(missing));
        System.out.println("Found " + missing + "? " + bst.contains(missing));

        // Boundary case: empty tree
        CustomBST<Integer> emptyTree = new CustomBST<>();
        System.out.println("\nEmpty tree height: " + emptyTree.height());
        System.out.println("Empty tree isEmpty(): " + emptyTree.isEmpty());
        System.out.println("Empty tree inorder: " + emptyTree.inorderTraversal());

        // Invalid input case: duplicate insert
        boolean dupResult = bst.insert(50);
        System.out.println("\nInserting duplicate 50 -> accepted? " + dupResult);

        // Skewed tree demo: sequential inserts to show degeneration and
        // exercise the ID-derived threshold check
        CustomBST<Integer> skewed = new CustomBST<>();
        for (int i = 1; i <= 30; i++) {
            skewed.insert(i);
        }
        System.out.println("\nSkewed tree (sequential 1..30) height: " + skewed.height());
        System.out.println("Skewed tree isSkewed() [threshold=" + MAX_DEPTH_THRESHOLD + "]: " + skewed.isSkewed());
    }
}
