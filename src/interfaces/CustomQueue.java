package structures;

/**
 * CustomQueue.java
 * Student: Godlove Agyei Sarfo
 * Student ID: 22263864
 *
 * Circular Queue
 * Maximum Size = 864 % 100 = 64
 */
public class CustomQueue<T> {

    private static final int MAX_SIZE = 64;

    private final Object[] queue;
    private int front;
    private int rear;
    private int size;

    /**
     * Constructor
     */
    public CustomQueue() {
        queue = new Object[MAX_SIZE];
        front = 0;
        rear = -1;
        size = 0;
    }

    /**
     * Adds an element to the rear of the queue.
     *
     * @param item element to add
     * @throws IllegalStateException if queue is full
     */
    public void enqueue(T item) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full.");
        }

        rear = (rear + 1) % MAX_SIZE;
        queue[rear] = item;
        size++;
    }

    /**
     * Removes and returns the front element.
     *
     * @return removed element
     * @throws IllegalStateException if queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        T item = (T) queue[front];
        queue[front] = null;

        front = (front + 1) % MAX_SIZE;
        size--;

        if (size == 0) {
            front = 0;
            rear = -1;
        }

        return item;
    }

    /**
     * Returns the front element without removing it.
     *
     * @return front element
     * @throws IllegalStateException if queue is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        return (T) queue[front];
    }

    /**
     * Checks if queue is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks if queue is full.
     *
     * @return true if full
     */
    public boolean isFull() {
        return size == MAX_SIZE;
    }

    /**
     * Returns current number of elements.
     *
     * @return queue size
     */
    public int size() {
        return size;
    }

    /**
     * Returns queue capacity.
     *
     * @return maximum capacity
     */
    public int capacity() {
        return MAX_SIZE;
    }

    /**
     * Removes all elements from the queue.
     */
    public void clear() {
        for (int i = 0; i < MAX_SIZE; i++) {
            queue[i] = null;
        }

        front = 0;
        rear = -1;
        size = 0;
    }

    /**
     * Displays queue contents from front to rear.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        System.out.print("Queue: ");

        for (int i = 0; i < size; i++) {
            int index = (front + i) % MAX_SIZE;
            System.out.print(queue[index]);

            if (i < size - 1) {
                System.out.print(" <- ");
            }
        }

        System.out.println();
    }

    /**
     * Returns the queue as a string.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (front + i) % MAX_SIZE;
            sb.append(queue[index]);

            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
