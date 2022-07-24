public class ArrayDeque<T> implements Deque<T> {
    private int capacity;
    private T[] items;
    private int front, rear;

    public ArrayDeque() {
        capacity = 8;
        items = (T[]) new Object[capacity];
        front = rear = 0;
    }

    private int minusOne(int index) {
        index--;
        while (index < 0) {
            index += capacity;
        }
        return index % capacity;
    }
    private void reSize(int c) {
        int oldCapacity = capacity;
        capacity = c;
        T[] newItems = (T[]) new Object[capacity];
        int j = 0;
        for (int i = front; i != rear; i = (i + 1) % oldCapacity) {
            newItems[j++] = items[i];
        }
        items = newItems;
        front = 0;
        rear = j;
    }

    @Override
    public void addFirst(T item) {
        if (size() == capacity - 1) {
            reSize(capacity * 2);
        }
        front = minusOne(front);
        items[front] = item;
    }

    @Override
    public void addLast(T item) {
        if (size() == capacity - 1) {
            reSize(capacity * 2);
        }
        items[rear] = item;
        rear = (rear + 1) % capacity;
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }

    @Override
    public int size() {
        return (rear - front + capacity) % capacity;
    }

    @Override
    public void printDeque() {
        for (int i = front; i != rear; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = items[front];
        front = (front + 1) % capacity;
        if (capacity >= 16 && (double) size() / capacity < 0.25) {
            reSize(capacity / 2);
        }
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        rear = minusOne(rear);
        T item = items[rear];
        if (capacity >= 16 && (double) size() / capacity < 0.25) {
            reSize(capacity / 2);
        }
        return item;
    }

    @Override
    public T get(int index) {
        return items[(front + index) % capacity];
    }
}
