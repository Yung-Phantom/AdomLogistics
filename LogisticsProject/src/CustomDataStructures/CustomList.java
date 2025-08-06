package CustomDataStructures;

public class CustomList<E> {
    private NodeElements<E> head; //
    private NodeElements<E> tail; //
    private int size; // To keep track of the size of the list

    // constructor to initialize the head, tail and size()
    public CustomList() {
        head = null;
        tail = null;
        size();
    }

    // Insert at front of the list
    public void insertFront(E element) {
        NodeElements<E> newNode = new NodeElements<>(); // Create a new node for the element
        newNode.SinglyNodeElements(element); // Initialize the node with the provided data
        newNode.next = head; // Set the new node's next to the current head
        head = newNode; // Update the head to point to the new node
        if (tail == null) {
            tail = newNode; // If the list was empty, set tail to the new node
        }
        size = size + 1; // Increment the size of the list
    }

    // Return and remove from front of the list
    public void removeFront() {
        // check if head is empty before removing
        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size = size - 1;
        }
    }

    // Insert at back of the list
    public void insertBack(E element) {
        NodeElements<E> newNode = new NodeElements<>();// Create a new node for the element
        newNode.SinglyNodeElements(element); // Initialize the node with the provided data
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size = size + 1;
    }

    // Return and remove from back of the list
    public void removeBack() {
        if (head == null) {
            return; // If the list is empty, do nothing
        }
        if (head == tail) {
            head = tail = null;
            return;
        }
        NodeElements<E> current = head;
        while (current.next != tail) {
            current = current.next;
        }
        current.next = null;
        tail = current;
    }

    public void displayList() {
        NodeElements<E> current = head;
        System.out.println("<-- Start");
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        System.out.println("End -->");
    }
    public void clear(){
        while (head != null || tail != null) {
            removeFront();
        }
    }

    public int size() {
        return size; // Return the size of the stack
    }
}
