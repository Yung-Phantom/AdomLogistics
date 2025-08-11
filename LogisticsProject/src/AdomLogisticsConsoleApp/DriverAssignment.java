package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import CustomDataStructures.CustomArrayList;

public class DriverAssignment {

    BannerElements bElements;
    BannerElements addDriver;
    BannerElements searchByElements;
    BannerElements updateDriver;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    String userStringInput;
    double userDoubleInput;

    File txtStorageDir;
    File txtStorage;

    CustomArrayList<Object> addDriverDetails;
    CustomArrayList<Object> addNewEntry;
    CustomArrayList<String> updateDriverDetails;
    File driverStorage;
    File finalStorageDir;

    public DriverAssignment(int width) {
        bElements = new BannerElements(width, "Adom Logistics Driver Management");
        bElements.printBanner();
        bElements.addToMenu("Add new driver");
        bElements.addToMenu("Remove driver");
        bElements.addToMenu("Update driver activity");
        bElements.addToMenu("Assign driver");
        bElements.addToMenu("View driver activity");
        bElements.addToMenu("Exit");
        bElements.printMenu();

        addDriver = new BannerElements(userInput, "Add new driver");
        addDriver.addToMenu("Please enter the driver's name.");
        addDriver.addToMenu("Please enter the driver's license number.");
        addDriver.addToMenu("Please enter the driver's phone number.");
        addDriver.addToMenu("Please enter the driver's email address.");
        addDriver.addToMenu("Please enter the driver's address.");
        addDriver.addToMenu("Please enter the maintenance history of the vehicle.");

        updateDriver = new BannerElements(userInput, "Update Driver Activity");
        updateDriver.addToMenu("Please enter the driver's ID.");
        updateDriver.addToMenu("Please enter the new route ID.");
        updateDriver.addToMenu("Was there a delay? (yes/no)");
        updateDriver.addToMenu("Please enter any infraction notes.(leave blank if none):");
    }

    public void selectMenuItem() {
        // call the validity method to get the boolean and int that will be used for the
        validity(scanner, bElements.size());
        boolean choice = trueFalse;
        int choiceInput = userInput;

        if (choice) {
            switch (choiceInput) {
                case 0:
                    bElements.printMenu();
                    selectMenuItem(); // Call the method again to allow the user to select another option
                    break;
                case 1:
                    addNewDriver();
                    break;
                case 2:
                    removeDriver();
                    break;
                case 3:
                    updateDriverActivity();
                    break;
                case 4:
                    assignDriverMenu();
                    break;
                case 5:
                    updateDriverActivity();
                    break;
                case 6:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;

            }
        }
    }

    private void assignDriverMenu() {
        
    }

    // Helper: check if driverID exists in driverDetails.txt (supports a few common
    // formats)
    private boolean driverExistsInDetails(String driverID) {
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
        if (!detailsFile.exists())
            return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty())
                    continue;

                // Format 1: "Driver ID: DRV001"
                if (trimmed.startsWith("Driver ID:")) {
                    String id = trimmed.substring("Driver ID:".length()).trim();
                    if (driverID.equals(id))
                        return true;
                    continue;
                }

                // Format 2: pipe-delimited with ID as first field, e.g., "DRV001|John Doe|..."
                if (trimmed.contains("|")) {
                    String first = trimmed.split("\\|", -1)[0].trim();
                    if (driverID.equals(first))
                        return true;
                    continue;
                }

                // Format 3: line equals the ID
                if (driverID.equals(trimmed))
                    return true;
            }
        } catch (IOException e) {
            System.out.println("Error checking driverDetails.txt: " + e.getMessage());
        }
        return false;
    }

    private void updateDriverActivity() {
        updateDriverDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();

        System.out.println("\n=== Update Driver Activity ===");

        // Prompt 0: Driver ID
        System.out.println("\n" + updateDriver.getElement(0));
        validityString(scanner);
        String driverID = userStringInput;
        updateDriverDetails.addElement(driverID);

        // Gate: only proceed if driver exists in driverDetails.txt
        if (!driverExistsInDetails(driverID)) {
            System.out.println("\nDriver ID \"" + driverID + "\" not found in driverDetails.txt.");
            System.out.println("Activity update cancelled.\n");
            return;
        }

        // Prompt 1: Route ID
        System.out.println("\n" + updateDriver.getElement(1));
        validityString(scanner);
        String routeID = userStringInput;
        updateDriverDetails.addElement(routeID);

        // Prompt 2: Delay? (yes/no only)
        while (true) {
            System.out.println("\n" + updateDriver.getElement(2));
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                updateDriverDetails.addElement(input);
                break;
            } else {
                System.out.println("Invalid input. Please enter \"yes\" or \"no\".");
            }
        }

        // Prompt 3: Infraction (optional)
        System.out.println("\n" + updateDriver.getElement(3));
        String infraction = scanner.nextLine().trim();
        updateDriverDetails.addElement(infraction);

        boolean delayed = updateDriverDetails.getElement(2).equalsIgnoreCase("yes");

        finalStorageDir = new File("LogisticsProject/src/finalDatabase").getAbsoluteFile();
        if (!finalStorageDir.exists())
            finalStorageDir.mkdirs();

        driverStorage = new File("LogisticsProject/src/finalDatabase/drivers.txt").getAbsoluteFile();
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();

        // Step: resolve name from driverDetails.txt if available
        String resolvedNameFromDetails = null;
        if (detailsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
                String line;
                boolean insideBlock = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Driver ID: ")) {
                        insideBlock = line.equals("Driver ID: " + driverID);
                        continue;
                    }
                    if (insideBlock) {
                        if (line.startsWith("Driver Name: ")) {
                            resolvedNameFromDetails = line.substring("Driver Name: ".length()).trim();
                            break;
                        }
                        if (line.startsWith("Entry")) {
                            insideBlock = false;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error accessing driverDetails.txt: " + e.getMessage());
            }
        }

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try {
            if (driverStorage.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(driverStorage))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|", -1);
                        if (parts.length < 6) {
                            updatedLines.add(line);
                            continue;
                        }

                        if (parts[0].trim().equals(driverID)) {
                            found = true;

                            // Replace "Unknown" name if correct name now available
                            String nameField = parts[1].trim();
                            if (nameField.equalsIgnoreCase("Unknown") && resolvedNameFromDetails != null) {
                                nameField = resolvedNameFromDetails;
                            }

                            String existingRoutes = parts[2].trim();
                            String routes = existingRoutes.isEmpty() ? routeID : existingRoutes + "," + routeID;

                            int delayCount = safeParseInt(parts[3].trim(), 0);
                            if (delayed)
                                delayCount++;

                            int infractionCount = safeParseInt(parts[4].trim(), 0);
                            String infractions = parts[5].trim();
                            if (!infraction.isEmpty()) {
                                infractionCount++;
                                infractions = infractions.isEmpty() ? infraction : infractions + "," + infraction;
                            }

                            String updated = String.join("|",
                                    parts[0].trim(),
                                    nameField,
                                    routes,
                                    String.valueOf(delayCount),
                                    String.valueOf(infractionCount),
                                    infractions);
                            updatedLines.add(updated);
                        } else {
                            updatedLines.add(line);
                        }
                    }
                }
            }

            // If driver doesn't exist in drivers.txt, create new entry using known name or
            // "Unknown"
            if (!found) {
                String name = (resolvedNameFromDetails != null) ? resolvedNameFromDetails : "Unknown";
                String routes = routeID;
                int delayCount = delayed ? 1 : 0;
                int infractionCount = infraction.isEmpty() ? 0 : 1;
                String infractions = infraction.isEmpty() ? "" : infraction;

                String newDriverLine = String.join("|",
                        driverID, name, routes,
                        String.valueOf(delayCount),
                        String.valueOf(infractionCount),
                        infractions);
                updatedLines.add(newDriverLine);
                System.out.println("New driver added to drivers.txt and activity recorded.");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(driverStorage))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
                System.out.println("Driver activity updated successfully.");
            }

        } catch (IOException e) {
            System.out.println("File access error: " + e.getMessage());
        }
    }

    // Inline-safe integer parser for delay/infraction counts
    private int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void removeDriver() {
        CustomArrayList<Object> searchResults = searchDriverString("License Number");

        if (searchResults.size() > 1) {
            CustomArrayList<Object> phoneResults = searchDriverString("Phone Number");
            CustomArrayList<Object> temp = new CustomArrayList<>();

            for (int i = 0; i < searchResults.size(); i++) {
                Object entry = searchResults.getElement(i);
                if (!(entry instanceof CustomArrayList))
                    continue;
                String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                for (int j = 0; j < phoneResults.size(); j++) {
                    Object candidate = phoneResults.getElement(j);
                    if (!(candidate instanceof CustomArrayList))
                        continue;
                    String candidateHeader = ((CustomArrayList<?>) candidate).getElement(0).toString().trim();

                    if (entryHeader.equals(candidateHeader)) {
                        temp.addElement(entry);
                        break;
                    }
                }
            }
            searchResults = temp;
        }

        if (searchResults.size() == 1) {
            Object entryObj = searchResults.getElement(0);
            if (entryObj instanceof CustomArrayList) {
                CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                String header = entry.getElement(0).toString();
                String entryNumber = header.replace("Entry ", "").trim();
                updateTxt(entryNumber);
            } else {
                System.out.println("Unexpected result format.");
            }
        } else if (searchResults.size() == 0) {
            System.out.println("No matching driver found.");
        } else {
            System.out.println("Multiple entries still match. Manual review may be required.");
        }
    }

    public void updateTxt(String uniqueIdentifier) {
        txtStorageDir = new File("LogisticsProject/src/TXTDatabase").getAbsoluteFile();
        if (!txtStorageDir.exists()) {
            txtStorageDir.mkdirs();
        }

        txtStorage = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(txtStorage))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        String content = fileContent.toString();
        int entryIndex = content.indexOf("Entry " + uniqueIdentifier);
        if (entryIndex != -1) {
            int statusIndex = content.indexOf("Status: Active", entryIndex);
            if (statusIndex != -1) {
                // Extract name and license number
                int nameIndex = content.indexOf("Driver Name:", entryIndex);
                int licenseIndex = content.indexOf("License Number:", entryIndex);

                String driverName = "Unknown";
                String licenseNumber = "Unknown";

                if (nameIndex != -1) {
                    int nameEnd = content.indexOf("\n", nameIndex);
                    driverName = content.substring(nameIndex + "Driver Name:".length(), nameEnd).trim();
                }

                if (licenseIndex != -1) {
                    int licenseEnd = content.indexOf("\n", licenseIndex);
                    licenseNumber = content.substring(licenseIndex + "License Number:".length(), licenseEnd).trim();
                }

                System.out.print("Confirm remove for " + driverName + " [License: " + licenseNumber + "]? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!response.equals("yes")) {
                    System.out.println("Remove cancelled.");
                    return;
                }

                content = content.substring(0, statusIndex) + "Status: Deleted"
                        + content.substring(statusIndex + "Status: Active".length());

                try (FileWriter writer = new FileWriter(txtStorage)) {
                    writer.write(content);
                    System.out.println(driverName + " with License: " + licenseNumber + " removed.");
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
            } else {
                System.out.println("Status field not found near the identifier.");
            }
        } else {
            System.out.println("Entry not found for " + uniqueIdentifier + ".");
        }
    }

    public CustomArrayList<Object> searchDriverString(String searchString) {
        System.out.println("Enter: " + searchString);
        validityString(scanner); // Validate input
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput;

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            System.out.println(
                    "Searching for drivers with " + searchString + " matching \"" + userSearchInputString + "\"");

            txtStorage = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(txtStorage))) {
                String line;
                boolean insideEntry = false;
                String currentEntryNumber = null;
                CustomArrayList<String> entryLines = new CustomArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();

                    // Detect entry start
                    if (!insideEntry && trimmedLine.startsWith("Entry")) {
                        currentEntryNumber = trimmedLine.replace("Entry", "").trim();
                        insideEntry = true;
                        entryLines.clear();
                        entryLines.addElement(trimmedLine);
                        continue;
                    }

                    if (insideEntry) {
                        entryLines.addElement(trimmedLine);

                        // Detect end of entry block
                        if (trimmedLine.startsWith("Status:")) {
                            boolean isDeleted = trimmedLine.equalsIgnoreCase("Status: Deleted");
                            String matchedFieldValue = null;

                            for (int i = 0; i < entryLines.size(); i++) {
                                String e = entryLines.getElement(i).trim();
                                if (e.startsWith(searchString + ":")) {
                                    matchedFieldValue = e.substring(e.indexOf(":") + 1).trim();
                                    break;
                                }
                            }

                            if (!isDeleted && matchedFieldValue != null &&
                                    matchedFieldValue.equalsIgnoreCase(userSearchInputString)) {
                                CustomArrayList<String> oneEntry = new CustomArrayList<>();
                                oneEntry.addElement("Entry " + (currentEntryNumber == null ? "" : currentEntryNumber));
                                for (int i = 0; i < entryLines.size(); i++) {
                                    oneEntry.addElement(entryLines.getElement(i));
                                }
                                results.addElement(oneEntry);
                            }

                            insideEntry = false;
                            currentEntryNumber = null;
                            entryLines.clear();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading TXT database: " + e.getMessage());
            }
        }

        for (int index = 0; index < results.size(); index++) {
            Object entryObj = results.getElement(index);

            if (entryObj instanceof CustomArrayList) {
                CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                String header = entry.getElement(0).toString(); // "Entry 001"
                String entryNumber = header.replace("Entry", "").trim();
                System.out.println("Entry number found: " + entryNumber);
            } else {
                System.out.println(entryObj); // fallback
            }
        }

        return results;
    }

    private void addNewDriver() {
        addDriverDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();

        // 0) Capture Driver ID first (so update/search can key on it)
        System.out.println("Enter Driver ID:");
        validityString(scanner);
        String driverId = userStringInput;
        addDriverDetails.addElement(driverId);

        // 1) Capture existing fields using your configured prompts in `addDriver`
        // Expected order of prompts currently in `addDriver`:
        // 0: Driver Name
        // 1: Driver's license
        // 2: Driver's phone number
        // 3: Driver's email address
        // 4: Driver address
        // 5: Driver maintenance history
        for (int i = 0; i < addDriver.size(); i++) {
            System.out.println(addDriver.getElement(i));
            switch (i) {
                case 0: // Driver Name
                case 1: // Driver's license
                case 3: // Driver's email address
                case 4: // Driver address
                case 5: // Driver maintenance history
                    validityString(scanner);
                    addDriverDetails.addElement(userStringInput);
                    break;
                case 2: // Driver's phone number
                    validityPhoneString(scanner);
                    addDriverDetails.addElement(userStringInput);
                    break;
                default:
                    // Fallback: treat as a normal string field
                    validityString(scanner);
                    addDriverDetails.addElement(userStringInput);
            }
        }

        System.out.println("Operation successful");

        // 2) Build nicely labeled output with new indices
        // Now the indices are:
        // 0: Driver ID
        // 1: Driver Name
        // 2: Driver's license
        // 3: Driver's phone number
        // 4: Driver's email address
        // 5: Driver address
        // 6: Driver maintenance history
        for (int index = 0; index < addDriverDetails.size(); index++) {
            if (index == 0) {
                addNewEntry.addElement("Driver ID: " + addDriverDetails.getElement(index));
            } else if (index == 1) {
                addNewEntry.addElement("Driver Name: " + addDriverDetails.getElement(index));
            } else if (index == 2) {
                addNewEntry.addElement("Driver's license: " + addDriverDetails.getElement(index));
            } else if (index == 3) {
                addNewEntry.addElement("Driver's phone number: " + addDriverDetails.getElement(index));
            } else if (index == 4) {
                addNewEntry.addElement("Driver's email address: " + addDriverDetails.getElement(index));
            } else if (index == 5) {
                addNewEntry.addElement("Driver address: " + addDriverDetails.getElement(index));
            } else if (index == 6) {
                addNewEntry.addElement("Driver maintenance history: " + addDriverDetails.getElement(index));
            }
        }

        // 3) Persist: saveTXT now receives the ID as the first element
        // Ensure your saveTXT respects this order so search-by-ID works cleanly
        saveTXT(addDriverDetails);

        // 4) Echo summary
        for (int i = 0; i < addNewEntry.size(); i++) {
            System.out.println(addNewEntry.getElement(i));
        }
    }

    public void saveTXT(CustomArrayList<Object> driverDetails) {
        // Ensure the TXT storage directory exists
        txtStorageDir = new File("LogisticsProject/src/TXTDatabase").getAbsoluteFile();
        if (!txtStorageDir.exists()) {
            txtStorageDir.mkdirs();
        }

        // Define the storage file
        txtStorage = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();

        // Count existing entries
        int entryCount = 1;
        try {
            if (!txtStorage.exists()) {
                txtStorage.createNewFile();
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(txtStorage))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith("Entry")) {
                            entryCount++;
                        }
                    }
                }
            }

            // Build new entry block with updated indices
            StringBuilder newEntry = new StringBuilder();
            newEntry.append("Entry ").append(String.format("%03d", entryCount)).append("\n");
            newEntry.append("Driver ID: ").append(driverDetails.getElement(0)).append("\n");
            newEntry.append("Driver Name: ").append(driverDetails.getElement(1)).append("\n");
            newEntry.append("License Number: ").append(driverDetails.getElement(2)).append("\n");
            newEntry.append("Phone Number: ").append(driverDetails.getElement(3)).append("\n");
            newEntry.append("Email Address: ").append(driverDetails.getElement(4)).append("\n");
            newEntry.append("Address: ").append(driverDetails.getElement(5)).append("\n");
            newEntry.append("Maintenance History: ").append(driverDetails.getElement(6)).append("\n");
            newEntry.append("Status: Active\n");
            newEntry.append("--------------------------------------------------\n");

            // Append to file
            try (FileWriter writer = new FileWriter(txtStorage, true)) {
                writer.write(newEntry.toString());
                System.out.println("Driver entry " + entryCount + " saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void validityPhoneString(Scanner scanner) {
        while (true) {
            scanner.nextLine(); // clear buffer
            System.out.print("Enter phone number (digits only): ");
            try {
                userStringInput = scanner.nextLine().trim();
                if (userStringInput.matches("\\d{10,16}")) { // Accepts 10–16 digits
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid phone number. Enter 10–16 digits only.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // clear buffer
            }
        }
    }

    public void validity(Scanner scanner, int i) {
        while (true) {
            System.out.print("Please enter your choice (1 - " + i + "): ");
            try {
                userInput = scanner.nextInt();
                if (userInput >= 0 && userInput <= i) {
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Try again. Enter 0 to view the menu");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number. Enter 0 to view the menu");
                scanner.nextLine(); // clear the buffer
            }
        }
    }

    public void validityDouble(Scanner scanner) {
        while (true) {
            scanner.nextLine(); // clear the buffer
            try {
                userDoubleInput = scanner.nextDouble();
                if (userDoubleInput >= 0) {
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Please enter a positive double.");
                }
                break;
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid double.");
                scanner.nextLine(); // clear the buffer
            }
        }
    }

    public void validityString(Scanner scanner) {
        while (true) {
            scanner.nextLine(); // clear the buffer
            try {
                userStringInput = scanner.nextLine();
                if (userStringInput.length() > 0) {
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a non-empty string.");
                }
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid string.");
                scanner.nextLine(); // clear the buffer
            }
        }
    }

    public static void main(String[] args) {
        // System.out.println("hello world");
        DriverAssignment driverAssignment = new DriverAssignment(50);
        driverAssignment.selectMenuItem();
    }
}
// Driver Assignment
// o Manage a stack or queue of available drivers.
// o Drivers are assigned based on proximity or experience.
// o Track driver activity: assigned routes, recent delays, infractions.
