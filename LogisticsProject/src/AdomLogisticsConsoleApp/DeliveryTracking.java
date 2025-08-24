package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import CustomDataStructures.DoublyLinkedList;
import java.util.Scanner;

import CustomDataStructures.CustomArrayList;

public class DeliveryTracking {

    BannerElements bElements;
    BannerElements addPackage;
    BannerElements packageViews;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    CustomArrayList<String> load;
    CustomArrayList<Object> addPackageDetails;
    CustomArrayList<Object> addNewEntry;
    File packageStorage;
    File finalStorageDir;

    DoublyLinkedList<String> packageIDList;
    DoublyLinkedList<String> originList;
    DoublyLinkedList<String> destinationList;
    DoublyLinkedList<String> ETAList;
    DoublyLinkedList<String> assignedDriverIDList;
    DoublyLinkedList<String> assignedVehicleIDList;
    DoublyLinkedList<String> statusList;
    String userStringInput;
    double userDoubleInput;
    static String lastVehicleReg;
    static String lastDriverId;

    public DeliveryTracking(int width) {
        bElements = new BannerElements(width, "Adom Logistics Delivery Tracking");
        bElements.printBanner();
        bElements.addToMenu("Add new delivery");
        bElements.addToMenu("View deliveries");
        bElements.addToMenu("Search for delivery");
        bElements.addToMenu("Exit");
        bElements.printMenu();

        addPackage = new BannerElements(userInput, "Add new package");
        addPackage.addToMenu("Please enter Package ID: ");
        addPackage.addToMenu("Please enter package name");
        addPackage.addToMenu("Please enter the origin");
        addPackage.addToMenu("Please enter the destination");
        addPackage.addToMenu("Please enter the ETA (e.g., 2025-07-17 10:30)");

        packageViews = new BannerElements(userInput, "View packages");
        packageViews.addToMenu("View pending packages");
        packageViews.addToMenu("View packages in transit");
        packageViews.addToMenu("View delivered packages");
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
                    selectMenuItem();
                    break;
                case 1:
                    addDelivery();
                    break;
                case 2:
                    viewDeliveryDetails();
                    break;
                case 3:
                    searchDeliveryString("Package ID");
                    break;
                case 4:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;

            }
        }
    }

    public CustomArrayList<Object> searchDeliveryString(String searchString) {
        // Prompt & validate
        scanner.nextLine();
        System.out.println("Enter :" + searchString);
        validityString(scanner); // sets trueFalse + userStringInput
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput == null
                ? ""
                : userStringInput.trim();

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            System.out.println(
                    "Searching for deliveries with " + searchString + " matching \"" + userSearchInputString + "\"");

            File packageDetails = new File("LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(packageDetails))) {

                String line;
                boolean insideEntry = false;
                CustomArrayList<String> entryLines = new CustomArrayList<>();

                while ((line = reader.readLine()) != null) {
                    // Detect start of a new block
                    if (!insideEntry && line.startsWith("Entry ")) {
                        insideEntry = true;
                        entryLines.clear();
                    }

                    if (insideEntry) {
                        // Buffer every raw line
                        entryLines.addElement(line);

                        // On delimiter, evaluate and collect match
                        if (line.trim().equals("----------")) {
                            String matchedFieldValue = null;

                            // Find the field value in this block
                            for (int i = 0; i < entryLines.size(); i++) {
                                String e = entryLines.getElement(i).trim();
                                if (e.startsWith(searchString + ":")) {
                                    matchedFieldValue = e
                                            .substring((searchString + ":").length())
                                            .trim();
                                    break;
                                }
                            }

                            // If the field matches user input, save the entire block
                            if (matchedFieldValue != null
                                    && matchedFieldValue.equalsIgnoreCase(userSearchInputString)) {
                                CustomArrayList<String> oneEntry = new CustomArrayList<>();
                                for (int i = 0; i < entryLines.size(); i++) {
                                    oneEntry.addElement(entryLines.getElement(i));
                                }
                                results.addElement(oneEntry);
                            }

                            insideEntry = false;
                            entryLines.clear();
                        }
                    }
                }

                // Handle a final block if the file doesn't end with "----------"
                if (insideEntry && entryLines.size() > 0) {
                    String matchedFieldValue = null;
                    for (int i = 0; i < entryLines.size(); i++) {
                        String e = entryLines.getElement(i).trim();
                        if (e.startsWith(searchString + ":")) {
                            matchedFieldValue = e
                                    .substring((searchString + ":").length())
                                    .trim();
                            break;
                        }
                    }
                    if (matchedFieldValue != null
                            && matchedFieldValue.equalsIgnoreCase(userSearchInputString)) {
                        CustomArrayList<String> oneEntry = new CustomArrayList<>();
                        for (int i = 0; i < entryLines.size(); i++) {
                            oneEntry.addElement(entryLines.getElement(i));
                        }
                        results.addElement(oneEntry);
                    }
                }

            } catch (IOException e) {
                System.out.println("Error reading packageDetails.txt: " + e.getMessage());
            }
        }

        // Display concise confirmation of matches
        for (int idx = 0; idx < results.size(); idx++) {
            Object entryObj = results.getElement(idx);
            if (entryObj instanceof CustomArrayList) {
                CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                String header = entry.getElement(0).toString(); // "Entry NNN"
                String entryNumber = header.replace("Entry ", "").trim();
                System.out.println("Entry number found: " + entryNumber);
            } else {
                System.out.println(entryObj);
            }
        }

        return results;
    }

    private void viewDeliveryDetails() {

        for (int i = 0; i < packageViews.size(); i++) {
            System.out.println(packageViews.getElement(i));
        }
        validity(scanner, packageViews.size());
        boolean choice = trueFalse;
        int choiceInput = userInput;
        if (choice) {
            switch (choiceInput) {
                case 0:
                    viewDeliveryDetails();
                    break;
                case 1:
                    viewByStatus("Pending");
                    break;
                case 2:
                    viewByStatus("In Transit");
                    break;
                case 3:
                    viewByStatus("Delivered");
                    break;
                case 4:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;
                default:
                    break;
            }
        }
    }

    private void viewByStatus(String desiredStatus) {
        File packageDetails = new File(
                "LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

        if (!packageDetails.exists() || packageDetails.length() == 0) {
            System.out.println("No deliveries found.");
            return;
        }

        CustomArrayList<String> entryBuffer = new CustomArrayList<>();
        boolean shouldPrint = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(packageDetails))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                // Start of a new entry block
                if (trimmed.startsWith("Entry ")) {
                    entryBuffer.clear();
                    shouldPrint = false;
                }

                entryBuffer.addElement(line);

                // Detect status match
                if (trimmed.startsWith("Status:")) {
                    String status = trimmed.substring("Status:".length()).trim();
                    if (status.equalsIgnoreCase(desiredStatus)) {
                        shouldPrint = true;
                    }
                }

                // End of block delimiter
                if (trimmed.equals("----------")) {
                    if (shouldPrint) {
                        // Iterate by index since CustomArrayList isn't iterable
                        for (int i = 0; i < entryBuffer.size(); i++) {
                            System.out.println(entryBuffer.getElement(i));
                        }
                    }
                    // Reset buffer for next block
                    entryBuffer.clear();
                    shouldPrint = false;
                }
            }

            // Handle case where file doesn't end with "----------"
            if (shouldPrint && entryBuffer.size() > 0) {
                for (int i = 0; i < entryBuffer.size(); i++) {
                    System.out.println(entryBuffer.getElement(i));
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading packageDetails.txt: " + e.getMessage());
        }
    }

    private void addDelivery() {
        addPackageDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();

        // Check silently if file exists & is non-empty
        File packageDetailsFile = new File("LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();
        if (!packageDetailsFile.exists() || packageDetailsFile.length() == 0) {
            // No warning printed
        }

        for (int i = 0; i < addPackage.size(); i++) {
            System.out.println(addPackage.getElement(i));
            switch (i) {
                case 0:
                    scanner.nextLine(); // clear buffer

                    validityString(scanner);

                    // Uniqueness check BEFORE any more input
                    if (packageIdExists(userStringInput)) {
                        System.out.println("A delivery with this Package ID already exists. Aborting.");
                        return;
                    }
                    addPackageDetails.addElement(userStringInput);
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    validityString(scanner);
                    addPackageDetails.addElement(userStringInput);
                    break;

            }
        }
        System.out.println("Operation successful");

        for (int index = 0; index < addPackageDetails.size(); index++) {
            if (index == 0) {
                addNewEntry.addElement("Package ID: " + addPackageDetails.getElement(index));
            } else if (index == 1) {
                addNewEntry.addElement("Package Name: " + addPackageDetails.getElement(index));
            } else if (index == 2) {
                addNewEntry.addElement("Package origin: " + addPackageDetails.getElement(index));
            } else if (index == 3) {
                addNewEntry.addElement("Package destination: " + addPackageDetails.getElement(index));
            } else if (index == 4) {
                addNewEntry.addElement("Package ETA: " + addPackageDetails.getElement(index));
            }
        }

        // Save
        saveTXT(addPackageDetails);

        // Print confirmation
        for (int i = 0; i < addNewEntry.size(); i++) {
            System.out.println(addNewEntry.getElement(i));
        }
    }

    private void saveTXT(CustomArrayList<Object> addPackageDetails2) {
        finalStorageDir = new File("LogisticsProject/src/TXTDatabase").getAbsoluteFile();
        if (!finalStorageDir.exists()) {
            finalStorageDir.mkdirs();
        }

        // Corrected: saving to packageDetails.txt
        packageStorage = new File("LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

        int entryCount = 1;
        try {
            if (!packageStorage.exists()) {
                packageStorage.createNewFile();
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(packageStorage))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith("Entry")) {
                            entryCount++;
                        }
                    }
                }
            }

            StringBuilder newEntry = new StringBuilder();
            newEntry.append("Entry ").append(String.format("%03d", entryCount)).append("\n");
            newEntry.append("Package ID: ").append(addPackageDetails.getElement(0)).append("\n");
            newEntry.append("Package Name: ").append(addPackageDetails.getElement(1)).append("\n");
            newEntry.append("Package origin: ").append(addPackageDetails.getElement(2)).append("\n");
            newEntry.append("Package destination: ").append(addPackageDetails.getElement(3)).append("\n");
            newEntry.append("Package ETA: ").append(addPackageDetails.getElement(4)).append("\n");
            newEntry.append("Assigned to: ").append(lastDriverId != null ? lastDriverId : "null").append("\n");
            newEntry.append("Vehicle Registration: ").append(lastVehicleReg != null ? lastVehicleReg : "null")
                    .append("\n");

            newEntry.append("Status: Pending\n");
            newEntry.append("----------\n");

            try (FileWriter writer = new FileWriter(packageStorage, true)) {
                writer.write(newEntry.toString());
                System.out.println("Package entry " + entryCount + " saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private boolean packageIdExists(String packageId) {
        File packageDetails = new File("LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

        if (!packageDetails.exists() || packageDetails.length() == 0) {
            return false; // nothing to check
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(packageDetails))) {
            String line;
            boolean insideEntry = false;
            boolean idMatch = false;
            String status = "";

            while ((line = reader.readLine()) != null) {
                String t = line.trim();
                if (t.isEmpty())
                    continue;

                if (t.startsWith("Entry ")) {
                    insideEntry = true;
                    idMatch = false;
                    status = "";
                    continue;
                }

                if (!insideEntry)
                    continue;

                if (t.startsWith("Package ID:")) {
                    String id = t.substring("Package ID:".length()).trim();
                    idMatch = id.equalsIgnoreCase(packageId);
                }

                if (t.startsWith("Status:")) {
                    status = t.substring("Status:".length()).trim();
                    if (idMatch && !status.equalsIgnoreCase("Deleted")) {
                        return true;
                    }
                    insideEntry = false;
                    idMatch = false;
                    status = "";
                }

                if (t.startsWith("----------")) {
                    if (idMatch && !status.equalsIgnoreCase("Deleted")) {
                        return true;
                    }
                    insideEntry = false;
                    idMatch = false;
                    status = "";
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading packageDetails.txt: " + e.getMessage());
        }
        return false;
    }

    /**
     * Automatically updates packageDetails.txt for all pending/in transit
     * deliveries
     * using the last updated driver (Last Update: true) from driverDetails.txt.
     * Sets Assigned to, Vehicle Registration, and Status as needed.
     */

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

    public void validityString(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) {
                userStringInput = s;
                trueFalse = true;
                break;
            } else {
                System.out.println("Invalid input. Please enter a non-empty string.");
            }
        }
    }

    public void validityDouble(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                if (s.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                double value = Double.parseDouble(s);
                if (value >= 0) {
                    userDoubleInput = value;
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Please enter a positive double.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double.");
            }
        }
    }

    public static void main(String[] args) {
        // System.out.println("hello world");
        DeliveryTracking deliveryTracking = new DeliveryTracking(50);
        deliveryTracking.selectMenuItem();
    }


}

// remember to add the assigned to and vehicle registration
