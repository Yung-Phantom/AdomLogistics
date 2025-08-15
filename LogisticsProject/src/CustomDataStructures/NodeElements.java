package CustomDataStructures;

public class NodeElements<E> {
    public E data;
    public NodeElements<E> next;
    NodeElements<E> previous;

    public void SinglyNodeElements(E data) {
        this.data = data; // Initialize the node with the provided data
        this.next = null; // Set next to null initially
    }
    public void DoublyNodeElements(E data) {
        this.data = data; // Initialize the node with the provided data
        this.next = null; // Set next to null initially
        this.previous = null; // Set previous to null initially
    }
}
