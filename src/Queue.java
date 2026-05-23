public class Queue {
    private Object[] elements;
    private int front;
    private int rear;
    private int count;

    public Queue(int capacity) {
        elements = new Object[capacity];
        front = 0;
        rear = -1;
        count = 0;
    }

    public void enqueue(Object data) {
        if (!isFull()) {
            rear = (rear + 1) % elements.length;
            elements[rear] = data;
            count++;
        }
    }

    public Object dequeue() {
        if (!isEmpty()) {
            Object data = elements[front];
            front = (front + 1) % elements.length;
            count--;
            return data;
        }
        return null;
    }

    public Object peek() {
        if (!isEmpty()) {
            return elements[front];
        }
        return null;
    }

    public boolean isFull() {
        return count == elements.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public Object getElement(int index) {
        if (index >= 0 && index < count) {
            // Dairesel yapıda front'tan başlayarak index kadar ileri gidiyoruz
            return elements[(front + index) % elements.length];
        }
        return null;
    }
}