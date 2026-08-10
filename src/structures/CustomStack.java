/*
 * Name: Adjei Asaph Adjetey
 * Student ID: 22242385
 * Assigned Component: CustomStack.java (src/structures/CustomStack.java)
 */

package structures;

/**
 * CustomStack
 *
 * A stack implemented from scratch using a raw primitive array (no
 * java.util.ArrayList, LinkedList, HashMap, PriorityQueue, Vector, or
 * java.util.Stack anywhere in the core logic).
 *
 * MAX_DEPTH represents the "Undo/Audit log stack depth" — i.e. the
 * maximum number of undo/audit entries this stack is allowed to hold
 * before it is considered full. Once MAX_DEPTH is reached, push()
 * throws an exception instead of silently growing forever, since an
 * audit log with an unbounded depth defeats the point of having a
 * bounded undo history.
 */
public class CustomStack {

    // Verification Variable derived from Student ID: 22242385 -> 85
    private static final int MAX_DEPTH = 85;

    private int[] data;
    private int top; // index of the top element; -1 means empty

    public CustomStack() {
        this.data = new int[MAX_DEPTH];
        this.top = -1;
    }

    /** Pushes a value onto the stack. Throws if the stack is at MAX_DEPTH. */
    public void push(int value) {
        if (isFull()) {
            throw new IllegalStateException(
                "Stack overflow: cannot push beyond max audit depth of " + MAX_DEPTH);
        }
        data[++top] = value;
    }

    /** Pops and returns the top value. Throws if the stack is empty. */
    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow: cannot pop an empty stack");
        }
        return data[top--];
    }

    /** Returns the top value without removing it. Throws if the stack is empty. */
    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek an empty stack");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == MAX_DEPTH - 1;
    }

    public int size() {
        return top + 1;
    }

    public int capacity() {
        return MAX_DEPTH;
    }

    /**
     * Executable test class / main() proving normal, boundary, and
     * invalid/empty-case behavior, as required by Step 2.3 and the
     * Step 3 pre-PR checklist.
     */
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        // ---- Normal cases ----
        CustomStack stack = new CustomStack();
        stack.push(10);
        stack.push(20);
        stack.push(30);

        if (stack.peek() == 30) {
            System.out.println("[PASS] peek() returns last pushed value (30)");
            passed++;
        } else {
            System.out.println("[FAIL] peek() expected 30, got " + stack.peek());
            failed++;
        }

        if (stack.size() == 3) {
            System.out.println("[PASS] size() reports 3 after 3 pushes");
            passed++;
        } else {
            System.out.println("[FAIL] size() expected 3, got " + stack.size());
            failed++;
        }

        int popped = stack.pop();
        if (popped == 30 && stack.size() == 2) {
            System.out.println("[PASS] pop() removes and returns 30, size drops to 2");
            passed++;
        } else {
            System.out.println("[FAIL] pop() normal case failed");
            failed++;
        }

        // ---- Boundary cases ----
        // Single-element structure
        CustomStack single = new CustomStack();
        single.push(99);
        if (!single.isEmpty() && single.peek() == 99 && single.size() == 1) {
            System.out.println("[PASS] single-element stack behaves correctly");
            passed++;
        } else {
            System.out.println("[FAIL] single-element stack case failed");
            failed++;
        }
        single.pop();
        if (single.isEmpty()) {
            System.out.println("[PASS] stack correctly reports empty after popping only element");
            passed++;
        } else {
            System.out.println("[FAIL] stack should be empty after popping only element");
            failed++;
        }

        // Structure at capacity (fill to MAX_DEPTH = 85)
        CustomStack full = new CustomStack();
        for (int i = 0; i < full.capacity(); i++) {
            full.push(i);
        }
        if (full.isFull() && full.size() == 85) {
            System.out.println("[PASS] stack correctly reports full at MAX_DEPTH = 85");
            passed++;
        } else {
            System.out.println("[FAIL] stack should be full at size 85");
            failed++;
        }

        // ---- Invalid / empty input cases ----
        boolean overflowCaught = false;
        try {
            full.push(999); // should throw, stack is already at MAX_DEPTH
        } catch (IllegalStateException e) {
            overflowCaught = true;
        }
        if (overflowCaught) {
            System.out.println("[PASS] pushing beyond MAX_DEPTH correctly throws overflow exception");
            passed++;
        } else {
            System.out.println("[FAIL] expected overflow exception was not thrown");
            failed++;
        }

        boolean underflowCaught = false;
        CustomStack empty = new CustomStack();
        try {
            empty.pop(); // should throw, stack is empty
        } catch (IllegalStateException e) {
            underflowCaught = true;
        }
        if (underflowCaught) {
            System.out.println("[PASS] popping an empty stack correctly throws underflow exception");
            passed++;
        } else {
            System.out.println("[FAIL] expected underflow exception was not thrown");
            failed++;
        }

        boolean peekEmptyCaught = false;
        try {
            empty.peek(); // should throw, stack is empty
        } catch (IllegalStateException e) {
            peekEmptyCaught = true;
        }
        if (peekEmptyCaught) {
            System.out.println("[PASS] peeking an empty stack correctly throws exception");
            passed++;
        } else {
            System.out.println("[FAIL] expected exception on peek of empty stack was not thrown");
            failed++;
        }

        System.out.println();
        System.out.println("=== Test Summary: " + passed + " passed, " + failed + " failed ===");
    }
}
