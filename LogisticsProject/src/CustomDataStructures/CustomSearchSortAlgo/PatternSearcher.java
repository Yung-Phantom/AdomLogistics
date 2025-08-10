package CustomDataStructures.CustomSearchSortAlgo;

import CustomDataStructures.CustomArrayList;

public class PatternSearcher {

    String input;
    String pattern;

    public boolean patternSearcher(String input, CustomArrayList<String> pattern) {
        this.input = input;
        for (int i = 0; i < pattern.size(); i++) {
            this.pattern = pattern.getElement(i);

            if (input.matches(pattern.getElement(i))) {
                pattern.getElement(i);
                return true;
            } else {
                System.out.println(input + " not found");

            }
        }
        return false;
    }
}
