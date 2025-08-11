package CustomDataStructures;

public class CustomArrayList<T> {
    private T[] ArrayInput; // Array to hold the elements
    private int size; // Current size of the array

    @SuppressWarnings("unchecked")
    public CustomArrayList() {
        ArrayInput = (T[]) new Object[1]; // Initialize with a default size of 1
        size = 0; // Start with size 0
    }

    public int size(){
        return size; // Return the current size of the array
    }

    // Method to add an element to the array
    public void addElement(T value) {
        if (size == ArrayInput.length) {
            resizeArray(ArrayInput.length + 1); // Resize the array if it's full. add 1 to the length
        }
        ArrayInput[size++] = value; // add the new value to the new slot created
    }

    // Method to get an element at a specific index
    public T getElement(int index) {
        checkIndex(index); // Check if the index is valid
        return ArrayInput[index]; // Return the element at the specified index
    }

    // Method to set an element at a specific index
    public void setElement(int index, T value) {
        checkIndex(index); // Check if the index is valid
        ArrayInput[index] = value; // Set the value at the specified index
    }

    // Method to remove an element at a specific index
    public T removeElement(int index) {
        checkIndex(index); // Check if the index is valid
        T removedElement = ArrayInput[index]; // Store the element to be removed
        for (int i = index; i < size - 1; i++) {
            ArrayInput[i] = ArrayInput[i + 1]; // Shift elements to the left
        }
        ArrayInput[size - 1] = null; // Clear the last element
        size--;
        resizeArray(size); // Resize the array to the new size
        return removedElement; // Return the removed element
    }

    // Method to check validity of the index
    private void checkIndex(int index) {
        // Check if the index is within the bounds of the array
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index); // Throw an exception if the index is invalid
        }
    }

    // Method to resize the array
    @SuppressWarnings("unchecked")
    private void resizeArray(int i) {
        T[] newDataSpace = (T[]) new Object[i]; // Create a new array with the new size
        System.arraycopy(ArrayInput, 0, newDataSpace, 0, size); // Copy the old elements to the new array
        ArrayInput = newDataSpace; // Update the reference to the new array
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        ArrayInput = (T[]) new Object[1]; // Reset the array to a new array of size 1
        size = 0; // Reset the size to 0
    }
}
