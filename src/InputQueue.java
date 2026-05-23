import java.util.Random;

public class InputQueue {
    private Queue q;
    private Random rnd = new Random();
    private String symbols = "ABCDabcd~^v+>=";

    public InputQueue() {
        q = new Queue(10);
        for (int i = 0; i < 10; i++) {
            q.enqueue(generateNewElement());
        }
    }

    public char generateNewElement() {
        int chance = rnd.nextInt(1, 21);
        if (chance <= 14) return symbols.charAt(rnd.nextInt(symbols.length()));
        else if (chance <= 18) return '@';
        else return 'X';
    }

    public char dequeue() {
        Object data = q.dequeue();
        char firstElement = (data != null) ? (char) data : ' ';
        q.enqueue(generateNewElement());
        return firstElement;
    }

    // Ekran çizimi için kuyruğu dairesel sırayla char dizisine döker
    public char[] getQueue() {
        char[] currentQueue = new char[10];
        for (int i = 0; i < q.size(); i++) {
            // Yeni eklediğimiz getElement metodu sayesinde front'u bozmadan okuyoruz
            Object element = q.getElement(i);
            if (element != null) {
                currentQueue[i] = (char) element;
            } else {
                currentQueue[i] = ' '; // Güvenlik önlemi
            }
        }
        return currentQueue;
    }
}