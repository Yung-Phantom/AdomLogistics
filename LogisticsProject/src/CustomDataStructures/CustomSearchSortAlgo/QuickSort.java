package CustomDataStructures.CustomSearchSortAlgo;

import CustomDataStructures.CustomArrayList;

public class QuickSort {
    public int partitioner(CustomArrayList<String> array, int low, int high) {
        String pivot = array.getElement(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array.getElement(j).compareTo(pivot) <= 0) {
                i++;
                String temp = array.getElement(i);
                array.setElement(i, array.getElement(j));
                array.setElement(j, temp);
            }
        }
        String temp = array.getElement(i + 1);
        array.setElement(i + 1, array.getElement(high));
        array.setElement(high, temp);
        return i + 1;
    }
}
