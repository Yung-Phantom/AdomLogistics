package CustomDataStructures;

public class CustomQueue<E> {
    private CustomArrayList<E> queueList; // Using CustomArrayList to hold queue elements

    public CustomQueue() {
        queueList = new CustomArrayList<>(); // Initialize the CustomArrayList
    }

    public E enqueue(E element) {
        addToQueue(element); // Add the element to the end of the queue
        return element; // Add the element to the end of the queue
    }

    private void addToQueue(E element) {
        queueList.addElement(element);
    }

    public synchronized E dequeue() {
        checkIfEmpty();
        E element = queueList.removeElement(0);
        return element;
    }

    public E peek() {
        checkIfEmpty();
        E element = queueList.getElement(0);
        return element;
    }

    private void checkIfEmpty() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
    }

    public boolean isEmpty() {
        boolean emptyOrNot = (queueList.size() == 0); // Check if the queue is empty
        return emptyOrNot;
    }

    public int size() {
        return queueList.size();
    }

    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }
}
