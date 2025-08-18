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
    }

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
                    int lineStarterLength = lineStarter.length();
                    current = new TxtEntry(line.substring(lineStarterLength).trim());
                    entries.addElement(current);
                    current.rawLines.addElement(line);
                } else if (current != null) {
                    current.rawLines.addElement(line);
                    if ("----------".equals(line.trim()) || line.isBlank()) {
                        parseFields(current);
                        current = null;
                    }
                }
            }
        }
        return entries;
    }

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

    public void writeEntry(TxtEntry entry) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile, true))) {
            writer.write(entry.entryNumber + "\n");
            for (int i = 0; i < entry.rawLines.size(); i++) {
                String line = entry.rawLines.getElement(i);
                writer.write(line + "\n");
            }
            writer.write("----------\n");
        }
    }

    // Method to read all lines from the file
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

    // Method to write all lines to the file
    private void writeAllLines(CustomArrayList<String> allLines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
            for (int i = 0; i < allLines.size(); i++) {
                writer.write(allLines.getElement(i) + "\n");
            }
        }
    }

    // Method to mark an entry as deleted
    public boolean markDeleted(String entryId, String deletor) throws IOException {
        CustomArrayList<String> allLines = readAllLines();
        boolean updated = false;
        boolean inBlock = false;
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.getElement(i);
            if (line.equals(lineStarter + entryId)) {
                inBlock = true;
            }
            if (inBlock && line.startsWith(deletor)) {
                allLines.setElement(i, deletor + ": Deleted");
                updated = true;
                inBlock = false;
            }
        }
        if (updated) {
            writeAllLines(allLines);
        }
        return updated;
    }

    public String nextEntryId() throws IOException {
        int max = 0;

        for (int i = 0; i < entries.size(); i++) {
            try {
                max = Math.max(max, Integer.parseInt(entries.getElement(i).entryNumber));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        return String.format("03d", max + 1);
    }

    // searches

    public CustomArrayList<Object> 
    searchByField(String fieldKey, String value) {
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

    public CustomArrayList<Object> searchByRange(String fieldKey, double low, double high) {
        CustomArrayList<Object> results = new CustomArrayList<>();
        try {
            CustomArrayList<TxtEntry> all = readAllEntries(lineStarter);
            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                if (!e.deleted) {
                    String v = e.fields.get(fieldKey);
                    if (v != null && Double.parseDouble(v) >= low && Double.parseDouble(v) <= high) {
                        results.addElement(wrap(e));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching entries: " + e.getMessage());
        }
        return results;
    }

    // helpers
    private CustomArrayList<String> wrap(TxtEntry e) {
        CustomArrayList<String> block = new CustomArrayList<>();
        for (int i = 0; i < e.rawLines.size(); i++) {
            block.addElement(e.rawLines.getElement(i));
        }
        return block;
    }

    public void printEntryNumbers(CustomArrayList<Object> results) {
        for (int i = 0; i < results.size(); i++) {
            Object entryObj = results.getElement(i);
            if (entryObj instanceof CustomArrayList<?> entry) {
                String header = entry.getElement(0).toString();
                String entryNumber = header.replace("Entry ", "").trim();
                System.out.println("Entry number found: " + entryNumber);
            } else {
                // fallback (should not happen in normal flow)
                System.out.println(entryObj);
            }
        }
    }
}
