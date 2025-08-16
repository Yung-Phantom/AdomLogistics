package CustomDataStructures;

public class SinglyLinkedList<E> {
    public NodeElements<E> head;
    public NodeElements<E> tail;
    public int size; // To keep track of the size of the list

    // constructor to initialize the head and tail and get the size
    public SinglyLinkedList() {
        head = null;
        tail = null;
        size(); // size getter
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
        if (current != null) {
            System.out.println("<-- Start");
            while (current != null) {
                System.out.println(current.data);
                current = current.next;
            }
            System.out.println("End -->");
        } else {
            System.out.println("Empty list");
        }
    }

    public void clear() {
        while (head != null || tail != null) {
            removeFront();
        }
    }

    public int size() {
        return size; // Return the size of the stack
    }

    public NodeElements<E> getNodeAt(int i) {
        if (i < 0 || i >= size) {
            return null;
        }
        NodeElements<E> current = head;
        int index = 0;
        while (index < i) {
            current = current.next;
            index++;
        }
        return current;
    }

    public void displayNodeAt(int i) {
        if (i < 0 || i >= size) {
            System.out.println("Index out of bounds");
            return;
        }
        E display = getNodeAt(i).data;
        System.out.println("Node at index " + i + ": " + display);
    }

    public NodeElements<E> getHead() {
        return getNodeAt(0);
    }

    public NodeElements<E> getTail() {
        return getNodeAt(size - 1);
    }

    public boolean isEmpty() {
        return head == null;
    }
    public E getElement(int i){
        return getNodeAt(i).data;
    }
    public int indexOf(String element) {
    for (int i = 0; i < size(); i++) {
        if (getElement(i).equals(element)) {
            return i;
        }
    }
    return -1; // return -1 if element not found
}
}
