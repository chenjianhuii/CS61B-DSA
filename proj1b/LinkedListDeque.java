public class LinkedListDeque<T> implements Deque<T> {

    private static class Node<T> {
        private T item;
        private Node prev, next;
        Node(T data, Node p, Node n) {
            item = data;
            prev = p;
            next = n;
        }
    }

    private int size;
    private Node sentinal;

    public LinkedListDeque() {
        size = 0;
        sentinal = new Node(0, null, null);
        sentinal.next = sentinal;
        sentinal.prev = sentinal;
    }

    @Override
    public void addFirst(T item) {
        sentinal.next.prev = new Node(item, sentinal, sentinal.next);
        sentinal.next = sentinal.next.prev;
        size++;
    }

    @Override
    public void addLast(T item) {
        sentinal.prev.next = new Node(item, sentinal.prev, sentinal);
        sentinal.prev = sentinal.prev.next;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinal.next;
        while (p != sentinal) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size--;
        T ret = (T) sentinal.next.item;
        sentinal.next.item = null;
        sentinal.next = sentinal.next.next;
        sentinal.next.prev = sentinal;
        return ret;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size--;
        T ret = (T) sentinal.prev.item;
        sentinal.prev.item = null;
        sentinal.prev = sentinal.prev.prev;
        sentinal.prev.next = sentinal;
        return ret;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinal.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return (T) p.item;
    }

    private T getRecursiveHelper(int index, Node p) {
        if (index == 0) {
            return (T) p.item;
        }
        return getRecursiveHelper(index - 1, p.next);
    }
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, sentinal.next);
    }
}
