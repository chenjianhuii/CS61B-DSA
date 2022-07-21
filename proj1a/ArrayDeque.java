public class ArrayDeque<T> {
    private int capacity;
    private T[] items;
    private int front, rear;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        capacity = 8;
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

    public void addFirst(T item) {
        if (size() == capacity - 1) {
            reSize(capacity*2);
        }
        front = minusOne(front);
        items[front] = item;
    }

    public void addLast(T item) {
        if (size() == capacity - 1) {
            reSize(capacity*2);
        }
        items[rear] = item;
        rear = (rear + 1) % capacity;
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public int size() {
        return (rear - front + capacity) % capacity;
    }

    public void printDeque() {
        for (int i = front; i != rear ; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        T item = items[front];
        front = (front + 1) % capacity;
        if (capacity < 16 && size() / capacity < 0.25) {
            reSize(capacity/2);
        }
        return item;
    }

    public T removeLast() {
        rear = minusOne(rear);
        T item = items[rear];
        if (capacity < 16 && size() / capacity < 0.25) {
            reSize(capacity/2);
        }
        return item;
    }

    public T get(int index) {
        return items[(front+index)%capacity];
    }
}