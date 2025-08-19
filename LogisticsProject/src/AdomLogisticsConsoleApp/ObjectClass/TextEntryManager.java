package AdomLogisticsConsoleApp.ObjectClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import CustomDataStructures.CustomArrayList;

public class TextEntryManager {

    private final File pathToDir;
    private final File pathToFile;
    public String lineStarter;

    public CustomArrayList<TxtEntry> entries;

    public TextEntryManager(String pathToDir, String pathToFile, String lineStarter) {
        this.pathToDir = new File(pathToDir);
        this.pathToFile = new File(pathToFile);
        this.lineStarter = lineStarter; // <-- ensure stored
    }

    /**
     * Reads all entries from the text file and parses them into
     * {@link TxtEntry} objects.
     * 
     * @param lineStarter the prefix to use for identifying entry lines
     * @return a list of all entries in the file
     * @throws IOException if an I/O error occurs
     */
    public CustomArrayList<TxtEntry> readAllEntries(String lineStarter) throws IOException {
        entries = new CustomArrayList<>();
        if (!pathToFile.exists()) {
            System.out.println("File Does not exist. Please check the directory entered.");
            return entries;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            TxtEntry current = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(lineStarter)) {
                    // Found the start of a new entry
                    int lineStarterLength = lineStarter.length();
                    current = new TxtEntry(line.substring(lineStarterLength).trim());
                    entries.addElement(current);
                    current.rawLines.addElement(line);
                } else if (current != null) {
                    // Add the line to the current entry
                    current.rawLines.addElement(line);
                    if ("----------".equals(line.trim()) || line.isBlank()) {
                        // End of block, parse fields
                        parseFields(current);
                        current = null;
                    }
                }
            }
            // Parse trailing block if file ended without delimiter/blank
            if (current != null) {
                parseFields(current);
            }
        }
        return entries;
    }

    /**
     * Parses the raw lines of a TxtEntry and populates its field map.
     * <p>
     * Each line is expected to follow the format: {@code Key: Value}. Lines that do
     * not
     * contain a colon, or have it as the first character (empty key), are ignored.
     * </p>
     * <p>
     * Leading and trailing whitespace around keys and values is trimmed before
     * storage.
     * If a key named "Status" (case-insensitive) has the value "Deleted"
     * (case-insensitive),
     * the {@code deleted} flag on the entry is set to {@code true}.
     * </p>
     *
     * @param entry the TxtEntry whose raw lines will be parsed into key-value pairs
     * @throws NullPointerException if {@code entry}, {@code entry.rawLines},
     *  or {@code entry.fields} is {@code null}
     */
    private void parseFields(TxtEntry entry) {
        for (int i = 0; i < entry.rawLines.size(); i++) {
            String iterable_element = entry.rawLines.getElement(i);
            int index = iterable_element.indexOf(':');

            if (index > 0) {
                String key = iterable_element.substring(0, index).trim();
                String value = iterable_element.substring(index + 1).trim();
                entry.fields.put(key, value);

                if ("Status".equalsIgnoreCase(key) && "Deleted".equalsIgnoreCase(value)) {
                    entry.deleted = true;
                }
            }
        }
    }

    /**
     * Write a single entry to the file.
     * <p>
     * Appends the entry to the end of the file. If the file is empty, writes the
     * entry.
     * </p>
     * <p>
     * If the last line of the entry is not a delimiter, appends a delimiter to the
     * end.
     * </p>
     *
     * @param entry the entry to write
     * @throws IOException if any IO error occurs
     */
    public void writeEntry(TxtEntry entry) throws IOException {
        if (entry.rawLines != null && entry.rawLines.size() > 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile, true))) {

                // If the file is not empty, append a newline
                if (pathToFile.length() > 0) {
                    writer.newLine();
                }

                // Write all lines of the entry
                for (int i = 0; i < entry.rawLines.size(); i++) {
                    writer.write(entry.rawLines.getElement(i));
                    writer.newLine();
                }

                // If the last line of the entry is not a delimiter, add one
                if (!"----------".equals(entry.rawLines.getElement(entry.rawLines.size() - 1).trim())) {
                    writer.write("----------");
                    writer.newLine();
                }
            }
        } else {
            System.out.println("Error: Entry has no lines to write.");
        }
    }

    /**
     * Reads all lines from the file and returns them as a list of strings.
     * <p>
     * This method is useful for reading the entire file into memory, such as when
     * making a backup or when performing operations that require random access to
     * the data.
     * </p>
     *
     * @return a list of all lines in the file
     * @throws IOException if an I/O error occurs
     */
    public CustomArrayList<String> readAllLines() throws IOException {
        CustomArrayList<String> allLines = new CustomArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.addElement(line);
            }
        }
        return allLines;
    }

    /**
     * Writes all lines from a list to the file.
     * <p>
     * This method is useful for writing the contents of a list of strings to a file.
     * It is used by the {@link #writeAllEntries(CustomArrayList)} method to write all
     * entries to the file.
     * </p>
     *
     * @param allLines the list of lines to write to the file
     * @throws IOException if an I/O error occurs
     */
    private void writeAllLines(CustomArrayList<String> allLines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
            for (int i = 0; i < allLines.size(); i++) {
                if (i < allLines.size()) { // Add this check
                    writer.write(allLines.getElement(i) + "\n");
                }
            }
                    // Write the line to the file
        }
    }

    /**
     * Marks an entry in the file as deleted.
     * <p>
     * This method works by reading all lines from the file into memory, updating
     * the entry with the given ID to have a status of "Deleted", and then writing
     * all lines back to the file.
     * </p>
     *
     * @param entryId the ID of the entry to mark deleted
     * @param deletor the string to use to indicate the entry is deleted
     * @return true if the entry was found and marked deleted, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public boolean markDeleted(String entryId, String deletor) throws IOException {
        CustomArrayList<String> allLines = readAllLines();
        boolean updated = false;
        // Iterate over all lines in the file
        boolean inBlock = false;
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.getElement(i);
            // Check if the line is the start of an entry
            if (line.equals(lineStarter + entryId)) {
                inBlock = true;
            }
            // Check if the line is the status line of the entry
            if (inBlock && line.startsWith(deletor)) {
                // Mark the entry as deleted
                allLines.setElement(i, deletor + ": Deleted");
                updated = true;
                inBlock = false;
            }
        }
        // If the entry was found and marked deleted, write the updated file
        if (updated) {
            writeAllLines(allLines);
        }
        return updated;
    }

    /**
     * Generates the next entry ID by finding the highest current entry ID and
     * adding one.
     *
     * @return the next entry ID as a string
     * @throws IOException if an I/O error occurs
     */
    public String nextEntryId() throws IOException {
        int max = 0;

        // Iterate over all existing entries and find the highest entry ID
        for (int i = 0; i < entries.size(); i++) {
            try {
                // Attempt to parse the entry ID as an integer
                max = Math.max(max, Integer.parseInt(entries.getElement(i).entryNumber));
            } catch (NumberFormatException e) {
                // Ignore any entries with invalid IDs
                System.out.println(e.getMessage());
            }
        }

        // Return the next entry ID by adding one to the highest current entry ID
        return String.format("%03d", max + 1);
    }

    // searches

    public CustomArrayList<Object> searchByField(String fieldKey, String value) {
        CustomArrayList<Object> results = new CustomArrayList<>();
        try {
            CustomArrayList<TxtEntry> all = readAllEntries(lineStarter);
            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                if (!e.deleted) {
                    String v = e.fields.get(fieldKey);
                    if (v != null && v.equalsIgnoreCase(value.trim())) {
                        results.addElement(wrap(e));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching entries: " + e.getMessage());
        }
        return results;
    }

    /**
     * Searches for entries that have a field with a value between the given
     * range.
     * <p>
     * This method works by reading all entries from the file, parsing the field
     * values, and comparing them to the given range. It returns a list of
     * matching entries in the same format as the {@link #searchByField(String, String)}
     * method.
     * </p>
     * 
     * @param fieldKey the key of the field to search
     * @param low      the lower bound of the range (inclusive)
     * @param high     the upper bound of the range (inclusive)
     * @return a list of matching entries
     */
    public CustomArrayList<Object> searchByRange(String fieldKey, double low, double high) {
        CustomArrayList<Object> results = new CustomArrayList<>();
        try {
            CustomArrayList<TxtEntry> all = readAllEntries(lineStarter);
            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                if (!e.deleted) {
                    String v = e.fields.get(fieldKey);
                    if (v != null) {
                        try {
                            double d = Double.parseDouble(v.trim());
                            if (d >= low && d <= high) {
                                // Add the matching entry to the results
                                results.addElement(wrap(e));
                            }
                        } catch (NumberFormatException ignore) {
                            // Ignore entries with invalid field values
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching entries: " + e.getMessage());
        }
        return results;
    }

    // helpers

    /**
     * Wraps a TxtEntry in a CustomArrayList, suitable for printing.
     * <p>
     * This method takes a TxtEntry and extracts all its raw lines, and
     * returns a new CustomArrayList containing those lines.
     * </p>
     *
     * @param e the TxtEntry to wrap
     * @return a CustomArrayList containing the raw lines of the TxtEntry
     */
    private CustomArrayList<String> wrap(TxtEntry e) {
        CustomArrayList<String> block = new CustomArrayList<>();
        for (int i = 0; i < e.rawLines.size(); i++) {
            block.addElement(e.rawLines.getElement(i));
        }
        return block;
    }

    /**
     * Print out only the entry ID (stripping "Entry ") for each result.
     * <p>
     * This method iterates over the given results list and prints out the entry
     * ID for each entry. If the entry is wrapped in a CustomArrayList, it
     * extracts the entry ID from the first element of the list; otherwise, it
     * prints the object directly.
     * </p>
     * 
     * @param results the list of results to print
     */
    public void printEntryNumbers(CustomArrayList<Object> results) {
        for (int i = 0; i < results.size(); i++) {
            Object entryObj = results.getElement(i);
            if (entryObj instanceof CustomArrayList<?> entry) {
                // Extract the entry header from the first element of the list
                String header = entry.getElement(0).toString();
                // Strip the "Entry " prefix from the header
                String prefix = (this.lineStarter == null ? "" : this.lineStarter);
                String entryNumber = header.startsWith(prefix)
                        ? header.substring(prefix.length()).trim()
                        : header;
                // Print the entry number
                System.out.println("Entry number found: " + entryNumber + "\n");
            } else {
                // Print the object directly if it's not a wrapped entry
                System.out.println(entryObj);
            }
        }
    }

    /**
     * Refine the given results by matching "Entry ..." headers with the given
     * candidates. This method is a drop-in helper to narrow down the search
     * results.
     * 
     * @param current   the current results to refine
     * @param candidates the candidate results to match against
     * @return a new list of refined results
     */
    public CustomArrayList<Object> refineByHeaderMatch(CustomArrayList<Object> current,
            CustomArrayList<Object> candidates) {
        if (current == null || candidates == null)
            return new CustomArrayList<>();

        CustomArrayList<Object> temp = new CustomArrayList<>();
        // Iterate over the current results and match them against the candidates
        for (int i = 0; i < current.size(); i++) {
            Object entry = current.getElement(i);
            if (!(entry instanceof CustomArrayList))
                continue;
            // Get the trimmed header of the current result
            String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

            // Iterate over the candidates and find a match
            for (int j = 0; j < candidates.size(); j++) {
                Object candidate = candidates.getElement(j);
                if (!(candidate instanceof CustomArrayList))
                    continue;
                // Get the trimmed header of the current candidate
                String candidateHeader = ((CustomArrayList<?>) candidate).getElement(0).toString().trim();

                // If the headers match, add the current result to the refined list
                if (entryHeader.equals(candidateHeader)) {
                    temp.addElement(entry);
                    break;
                }
            }
        }
        return temp;
    }

    /**
     * Check if a given driverID exists in driverDetails.txt and is not deleted.
     * Returns true only if an active entry with that ID is found.
     * @param driverID the driver ID to search for
     * @return true if the driver ID exists and is not deleted, false otherwise
     */
    public boolean doesDriverExist(String driverID) {
        try {
            // Create a TextEntryManager to read the driverDetails.txt file
            TextEntryManager driverFinder = new TextEntryManager("LogisticsProject/src/TXTDatabase/",
                    "LogisticsProject/src/TXTDatabase/driverDetails.txt", "Entry ");

            // Read all lines in the file
            CustomArrayList<String> allLines = driverFinder.readAllLines();

            // Iterate over all lines in the file
            for (int i = 0; i < allLines.size(); i++) {
                String line = allLines.getElement(i);
                if (line.startsWith("Entry ")) {
                    // Check if the current entry matches the given driver ID
                    boolean idMatch = false;
                    String status = "Active";
                    // Iterate over the lines in the current entry
                    while (i < allLines.size() - 1) {
                        line = allLines.getElement(++i);
                        // Check if the line contains the driver ID
                        if (line.startsWith("Driver ID:")) {
                            String id = line.substring("Driver ID:".length()).trim();
                            idMatch = id.equalsIgnoreCase(driverID);
                            continue;
                        }
                        // Check if the line contains the status
                        if (line.startsWith("Status:")) {
                            status = line.substring("Status:".length()).trim();
                            // If the entry matches the given ID and is not deleted, return true
                            if (idMatch && !status.equalsIgnoreCase("Deleted")) {
                                return true;
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Print an error message if an IOException occurs
            System.out.println("Error reading driverDetails.txt: " + e.getMessage());
        }
        // Return false if no matching entry is found
        return false;
    }
}
