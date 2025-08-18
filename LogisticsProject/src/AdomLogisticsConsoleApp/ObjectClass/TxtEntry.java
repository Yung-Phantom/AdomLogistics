package AdomLogisticsConsoleApp.ObjectClass;

import CustomDataStructures.CustomArrayList;
import CustomDataStructures.CustomHashMap;

/**
 * Represents a single entry from the TXT database.
 */
public class TxtEntry {
    /**
     * The entry number of this entry.
     */
    String entryNumber;

    /**
     * A mapping of field names to their corresponding values for this entry.
     */
    public CustomHashMap<String, String> fields = new CustomHashMap<>();

    /**
     * The raw lines of text that make up this entry.
     */
    public CustomArrayList<String> rawLines = new CustomArrayList<>();

    /**
     * Indicates whether this entry has been marked as deleted.
     */
    public boolean deleted;

    public TxtEntry(String entryNumber) {
        this.entryNumber = entryNumber;
        this.fields = new CustomHashMap<>();
        this.rawLines = new CustomArrayList<>();
    }
}

