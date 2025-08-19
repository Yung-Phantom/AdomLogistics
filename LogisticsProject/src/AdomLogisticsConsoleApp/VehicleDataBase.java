package AdomLogisticsConsoleApp;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import AdomLogisticsConsoleApp.ObjectClass.InputValidity;
import AdomLogisticsConsoleApp.ObjectClass.TextEntryManager;
import AdomLogisticsConsoleApp.ObjectClass.TxtEntry;
import CustomDataStructures.CustomArrayList;

public class VehicleDataBase {
    /**
     * Banner elements for the main menu.
     */
    private final BannerElements bElements;
    /**
     * Banner elements for adding a new vehicle.
     */
    private final BannerElements addVehicle;
    /**
     * Banner elements for searching for vehicles.
     */
    private final BannerElements searchBy;
    /**
     * Banner elements for searching for vehicles by type.
     */
    private final BannerElements searchByType;

    /**
     * A scanner for reading user input.
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * The directory where the text storage file is located.
     */
    private final File txtStorageDir;
    /**
     * The text storage file.
     */
    private final File txtStorage;

    /**
     * A text entry manager for adding new vehicles.
     */
    TextEntryManager textEntryManager;
    /**
     * A class for validating user input.
     */
    InputValidity iv = new InputValidity();
    /**
     * User's string input.
     */
    private String userStringInput;
    /**
     * Low end of a range for numerical input.
     */
    private double low;
    /**
     * High end of a range for numerical input.
     */
    private double high;

    public VehicleDataBase(int width) {
        bElements = new BannerElements(width, "Adom Logistics Vehicle Database");
        bElements.printBanner();

        bElements.addToMenu("Add vehicle");
        bElements.addToMenu("Remove vehicle");
        bElements.addToMenu("Search for vehicle");
        bElements.addToMenu("Exit");
        bElements.printMenu();

        addVehicle = new BannerElements(width, "Add vehicle");
        addVehicle.addToMenu("Please enter the driver ID.");
        addVehicle.addToMenu("Please enter the registration number.");
        addVehicle.addToMenu("Please enter the type of vehicle (e.g., Truck, Van).");
        addVehicle.addToMenu("Please enter the mileage of the vehicle.");
        addVehicle.addToMenu("Please enter the fuel usage of the vehicle.");

        searchBy = new BannerElements(width, "Search for vehicles");
        searchBy.addToMenu("Search for vehicle by driver ID");
        searchBy.addToMenu("Search by registration number");
        searchBy.addToMenu("Search for vehicle by type");
        searchBy.addToMenu("Search for vehicle by mileage");
        searchBy.addToMenu("Search for vehicle by fuel usage");
        searchBy.addToMenu("Search for vehicle by maintenance history");
        searchBy.addToMenu("Exit");

        searchByType = new BannerElements(width, "Search for vehicles by type");
        searchByType.addToMenu("Search for truck");
        searchByType.addToMenu("Search for vans");
        searchByType.addToMenu("Exit");

        txtStorageDir = new File("LogisticsProject/src/finalTxtDatabase");
        txtStorage = new File("LogisticsProject/src/finalTxtDatabase/vehicles.txt");
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

    /**
     * Main menu selection logic. Handles user input for menu navigation.
     * <p>
     * The user is presented with a menu and can select one of the following options:
     * <ul>
     * <li>Add a new vehicle</li>
     * <li>Remove a vehicle</li>
     * <li>Search for a vehicle</li>
     * <li>Exit the program</li>
     * </ul>
     * </p>
     * @throws IOException If the user selects to exit and close the scanner.
     */
    public void selectMenuItem() throws IOException {
        while (true) {
            int choice = iv.validity(scanner, bElements.size());
            switch (choice) {
                case 0:
                    bElements.printMenu();
                    // Continue to the next iteration of the loop
                    continue;
                case 1:
                    // Add a new vehicle
                    addVehicle();
                    break;
                case 2:
                    // Remove a vehicle
                    removeVehicle();
                    break;
                case 3:
                    // Search for a vehicle
                    searchVehicle();
                    break;
                case 4:
                    // Exit the program
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    // Handle invalid or unknown input
                    System.out.println("Unknown selection. Please try again.");
            }
        }
    }

    /**
     * Handle the search vehicle menu option.
     * <p>
     * The user is presented with a menu to select a search criteria.
     * </p>
     * <p>
     * After selecting a search criteria, the user is prompted to enter a value for the criteria.
     * The program will then search for matching vehicles and display the results if found.
     * </p>
     */
    private void searchVehicle() {
        while (true) {
            searchBy.printMenu();
            int choice = iv.validity(scanner, searchBy.size());
            if (choice == 0)
                continue;

            CustomArrayList<Object> results = new CustomArrayList<>();

            switch (choice) {
                case 1: {
                    // Search by driver ID
                    System.out.println(addVehicle.getElement(0));
                    userStringInput = iv.validityString(scanner);
                    results = textEntryManager.searchByField("Driver ID", userStringInput);
                    break;
                }
                case 2: {
                    // Search by registration number
                    System.out.println(addVehicle.getElement(1));
                    userStringInput = iv.validityString(scanner);
                    results = textEntryManager.searchByField("Registration Number", userStringInput);
                    break;
                }
                case 3: {
                    // Search by type
                    searchByType.printMenu();
                    System.out.println(addVehicle.getElement(2));
                    int userChoice = iv.validity(scanner, searchByType.size());
                    String target = switch (userChoice) {
                        case 1 -> "truck";
                        case 2 -> "van";
                        default -> null;
                    };
                    if (target == null) {
                        System.out.println("Invalid type selection.");
                        continue;
                    }
                    results = textEntryManager.searchByField("Type", target);
                    break;
                }
                case 4: {
                    // Search by mileage range
                    System.out.println(addVehicle.getElement(3));
                    System.out.println("First mileage: ");
                    low = iv.validityDouble(scanner);
                    System.out.println("Second mileage: ");
                    high = iv.validityDouble(scanner);
                    results = textEntryManager.searchByRange("Mileage", low, high);
                    break;
                }
                case 5: {
                    // Search by fuel usage range
                    System.out.println(addVehicle.getElement(4));
                    System.out.println("First fuel usage amount: ");
                    low = iv.validityDouble(scanner);
                    System.out.println("Second fuel usage amount: ");
                    high = iv.validityDouble(scanner);
                    results = textEntryManager.searchByRange("Fuel Usage", low, high);
                    break;
                }
                case 6: {
                    // Search by maintenance history
                    System.out.println("Please enter the maintenance history to search by.");
                    userStringInput = iv.validityString(scanner);
                    if (!"null".equalsIgnoreCase(userStringInput) && !userStringInput.isEmpty()) {
                        results = textEntryManager.searchByField("Vehicle maintenance history", userStringInput);
                    }
                    break;
                }
                case 7: {
                    // Exit
                    System.out.println("Exiting Adom Logistics Vehicle Database. Goodbye!\n");
                    return;
                }
                default:
                    System.out.println("Unknown selection. Please try again.");
                    continue;
            }

            if (results.isEmpty()) {
                System.out.println("No matching vehicles found.\n");
            } else {
                System.out.println("Matches found: " + results.size() + "\n");
                textEntryManager.printEntryNumbers(results);
            }
        }
    }

    /**
     * Handles adding a new vehicle.
     * <p>
     * The user is prompted to enter the driver ID, registration number, type, mileage, and fuel usage of the vehicle.
     * </p>
     * <p>
     * The user is asked to enter the driver ID first, then the registration number, type, mileage, and fuel usage.
     * The user is asked to enter the driver ID until a valid one is entered.
     * The user is asked to enter the registration number until a valid one is entered.
     * The user is asked to enter the type until a valid one is entered.
     * The user is asked to enter the mileage until a valid one is entered.
     * The user is asked to enter the fuel usage until a valid one is entered.
     * </p>
     */
    public void addVehicle() {
        String driverId;
        while (true) {
            driverId = iv.readNonEmptyLine(scanner, addVehicle.getElement(0));
            if (doesDriverExist(driverId))
                break;
            System.out.println("Driver ID \"" + driverId + "\" not found.");
        }
        String registrationNumber = iv.readNonEmptyLine(scanner, addVehicle.getElement(1));
        String type;
        while (true) {
            String raw = iv.readNonEmptyLine(scanner, addVehicle.getElement(2)).toLowerCase();
            if (raw.equals("truck") || raw.equals("van")) {
                type = raw;
                break;
            }
            System.out.println("Invalid input. Please enter either 'truck' or 'van'.");
        }
        double mileage = iv.readPositiveDouble(scanner, addVehicle.getElement(3));
        double fuel = iv.readPositiveDouble(scanner, addVehicle.getElement(4));

        System.out.println("Operation successful");
        System.out.println("Driver ID: " + driverId);
        System.out.println("Registration number: " + registrationNumber);
        System.out.println("Vehicle type: " + type);
        System.out.println("Vehicle mileage: " + mileage);
        System.out.println("Vehicle fuel usage: " + fuel);

        // Save the new vehicle to the text file
        saveTXT(registrationNumber, type, mileage, fuel, driverId);
    }

    /**
     * Persists a new vehicle entry to the text file.
     * <p>
     * The method first checks if the entries list exists. If it doesn't, it reads all entries from the file.
     * Then it generates a unique identifier for the new entry.
     * It creates a new entry based on the user's input and saves it to the text file.
     * Finally, it refreshes the entries list from the file and prints a success message.
     * </p>
     * <p>
     * If any exceptions occur during the process, the method catches them, prints the stack trace for debugging,
     * and prints an error message.
     * </p>
     *
     * @param registrationNumber the vehicle's registration number
     * @param type the vehicle's type (truck or van)
     * @param mileage the vehicle's mileage
     * @param fuel the vehicle's fuel usage
     * @param driverId the driver's ID
     */
    public void saveTXT(String registrationNumber, String type, double mileage, double fuel, String driverId) {
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
            entry.rawLines.addElement("Registration Number: " + registrationNumber);
            entry.rawLines.addElement("Type: " + type);
            entry.rawLines.addElement("Mileage: " + mileage);
            entry.rawLines.addElement("Fuel Usage: " + fuel);
            entry.rawLines.addElement("Vehicle maintenance history: null");
            entry.rawLines.addElement("Maintenance schedule: null");
            entry.rawLines.addElement("Status: Active");

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

    /**
     * Removes a vehicle from the database.
     * <p>
     * The user is first prompted to enter the registration number of the vehicle to be removed.
     * </p>
     * <p>
     * The user is then asked to enter the driver ID, registration number, type, mileage, and fuel usage of the vehicle.
     * The user is asked to enter the driver ID until a valid one is entered.
     * The user is asked to enter the registration number until a valid one is entered.
     * The user is asked to enter the type until a valid one is entered.
     * The user is asked to enter the mileage until a valid one is entered.
     * The user is asked to enter the fuel usage until a valid one is entered.
     * </p>
     * <p>
     * If the vehicle is found, the user is asked to confirm the removal.
     * </p>
     * <p>
     * If the user confirms, the vehicle is removed from the database.
     * </p>
     */
    /**
     * This method removes a vehicle from the database based on the user's input.
     * The method first prompts the user to enter the registration number of the vehicle.
     * It then asks the user to enter the driver ID, registration number, type, mileage, and fuel usage of the vehicle.
     * It then asks the user to enter the driver ID until a valid one is entered.
     * It then asks the user to enter the registration number until a valid one is entered.
     * It then asks the user to enter the type until a valid one is entered.
     * It then asks the user to enter the mileage until a valid one is entered.
     * It then asks the user to enter the fuel usage until a valid one is entered.
     * If the vehicle is found, the user is asked to confirm the removal.
     * If the user confirms, the vehicle is removed from the database.
     * @throws IOException if an error occurs while reading or writing to the file
     */
    public void removeVehicle() throws IOException {
        // Prompt the user to enter the registration number of the vehicle to be removed.
        System.out.println("Please enter the registration number:");
        userStringInput = iv.validityString(scanner);
        CustomArrayList<Object> results = textEntryManager.searchByField("Registration Number", userStringInput);

        // Refine the search by type if there are multiple results.
        if (results.size() > 1) {
            searchByType.printMenu();
            int userChoice = iv.validity(scanner, searchByType.size());
            String target = switch (userChoice) {
                case 1 -> "truck";
                case 2 -> "van";
                default -> null;
            };
            if (target != null) {
                CustomArrayList<Object> typeCandidates = textEntryManager.searchByField("Type", target);
                results = textEntryManager.refineByHeaderMatch(results, typeCandidates);
            }
        }

        // Refine the search by driver ID if there are multiple results.
        if (results.size() > 1) {
            System.out.println("Please enter the driver ID: ");
            userStringInput = iv.validityString(scanner);
            CustomArrayList<Object> driverCandidates = textEntryManager.searchByField("Driver ID", userStringInput);
            results = textEntryManager.refineByHeaderMatch(results, driverCandidates);
        }

        // Refine the search by maintenance history if there are multiple results.
        if (results.size() > 1) {
            System.out.println("Multiple entries still match. Refining by maintenance history...");
            System.out.println("Please enter the maintenance history to search by.");
            userStringInput = iv.validityString(scanner);

            // Block "null" from producing matches
            if (!"null".equalsIgnoreCase(userStringInput)) {
                CustomArrayList<Object> maintenanceCandidates = textEntryManager
                        .searchByField("Vehicle maintenance history", userStringInput);
                results = textEntryManager.refineByHeaderMatch(results, maintenanceCandidates);
            } else {
                System.out.println("Skipping 'null' maintenance history refinement.");
            }
        }

        // Refine the search by mileage range if there are multiple results.
        if (results.size() > 1) {
            System.out.println("Refining by mileage range...");
            double low = iv.validityDouble(scanner);
            double high = iv.validityDouble(scanner);
            CustomArrayList<Object> range = textEntryManager.searchByRange("Mileage", low, high);
            results = textEntryManager.refineByHeaderMatch(results, range);
        }

        // Refine the search by fuel usage range if there are multiple results.
        if (results.size() > 1) {
            System.out.println("Refining by fuel usage range...");
            double low = iv.validityDouble(scanner);
            double high = iv.validityDouble(scanner);
            CustomArrayList<Object> range = textEntryManager.searchByRange("Fuel Usage", low, high);
            results = textEntryManager.refineByHeaderMatch(results, range);
        }

        // If the vehicle is found, ask the user to confirm the removal.
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

    /**
     * Main entry point for the Adom Logistics Vehicle Database system.
     * 
     * @param args Command line arguments (not used)
     * @throws IOException If an I/O error occurs while reading or writing to a file
     */
    public static void main(String[] args) throws IOException {
        VehicleDataBase vdb = new VehicleDataBase(50);
        vdb.selectMenuItem();
    }

}
