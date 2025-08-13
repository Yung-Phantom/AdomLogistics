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
import CustomDataStructures.DoublyLinkedList;

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

    String packageID;
    String origin;
    String destination;
    String ETA; // Keep as String for CLI/file simplicity (e.g., 2025-07-17 10:30)
    String assignedDriverID; // May be "" if not yet assigned
    String assignedVehicleID; // May be "" if not yet assigned
    String status; // "Pending", "In Transit", "Delivered"
    String userStringInput;
    double userDoubleInput;

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
                    "Searching for deliveries with "
                            + searchString
                            + " matching \""
                            + userSearchInputString
                            + "\"");

            File packageDetails = new File(
                    "LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

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
                        if (line.trim().equals("-----")) {
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

                // Handle a final block if the file doesn't end with "-----"
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
                System.out.println(
                        "Error reading packageDetails.txt: "
                                + e.getMessage());
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
                if (trimmed.equals("-----")) {
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

            // Handle case where file doesn't end with "-----"
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
            newEntry.append("Assigned to: \n");
            newEntry.append("Vehicle Registration: \n");
            newEntry.append("Status: Pending\n");
            newEntry.append("--------------------------------------------------\n");

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

                if (t.startsWith("-----")) {
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
    // }

    /**
     * DeliveryManager
     * - addDelivery(): collects user inputs (packageID, origin, destination, ETA),
     * tries to auto-assign driver/vehicle from DriverManager/VehicleManager,
     * persists to deliveries.txt via CustomArrayList.
     * - processDelivery(): loads deliveries into a DoublyLinkedList, consults
     * DriverManager
     * to update statuses (Pending -> In Transit; In Transit -> Delivered), writes
     * back to file.
     * - searchDelivery(): scans deliveries.txt via CustomArrayList for a packageID.
     * - viewAllDeliveries(): prints all deliveries from deliveries.txt via
     * CustomArrayList.
     *
     * File format (CSV):
     * packageID,origin,destination,ETA,driverID,vehicleReg,status
     *
     * Status lifecycle:
     * Pending -> (assigned in DriverManager) -> In Transit
     * In Transit -> (completed in DriverManager) -> Delivered
     */
    // public class DeliveryTracking1 {

    // ----
    // File path----
    // private static final String DELIVERIES_FILE = "deliveries.txt";

    // ----

    // External managers (inject real instances) ----

    // private final DriverAssignment driverAssignment;
    // private final VehicleDataBase vehicleDatabase;

    // ----
    // Data structures
    // you already
    // have implemented----Placeholders for
    // type references

    // public static class CustomArrayList<E> implements Iterable<E> {
    // Implemented by you
    // public void add(E e) {
    // }

    // public int size() {
    // return 0;
    // }

    // public E get(int idx) {
    // return null;
    // }

    // public java.util.Iterator<E> iterator() {
    // return null;
    // }
    // Optionally: remove, set, etc.
    // }

    // public static class DoublyLinkedList<E> implements Iterable<E> {
    // Implemented by you
    // public void addLast(E e) {
    // }

    // public int size() {
    // return 0;
    // }

    // public E get(int idx) {
    // return null;
    // }

    // public java.util.Iterator<E> iterator() {
    // return null;
    // }
    // Optionally: set, remove, etc.
    // }

    // ----

    // Domain model----

    // public static class Delivery {
    // String packageID;
    // String origin;
    // String destination;
    // String ETA; // Keep as String for CLI/file simplicity (e.g., 2025-07-17
    // 10:30)
    // String assignedDriverID; // May be "" if not yet assigned
    // String assignedVehicleID; // May be "" if not yet assigned
    // String status; // "Pending", "In Transit", "Delivered"

    // public Delivery() {
    // }

    // public Delivery(String packageID, String origin, String destination, String
    // ETA,
    // String assignedDriverID, String assignedVehicleID, String status) {
    // this.packageID = packageID;
    // this.origin = origin;
    // this.destination = destination;
    // this.ETA = ETA;
    // this.assignedDriverID = assignedDriverID;
    // this.assignedVehicleID = assignedVehicleID;
    // this.status = status;
    // }

    // public static Delivery fromCsv(String line) {
    // Expected 7 fields; tolerate commas inside by keeping simple spec (no
    // quoted
    // commas)
    // String[] p = line.split(",", -1);
    // if (p.length < 7) {
    // throw new IllegalArgumentException("Malformed delivery line: " + line);
    // }
    // return new Delivery(
    // p[0].trim(),
    // p[1].trim(),
    // p[2].trim(),
    // p[3].trim(),
    // p[4].trim(),
    // p[5].trim(),
    // p[6].trim());
    // }

    // public String toCsv() {
    // Ensure no commas in fields or pre-sanitize if needed
    // return String.join(",",
    // nz(packageID),
    // nz(origin),
    // nz(destination),
    // nz(ETA),
    // nz(assignedDriverID),
    // nz(assignedVehicleID),
    // nz(status));
    // }

    // private static String nz(String s) {
    // return s == null ? "" : s;
    // }
    // }

    // ----Constructor----

    // public DeliveryTracking1(DriverAssignment driverAssignment, VehicleDatabase
    // vehicleDatabase) {
    // this.driverAssignment = driverAssignment;
    // this.vehicleDatabase = vehicleDatabase;
    // }

    // ----

    // Public API----

    // /**
    // * Collects user inputs (packageID, origin, destination, ETA), auto-attempts
    // * assignment,
    // * validates, appends to deliveries.txt.
    // */
    // public void addDelivery(Scanner in) {
    // System.out.print("Enter Package ID: ");
    // String packageID = in.nextLine().trim();

    // Uniqueness check
    // if (existsPackageId(packageID)) {
    // System.out.println("[X] A delivery with this Package ID already exists.
    // Aborting.");
    // return;
    // }

    // System.out.print("Enter Origin: ");
    // String origin = in.nextLine().trim();

    // System.out.print("Enter Destination: ");
    // String destination = in.nextLine().trim();

    // System.out.print("Enter ETA (e.g., 2025-07-17 10:30): ");
    // String eta = in.nextLine().trim();

    // Attempt auto-assignment based on your driver/vehicle modules
    // String driverID = "";
    // String vehicleReg = "";

    // TODO: Replace with your real assignment policy, e.g., by
    // proximity/experience.
    // If your DriverManager already made a pending assignment for this package,
    // pull it here. Otherwise leave blank and keep status Pending.
    // AssignedPair pair = tryAutoAssign(packageID, origin, destination);
    // if (pair != null) {
    // driverID = pair.driverId;
    // vehicleReg = pair.vehicleReg;
    // }

    // If driver/vehicle are non-empty, validate they exist in your stores
    // if (!driverID.isEmpty() && !driverAssignment.driverExists(driverID)) {
    // System.out.println("[!] Warning: Assigned driver not found. Clearing
    // assignment.");
    // driverID = "";
    // }
    // if (!vehicleReg.isEmpty() && !vehicleDatabase.vehicleExists(vehicleReg)) {
    // System.out.println("[!] Warning: Assigned vehicle not found. Clearing
    // assignment.");
    // vehicleReg = "";
    // }

    // Delivery d = new Delivery(packageID, origin, destination, eta, driverID,
    // vehicleReg, "Pending");

    // Persist (append)
    // appendDeliveryToFile(d);

    // System.out.println("[✓] Delivery added as Pending.");
    // if (!driverID.isEmpty() && !vehicleReg.isEmpty()) {
    // System.out.println(" Assigned to Driver " + driverID + " | Vehicle " +
    // vehicleReg);
    // }
    // }

    // /**
    // * Walks all deliveries (doubly linked list), consults DriverManager for
    // * assignments and completion,
    // * updates statuses, and writes back to file.
    // */
    // public void processDelivery() {
    // DoublyLinkedList<Delivery> list = loadDeliveriesAsLinked();

    // Traverse and update statuses
    // for (Delivery d : list) {
    // If Pending and driver assigned in driver module -> In Transit
    // if ("Pending".equalsIgnoreCase(d.status)) {
    // String assignedDriver =
    // driverAssignment.findAssignedDriverForPackage(d.packageID);
    // String assignedVehicle =
    // driverAssignment.findAssignedVehicleForPackage(d.packageID);

    // if (assignedDriver != null && !assignedDriver.isEmpty()
    // && assignedVehicle != null && !assignedVehicle.isEmpty()) {

    // Validate still exist
    // if (driverAssignment.driverExists(assignedDriver)
    // && vehicleDatabase.vehicleExists(assignedVehicle)) {
    // d.assignedDriverID = assignedDriver;
    // d.assignedVehicleID = assignedVehicle;
    // d.status = "In Transit";
    // }
    // }
    // }

    // If In Transit and driver module marks completed -> Delivered
    // if ("In Transit".equalsIgnoreCase(d.status)) {
    // if (driverAssignment.isDeliveryCompleted(d.packageID)) {
    // d.status = "Delivered";
    // }
    // }
    // }

    // Write back

    // entire set (preserve order)
    // saveAllDeliveries(iterableToArray(list));
    // System.out.println("[✓] Processing complete. File updated.");
    // }

    // /**
    // * Finds and prints a delivery by packageID using CustomArrayList file scan.
    // */
    // public Delivery searchDelivery(String packageID) {
    // CustomArrayList<Delivery> arr = loadDeliveriesAsArray();
    // for (int i = 0; i < arr.size(); i++) {
    // Delivery d = arr.get(i);
    // if (d.packageID.equalsIgnoreCase(packageID)) {
    // printDelivery(d);
    // return d;
    // }
    // }
    // System.out.println("[X] No delivery found for Package ID: " + packageID);
    // return null;
    // }

    // /**
    // * Prints all deliveries using CustomArrayList file scan.
    // */
    // public void viewAllDeliveries() {
    // CustomArrayList<Delivery> arr = loadDeliveriesAsArray();
    // if (arr.size() == 0) {
    // System.out.println("(no deliveries)");
    // return;
    // }
    // System.out.println("=== Deliveries ===");
    // for (Delivery d : arr) {
    // printDelivery(d);
    // }
    // }

    // ----Helpers:assignment,validation,printing----

    // private void printDelivery(Delivery d) {
    // System.out.println("- Package: " + d.packageID);
    // System.out.println(" Origin: " + d.origin);
    // System.out.println(" Destination: " + d.destination);
    // System.out.println(" ETA: " + d.ETA);
    // System.out.println(" Driver: " + (empty(d.assignedDriverID) ? "(unassigned)"
    // : d.assignedDriverID));
    // System.out.println(" Vehicle: " + (empty(d.assignedVehicleID) ?
    // "(unassigned)" : d.assignedVehicleID));
    // System.out.println(" Status: " + d.status);
    // }

    // private boolean existsPackageId(String packageID) {
    // CustomArrayList<Delivery> arr = loadDeliveriesAsArray();
    // for (int i = 0; i < arr.size(); i++) {
    // if (packageID.equalsIgnoreCase(arr.get(i).packageID))
    // return true;
    // }
    // return false;
    // }

    // private static boolean empty(String s) {
    // return s == null || s.isEmpty();
    // }

    // Simple pair record for
    // assignment results

    // private static class AssignedPair {
    // final String driverId;
    // final String vehicleReg;

    // AssignedPair(String d, String v) {
    // this.driverId = d;
    // this.vehicleReg = v;
    // }
    // }

    // /**
    // * Try to auto-assign a driver and vehicle for a new delivery.
    // * Replace with your real policy: proximity, experience, vehicle availability,
    // * etc.
    // */
    // private AssignedPair tryAutoAssign(String packageID, String origin, String
    // destination) {
    // TODO: Replace with actual logic:
    // Example: Let DriverManager pick best driver; VehicleManager pick suitable
    // vehicle type.
    // String driver = driverAssignment.suggestDriverForRoute(origin, destination);
    // String vehicle = vehicleDatabase.suggestVehicleForRoute(origin, destination);
    // if (!empty(driver) && !empty(vehicle)) {
    // Optionally, record this assignment in DriverManager now
    // driverAssignment.assignDeliveryToDriver(packageID, driver, vehicle);
    // return new AssignedPair(driver, vehicle);
    // }
    // return null;
    // }

    // ----

    // File I/O----

    // private CustomArrayList<Delivery> loadDeliveriesAsArray() {
    // CustomArrayList<Delivery> arr = new CustomArrayList<>();
    // try (BufferedReader br = new BufferedReader(new FileReader(DELIVERIES_FILE)))
    // {
    // String line;
    // while ((line = br.readLine()) != null) {
    // line = line.trim();
    // if (line.isEmpty())
    // continue;
    // arr.add(Delivery.fromCsv(line));
    // }
    // } catch (IOException e) {
    // If file doesn't exist yet, treat as empty set
    // } catch (IllegalArgumentException bad) {
    // System.out.println("[!] Skipping malformed line: " + bad.getMessage());
    // }
    // return arr;
    // }

    // private DoublyLinkedList<Delivery> loadDeliveriesAsLinked() {
    // DoublyLinkedList<Delivery> list = new DoublyLinkedList<>();
    // try (BufferedReader br = new BufferedReader(new FileReader(DELIVERIES_FILE)))
    // {
    // String line;
    // while ((line = br.readLine()) != null) {
    // line = line.trim();
    // if (line.isEmpty())
    // continue;
    // list.addLast(Delivery.fromCsv(line));
    // }
    // } catch (IOException e) {
    // If file missing, nothing to process
    // } catch (IllegalArgumentException bad) {
    // System.out.println("[!] Skipping malformed line: " + bad.getMessage());
    // }
    // return list;
    // }

    // private void appendDeliveryToFile(Delivery d) {
    // try (BufferedWriter bw = new BufferedWriter(new FileWriter(DELIVERIES_FILE,
    // true))) {
    // bw.write(d.toCsv());
    // bw.newLine();
    // } catch (IOException e) {
    // System.out.println("[X] Failed to write delivery: " + e.getMessage());
    // }
    // }

    // private void saveAllDeliveries(Delivery[] all) {
    // try (BufferedWriter bw = new BufferedWriter(new FileWriter(DELIVERIES_FILE,
    // false))) {
    // for (Delivery d : all) {
    // bw.write(d.toCsv());
    // bw.newLine();
    // }
    // } catch (IOException e) {
    // System.out.println("[X] Failed to save deliveries: " + e.getMessage());
    // }
    // }

    // private Delivery[] iterableToArray(Iterable<Delivery> it) {
    // Collect into CustomArrayList first, then to array
    // CustomArrayList<Delivery> tmp = new CustomArrayList<>();
    // for (Delivery d : it)
    // tmp.add(d);
    // Delivery[] out = new Delivery[tmp.size()];
    // for (int i = 0; i < tmp.size(); i++)
    // out[i] = tmp.get(i);
    // return out;
    // }

    // ----

    // Interfaces your
    // other modules

    // should provide (stubbed for
    // compilation)
    // ----

    // public interface DriverManager {
    // boolean driverExists(String driverId);

    // String findAssignedDriverForPackage(String packageId); // returns driverId or
    // ""

    // String findAssignedVehicleForPackage(String packageId); // returns vehicleReg
    // or ""

    // boolean isDeliveryCompleted(String packageId);

    // Optional helpers for auto-assignment:
    // String suggestDriverForRoute(String origin, String destination);

    // void assignDeliveryToDriver(String packageId, String driverId, String
    // vehicleReg);
    // }

    // public interface VehicleManager {
    // boolean vehicleExists(String vehicleReg);

    // String suggestVehicleForRoute(String origin, String destination);
    // }
    // }

}