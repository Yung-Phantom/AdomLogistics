# Implemented Data Structures

1. Tree (Binary Search Tree / AVL Tree) – Organize vehicles by mileage or type for efficient sorted traversal and range queries.

2. Hash Table (Manual HashMap) – Store and retrieve vehicles by registration number or type with constant-time access.

3. Stack – Manage available drivers in LIFO order, ideal for assigning the most recently available driver.

4. Queue – Assign drivers or process deliveries in FIFO order, simulating real-world dispatching.

5. LinkedList – Track delivery records with dynamic insertion/removal for rerouting and status updates.

6. Min-Heap / Priority Queue – Schedule vehicle maintenance by urgency (based on mileage or last service date), ensuring high-priority servicing.

7. Nested Map (Map<String, Map<String, Object>>) – Store detailed maintenance records per vehicle, including parts replaced and associated costs.

8. Array / List – Hold collections of vehicles, drivers, or deliveries for sorting and batch operations.

9. Binary Search – Quickly locate vehicles by registration number in a sorted array or list.

10. Insertion Sort – Sort small datasets like driver names manually with stable performance.

11. Merge Sort – Efficiently sort vehicles by mileage with guaranteed O(n log n) performance.

12. Quick Sort – Sort fuel usage or delivery ETA with fast average-case performance and in-place partitioning.

## Stack usage

To use stack, declare a new object
        CustomStack<E> stackname = new CustomStack<>(); and add the import (import CustomDataStructures.CustomStack;)
You can now call the elements using their method definitions
        stackname.clear(); - pop out all elements.
        stackname.isEmpty(); - boolean to check if stack is empty.
        stackname.peek(); - check the last element.
        stackname.pop(); - remove the last element.
        stackname.push(element); - add new element.
        stackname.size(); - check the stack size.

## Singly linked list usage

To use singly linked list, declare a new object
        SinglyLinkedList<E> singlyLinkedList = new SinglyLinkedList<>(); and add the import (import CustomDataStructures.SinglyLinkedList;)
You can now call the elements using their method definitions
        singlyLinkedList.insertFront(element); - insert element in front.
        singlyLinkedList.insertBack(element); - insert element at the back.
        singlyLinkedList.removeFront(); - remove element from the front.
        singlyLinkedList.removeBack(); - remove element from the back.
        singlyLinkedList.displayList(); - display all elements in the list.
        singlyLinkedList.size(); - display the size of the list.
        singlyLinkedList.clear(); - clear the list from the front.
        singlyLinkedList.getNodeAt(i) - get a node at i.
        singlyLinkedList.displayNodeAt(i) - display the node at i

## Doubly linked list usage

To use doubly linked list which extends SinglyLinkedList, declare a new object
        DoublyLinkedList<E> doublyLinkedList = new DoublyLinkedList<>(); and add the import (import CustomDataStructures.DoublyLinkedList;)
You can now call the elements using their method definitions
        doublyLinkedList.insertFront(element); - insert element in front.
        doublyLinkedList.insertBack(element); - insert element at the back.
        doublyLinkedList.removeFront(); - remove element from the front.
        doublyLinkedList.removeBack(); - remove element from the back.
        doublyLinkedList.displayList(); - display all elements in the list.
        doublyLinkedList.size(); - display the size of the list.
        doublyLinkedList.clear(); - clear the list from the front.
        doublyLinkedList.getNodeAt(i) - get a node at i.
        doublyLinkedList.displayNodeAt(i) - display the node at i
        doublyLinkedList.addAt(index, element) - add element after node index.
        doublyLinkedList.removeAt(index) - remove node at index.

Queue, hash table , tree , priority queue, nested map
