package AdomLogisticsConsoleApp.ObjectClass;

import CustomDataStructures.CustomArrayList;
import CustomDataStructures.CustomHashMap;

/**
 * Represents a single structured entry parsed from a TXT-based database.
 * <p>
 * Each {@code TxtEntry} is identified by its {@link #entryNumber}, contains the
 * original raw lines from the TXT file in {@link #rawLines}, and exposes a parsed
 * mapping of field names to values via {@link #fields}.
 * </p>
 * <p>
 * An entry may optionally be marked as logically deleted, indicated by the
 * {@link #deleted} flag — for example, when the "Status" field in the raw text equals "Deleted".
 * </p>
 *
 * <h3>Usage notes:</h3>
 * <ul>
 *   <li>Field data is stored in a {@code CustomHashMap} to preserve fast key lookup.</li>
 *   <li>Raw lines are stored in a {@code CustomArrayList} to retain original file order.</li>
 *   <li>This class is a data holder; parsing logic (e.g., field extraction) is performed externally.</li>
 * </ul>
 *
 * @author Adom Logistics
 */
public class TxtEntry {

    /**
     * Unique identifier for this entry, usually extracted from the TXT database header
     * or assigned during parsing.
     */
    String entryNumber;

    /**
     * A mapping of parsed field names to their corresponding string values.
     * <p>
     * Populated during parsing. Keys are typically case-insensitive when accessed,
     * depending on consuming logic.
     * </p>
     */
    public CustomHashMap<String, String> fields = new CustomHashMap<>();

    /**
     * The unmodified raw text lines for this entry, in the order they appeared in the TXT file.
     * <p>
     * Useful for re-writing the entry in its original format or for re-parsing with different logic.
     * </p>
     */
    public CustomArrayList<String> rawLines = new CustomArrayList<>();

    /**
     * Flag indicating whether this entry has been marked as deleted.
     * <p>
     * This is usually set to {@code true} when a "Status" field with the value "Deleted"
     * is encountered during parsing.
     * </p>
     */
    public boolean deleted;

    /**
     * Creates a new {@code TxtEntry} with the given entry number.
     *
     * @param entryNumber unique identifier for this entry
     */
    public TxtEntry(String entryNumber) {
        // Assigns the entry number upon creation
        this.entryNumber = entryNumber;
    }

    public String getFieldValue(String fieldName) {
        // implement the logic to retrieve the field value based on the field name
        // for example:
        for (int i = 0; i < rawLines.size(); i++) {
            String line = rawLines.getElement(i);
            if (line.startsWith(fieldName + ":")) {
                return line.substring(line.indexOf(":") + 1).trim();
            }
        }
        return null;
    }

    public int tryGetInt(String fieldName, int defaultValue) {
        String value = getFieldValue(fieldName);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
