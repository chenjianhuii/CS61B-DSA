package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>  {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = last = fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        } else {
            rb[last] = x;
            last = (last + 1) % capacity;
            fillCount++;
        }

    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            T item = rb[first];
            first = (first + 1) % capacity;
            fillCount--;
            return item;
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            T item = rb[first];
            return item;
        }
    }

    private class ARBIterator<T> implements Iterator<T> {

        private int cur;
        private boolean flag;

        ARBIterator() {
            cur = first;
            if (isFull()) {
                flag = true;
            }
        }

        @Override
        public boolean hasNext() {
            return flag || cur != last;
        }

        @Override
        public T next() {
            T item = (T) rb[cur];
            cur = (cur + 1) % capacity;
            if (cur == last) {
                flag = false;
            }
            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ARBIterator<>();
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
