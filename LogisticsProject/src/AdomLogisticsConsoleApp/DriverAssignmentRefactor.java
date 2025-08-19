package AdomLogisticsConsoleApp;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import AdomLogisticsConsoleApp.ObjectClass.InputValidity;
import AdomLogisticsConsoleApp.ObjectClass.TextEntryManager;
import AdomLogisticsConsoleApp.ObjectClass.TxtEntry;
import CustomDataStructures.CustomArrayList;

public class DriverAssignmentRefactor {
    BannerElements bElements;
    BannerElements addDriver;
    BannerElements searchByElements;
    BannerElements updateDriver;

    private final Scanner scanner = new Scanner(System.in);
    private final File txtStorageDir;
    private final File txtStorage;
    TextEntryManager textEntryManager;
    InputValidity iv = new InputValidity();
    private String userStringInput;
    private double low;
    private double high;

    public DriverAssignmentRefactor(int width) {
        bElements = new BannerElements(width, "Adom Logistics Driver Management");
        bElements.printBanner();
        bElements.addToMenu("Add new driver");
        bElements.addToMenu("Remove driver");
        bElements.addToMenu("Update driver activity");
        bElements.addToMenu("Assign driver");
        bElements.addToMenu("View driver activity");
        bElements.addToMenu("Exit");
        bElements.printMenu();

        addDriver = new BannerElements(width, "Add new driver");
        addDriver.addToMenu("Please enter the driver's ID.");
        addDriver.addToMenu("Please enter the driver's name.");
        addDriver.addToMenu("Please enter the driver's license number.");
        addDriver.addToMenu("Please enter the driver's phone number.");
        addDriver.addToMenu("Please enter the driver's email address.");
        addDriver.addToMenu("Please enter the driver's address.");
        addDriver.addToMenu("Please enter the driver's proximity.");

        updateDriver = new BannerElements(width, "Update Driver Activity");
        updateDriver.addToMenu("Please enter the driver's ID.");
        updateDriver.addToMenu("Please enter the new route ID.");
        updateDriver.addToMenu("Was there a delay? (yes/no)");
        updateDriver.addToMenu("Please enter any infraction notes.(leave blank if none):");

        txtStorageDir = new File("LogisticsProject/src/finalTxtDatabase");
        txtStorage = new File("LogisticsProject/src/finalTxtDatabase/drivers.txt");

        try {
            if (!txtStorageDir.exists() || !txtStorage.exists()) {
                txtStorageDir.mkdirs();
                if (!txtStorage.exists()) {
                    txtStorage.createNewFile();
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating file: " + e.getMessage());
        }

        textEntryManager = new TextEntryManager(txtStorageDir.getPath(), txtStorage.getPath(), "Entry ");

    }

    public void selectMenuItem() throws IOException {
        while (true) {
            int choice = iv.validity(scanner, bElements.size());
            switch (choice) {
                case 0:
                    bElements.printMenu();
                    // Continue to the next iteration of the loop
                    continue;
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
                    viewDriverActivity();
                    break;
                case 6:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    // Handle invalid or unknown input
                    System.out.println("Unknown selection. Please try again.");
            }
        }
    }

    private void addNewDriver() {
        String driverId;
        while (true) {
            driverId = iv.readNonEmptyLine(scanner, addDriver.getElement(0));
            if (!textEntryManager.doesDriverExist(driverId))
                break;
            System.out.println("Driver ID \"" + driverId + "\" already exists. Please choose a unique ID.");
        }

        String driverName = iv.readNonEmptyLine(scanner, addDriver.getElement(1));
        String driverLicense = iv.readNonEmptyLine(scanner, addDriver.getElement(2));
        System.out.println(addDriver.getElement(3));
        String driverPhone = iv.validityPhoneString(scanner);
        String driverEmail = iv.readNonEmptyLine(scanner, addDriver.getElement(4));
        String driverAddress = iv.readNonEmptyLine(scanner, addDriver.getElement(5));
        double driverProximity = iv.readPositiveDouble(scanner, addDriver.getElement(6));

        System.out.println("Operation successful");
        System.out.println("Driver ID: " + driverId);
        System.out.println("Driver Name: " + driverName);
        System.out.println("Driver's license: " + driverLicense);
        System.out.println("Driver's phone number: " + driverPhone);
        System.out.println("Driver's email address: " + driverEmail);
        System.out.println("Driver's address: " + driverAddress);
        System.out.println("Driver's proximity: " + driverProximity);

        saveTXT(driverId, driverName, driverLicense, driverPhone, driverEmail, driverAddress, driverProximity);
    }

    public void saveTXT(String driverId, String driverName, String driverLicense, String driverPhone,
            String driverEmail, String driverAddress, double driverProximity) {
        try {
            // Always ensure entries list exists
            if (textEntryManager.entries == null) {
                textEntryManager.readAllEntries("Entry ");
            }

            // Generate a unique identifier for the new entry
            String entryId = textEntryManager.nextEntryId();

            // Create a new entry based on the user's input
            TxtEntry entry = new TxtEntry(entryId);
            entry.rawLines.addElement("Entry " + entryId);
            entry.rawLines.addElement("Driver ID: " + driverId);
            entry.rawLines.addElement("Driver Name: " + driverName);
            entry.rawLines.addElement("Driver License: " + driverLicense);
            entry.rawLines.addElement("Driver Phone Number: " + driverPhone);
            entry.rawLines.addElement("Driver Email Address: " + driverEmail);
            entry.rawLines.addElement("Driver Address: " + driverAddress);
            entry.rawLines.addElement("Driver Proximity: " + driverProximity);
            entry.rawLines.addElement("Assigned: false");
            entry.rawLines.addElement("Assigned Routes: ");
            entry.rawLines.addElement("Status: Active");
            entry.rawLines.addElement("Last Update: false");

            // Save the new entry to the text file
            textEntryManager.writeEntry(entry);
            textEntryManager.readAllEntries("Entry "); // refresh entries from file

            // Print success message
            System.out.println("Entry " + entryId + " saved successfully.\n");

        } catch (Exception e) {
            // Handle any exceptions that might occur
            e.printStackTrace(); // full trace for debugging
            System.out.println("Error writing to file: " + e.getMessage() + "\n");
        }
    }

    public void removeDriver() throws IOException {
        System.out.println("Please enter the driver ID:");
        userStringInput = iv.validityString(scanner);
        CustomArrayList<Object> results = textEntryManager.searchByField("Driver ID", userStringInput);

        if (results.size() > 1) {
            System.out.println("Please enter the driver's name: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver Name", userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() > 1) {
            System.out.println("Please enter the driver's license: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver License", userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() > 1) {
            System.out.println("Please enter the driver's phone number: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver Phone Number",
                    userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() > 1) {
            System.out.println("Please enter the driver's email address: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver Email Address",
                    userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() > 1) {
            System.out.println("Please enter the driver's address: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver Address",
                    userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() > 1) {
            System.out.println("Please enter the driver's proximity: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverByName = textEntryManager.searchByField("Driver Proximity",
                    userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverByName);
        }
        if (results.size() == 1) {
            String header = ((CustomArrayList<?>) results.getElement(0)).getElement(0).toString();
            String entryId = header.replace("Entry ", "").trim();
            System.out.print("Confirm remove for Entry " + entryId + "? (yes/no): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("yes")) {
                try {
                    textEntryManager.markDeleted(entryId, "Status");
                    System.out.println("Entry " + entryId + " deleted.");
                } catch (IOException e) {
                    System.out.println("Error updating file: " + e.getMessage());
                }
            } else {
                System.out.println("Remove cancelled.\n");
            }
        } else if (results.isEmpty()) {
            System.out.println("No matching vehicle found after refinement.\n");
        } else {
            System.out.println("Still multiple entries. Manual review required.\n");
        }
    }
}