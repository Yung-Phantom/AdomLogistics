package CustomDataStructures;

public class DoublyLinkedList<E> extends SinglyLinkedList<E> {

    @Override
    public void insertFront(E element) {
        NodeElements<E> newNode = new NodeElements<>();
        newNode.DoublyNodeElements(element); // includes prev and next
        newNode.next = head;
        if (head != null) {
            head.previous = newNode;
        } else {
            tail = newNode;
        }
        head = newNode;
        size = size + 1;
    }

    @Override
    public void removeFront() {
        if (head != null) {
            head = head.next;
            if (head != null) {
                head.previous = null; // reset backward link
            } else {
                tail = null; // list became empty
            }
            size--;
        }
    }

    @Override
    public void insertBack(E element) {
        NodeElements<E> newNode = new NodeElements<>();
        newNode.DoublyNodeElements(element);
        newNode.previous = tail;
        newNode.next = null; // new node will be the last node
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size = size + 1;
    }

    @Override
    public void removeBack() {
        if (tail != null) {
            tail = tail.previous;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null; // list became empty
            }
            size = size - 1;
        }
    }

    public void addAt(int index, E element) {
        if (index == 0) {
            insertFront(element);
            return;
        } else {
            NodeElements<E> previousNode = getNodeAt(index - 1);
            if (previousNode != null) {
                add(previousNode, element);
            } else {
                System.out.println("Index out of bounds");
            }
        }
    };

    private void add(NodeElements<E> previousNode, E element) {
        NodeElements<E> newNode = new NodeElements<>();
        newNode.DoublyNodeElements(element);
        if (previousNode == null) {
            insertFront(element);
            return;
        } else {
            NodeElements<E> nextNode = previousNode.next;
            newNode.previous = previousNode;
            newNode.next = nextNode;
            previousNode.next = newNode;
            if (nextNode != null) {
                nextNode.previous = newNode;
            }
            size = size + 1;
        }

    }

    public void removeAt(int index) {
        if (index == 0) {
            removeFront();
        } else if (index == size - 1) {
            removeBack();
        } else if(0 < index && index < size-1){
            NodeElements<E> node = getNodeAt(index);
            if (node != null) {
                remove(node);
            }
        }
        else{
            System.out.println("Index out of bounds");
        }
    }

    private void remove(NodeElements<E> n) {
        if (n != null) {
            NodeElements<E> previousNode = n.previous;
            NodeElements<E> nextNode = n.next;
            if (previousNode != null) {
                previousNode.next = nextNode;
            } else {
                head = nextNode; // n was the head
            }
            if (nextNode != null) {
                nextNode.previous = previousNode;
            } else {
                tail = previousNode; // n was the tail
            }
            n.next = null;
            n.previous = null;
            size = size - 1;
        }
    }

    public void displayReverse() {
        NodeElements<E> current = tail;
        if (current != null) {
            System.out.println("<-- End");
            while (current != null) {
                System.out.println(current.data);
                current = current.previous;
            }
            System.out.println("Start -->");
        } else {
            System.out.println("Empty list");
        }
    }
    public NodeElements<E> setElement(int i, E element) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i);
        }
        NodeElements<E> node = getNodeAt(i);
        node.data = element;
        return node;
    }
}
