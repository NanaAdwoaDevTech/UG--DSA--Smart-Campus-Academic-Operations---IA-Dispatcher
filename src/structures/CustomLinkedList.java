/*
 * Name: Hammond Emmanuel Adukwei
 * Student ID: 22400734
 * Assigned Component: Custom Linked List (src/structures/CustomLinkedList.java)
 * ID Derivation Rule: Custom iterator step size = Last digit of ID (4) + 1 = 5
 */

package structures;

public class CustomLinkedList<T> {

    // Verification Variable derived from Student ID: 22400734 -> 5
    private static final int ITERATOR_STEP_SIZE = 5;

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public boolean remove(T value) {
        if (head == null) {
            return false;
        }
        if (valuesEqual(head.data, value)) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
            return true;
        }
        Node<T> prev = head;
        Node<T> current = head.next;
        while (current != null) {
            if (valuesEqual(current.data, value)) {
                prev.next = current.next;
                if (current == tail) {
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException("Cannot removeFirst from an empty list");
        }
        T value = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    public boolean contains(T value) {
        Node<T> current = head;
        while (current != null) {
            if (valuesEqual(current.data, value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
        }
    }

    private boolean valuesEqual(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    // Custom iterator contract (not java.util.Iterator)
    public interface StepIterator<T> {
        boolean hasNext();
        T next();
    }

    public StepIterator<T> stepIterator() {
        return new LinkedStepIterator();
    }

    // Walks the list ITERATOR_STEP_SIZE nodes at a time (skips 5 ahead each call)
    private class LinkedStepIterator implements StepIterator<T> {
        private Node<T> current = head;

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            if (current == null) {
                throw new IllegalStateException("No more elements in step iterator");
            }
            T value = current.data;
            for (int i = 0; i < ITERATOR_STEP_SIZE && current != null; i++) {
                current = current.next;
            }
            return value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== NORMAL CASES ===");

        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.addFirst(5);
        if (list.toString().equals("[5, 10, 20, 30]")) {
            System.out.println("PASS: add/addFirst produced " + list);
            passed++;
        } else {
            System.out.println("FAIL: unexpected list contents " + list);
            failed++;
        }

        if (list.get(2) == 20) {
            System.out.println("PASS: get(2) == 20");
            passed++;
        } else {
            System.out.println("FAIL: get(2) returned " + list.get(2));
            failed++;
        }

        if (list.contains(30) && !list.contains(999)) {
            System.out.println("PASS: contains() behaves correctly");
            passed++;
        } else {
            System.out.println("FAIL: contains() behaved incorrectly");
            failed++;
        }

        boolean removed = list.remove(10);
        if (removed && !list.contains(10) && list.size() == 3) {
            System.out.println("PASS: remove(10) -> " + list);
            passed++;
        } else {
            System.out.println("FAIL: remove(10) did not behave as expected -> " + list);
            failed++;
        }

        System.out.println();
        System.out.println("=== BOUNDARY CASES ===");

        CustomLinkedList<String> single = new CustomLinkedList<>();
        single.add("only");
        if (single.size() == 1 && single.get(0).equals("only")) {
            System.out.println("PASS: single-element list handled correctly");
            passed++;
        } else {
            System.out.println("FAIL: single-element list mishandled");
            failed++;
        }
        single.removeFirst();
        if (single.isEmpty()) {
            System.out.println("PASS: removing the only element empties the list (head/tail reset)");
            passed++;
        } else {
            System.out.println("FAIL: list not empty after removing only element");
            failed++;
        }

        CustomLinkedList<Integer> stepList = new CustomLinkedList<>();
        for (int i = 0; i < 12; i++) {
            stepList.add(i);
        }
        StepIterator<Integer> it = stepList.stepIterator();
        StringBuilder collected = new StringBuilder();
        while (it.hasNext()) {
            collected.append(it.next()).append(" ");
        }
        if (collected.toString().trim().equals("0 5 10")) {
            System.out.println("PASS: stepIterator() with step size 5 yielded: " + collected.toString().trim());
            passed++;
        } else {
            System.out.println("FAIL: stepIterator() yielded unexpected sequence: " + collected.toString().trim());
            failed++;
        }

        System.out.println();
        System.out.println("=== INVALID / EMPTY CASES ===");

        CustomLinkedList<Integer> empty = new CustomLinkedList<>();

        try {
            empty.removeFirst();
            System.out.println("FAIL: removeFirst() on empty list did not throw");
            failed++;
        } catch (IllegalStateException e) {
            System.out.println("PASS: removeFirst() on empty list threw IllegalStateException as expected");
            passed++;
        }

        try {
            empty.get(0);
            System.out.println("FAIL: get(0) on empty list did not throw");
            failed++;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("PASS: get(0) on empty list threw IndexOutOfBoundsException as expected");
            passed++;
        }

        boolean removeOnEmpty = empty.remove(5);
        if (!removeOnEmpty) {
            System.out.println("PASS: remove() on empty list returns false instead of throwing");
            passed++;
        } else {
            System.out.println("FAIL: remove() on empty list should return false");
            failed++;
        }

        StepIterator<Integer> emptyIt = empty.stepIterator();
        if (!emptyIt.hasNext()) {
            System.out.println("PASS: stepIterator() on empty list has no next element");
            passed++;
        } else {
            System.out.println("FAIL: stepIterator() on empty list should have no elements");
            failed++;
        }

        System.out.println();
        System.out.println("=== SUMMARY: " + passed + " passed, " + failed + " failed ===");
    }
}
