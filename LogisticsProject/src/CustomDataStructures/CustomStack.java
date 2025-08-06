package CustomDataStructures;

public class CustomStack<E> {
    private CustomArrayList<E> stackList; // Using CustomArrayList to hold stack elements

    public CustomStack() {
        stackList = new CustomArrayList<>(); // Initialize the CustomArrayList
    }

    // method to push to the top of the stack
    public E push(E item) {
        addToPush(item); // Add the item to the stack
        return item; // Return the item that was pushed
    }

    // method to pop from the top of the stack
    public synchronized E pop() {
        checkIfEmpty(); // Check if the stack is empty before popping
        int removeIndex = stackList.size() - 1; // Get the index of the last element
        E object = stackList.removeElement(removeIndex); // Remove the last element from the stack
        return object; // Return the removed element

    }

    // method to peek at the top of the stack without removing it
    public E peek() {
        checkIfEmpty(); // Check if the stack is empty before peeking
        int stackGet = stackList.size() - 1; // Get the index of the last element
        E object = stackList.getElement(stackGet); // Get the last element without removing it
        return object; // Return the last element
    }

    // method to check if the stack is empty
    public boolean isEmpty() {
        boolean emptyOrNot = (stackList.size() == 0); // Check if the stack is empty
        return emptyOrNot; // Return true if the stack is empty, false otherwise
    }

    // method to clear the stack
    public int size() {
        int size = stackList.size(); // Get the current size of the stack
        return size; // Return the size of the stack
    }

    // Private methods to handle stack operations
    private void addToPush(E object) {
        stackList.addElement(object); // Add the object to the stack
    }

    // Method to clear the stack
    public void clear() {
        // Clear the stack by popping all elements
        while (!isEmpty()) {
            pop(); // Pop elements until the stack is empty
        }
    }

    // Private method to check if the stack is empty before popping or peeking
    private void checkIfEmpty() {
        // Check if the stack is empty and throw an exception if it is
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty"); // Throw an exception if the stack is empty
        }
    }
}
