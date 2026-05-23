public class DoubleLinkedList {
    private Node head;
    private Node tail;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void insertSorted(Player newPlayer) {
        Node newNode = new Node(newPlayer);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else if (newPlayer.score >= head.getPlayer().score) {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else {
            Node current = head;
            while (current.getNext() != null && current.getNext().getPlayer().score > newPlayer.score) {
                current = current.getNext();
            }

            newNode.setNext(current.getNext());
            if (current.getNext() != null) {
                current.getNext().setPrev(newNode);
            } else {
                tail = newNode;
            }
            current.setNext(newNode);
            newNode.setPrev(current);
        }
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }
}