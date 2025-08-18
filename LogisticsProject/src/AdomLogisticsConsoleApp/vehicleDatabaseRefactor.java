package AdomLogisticsConsoleApp;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import AdomLogisticsConsoleApp.ObjectClass.InputValidity;
import AdomLogisticsConsoleApp.ObjectClass.TextEntryManager;
import AdomLogisticsConsoleApp.ObjectClass.TxtEntry;
import CustomDataStructures.CustomArrayList;

public class vehicleDatabaseRefactor {
    private final BannerElements bElements;
    private final BannerElements addVehicle;
    private final BannerElements searchBy;
    private final BannerElements searchByType;

    private final Scanner scanner = new Scanner(System.in);
    private final File txtStorageDir;
    private final File txtStorage;

    TextEntryManager textEntryManager;
    InputValidity iv = new InputValidity();
    private String userStringInput;
    private double low;
    private double high;

    public vehicleDatabaseRefactor(int width) {
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
                txtStorage.createNewFile();
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
                    continue;
                case 1:
                    addVehicle();
                    break;
                case 2:
                    removeVehicle();
                    break;
                case 3:
                    searchVehicle();
                    break;
                case 4:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown selection. Please try again.");
            }
        }
    }

    private void searchVehicle() {
        searchBy.printMenu();
        int choice = iv.validity(scanner, searchBy.size());
        switch (choice) {
            case 0:
                userStringInput = iv.validityString(scanner);
                textEntryManager.searchByField("Driver Id", userStringInput);
                break;
            case 1:
                userStringInput = iv.validityString(scanner);
                textEntryManager.searchByField("Registration Number", userStringInput);
                break;
            case 2:
                searchByType.printMenu();
                int userChoice = iv.validity(scanner, searchByType.size());
                String target = switch (userChoice) {
                    case 1 -> "truck";
                    case 2 -> "van";
                    default -> null;
                };
                textEntryManager.searchByField("Type", target);
                break;
            case 3:
                low = iv.validityDouble(scanner);
                high = iv.validityDouble(scanner);
                textEntryManager.searchByRange("Milage", low, high);
                break;
            case 4:
                low = iv.validityDouble(scanner);
                high = iv.validityDouble(scanner);
                textEntryManager.searchByRange("Fuel Usage", low, high);
                break;
            case 5:
                userStringInput = iv.validityString(scanner);
                textEntryManager.searchByField("Vehicle maintenance history", userStringInput);
        }
    }

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
        System.out.println("Registration number: " + registrationNumber);
        System.out.println("Vehicle type: " + type);
        System.out.println("Vehicle mileage: " + mileage);
        System.out.println("Vehicle fuel usage: " + fuel);
        System.out.println("Driver ID: " + driverId);

        saveTXT(registrationNumber, type, mileage, fuel, driverId);
    }

    public void saveTXT(String registrationNumber, String type, double mileage, double fuel, String driverId) {
        try {
            if (textEntryManager.entries == null) {
                textEntryManager.readAllEntries("Entry ");
            }
            
            String entryId = textEntryManager.nextEntryId();

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
            entry.rawLines.addElement("----------");

            textEntryManager.writeEntry(entry);
            System.out.println("Entry " + entryId + " saved successfully.");
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void removeVehicle() throws IOException {
        System.out.println("Please enter the registration number:");
        iv.validityString(scanner);
        CustomArrayList<Object> results = textEntryManager.searchByField("Registration Number", userStringInput);

        if (results.size() > 1) {
            searchByType.printMenu();
            int userChoice = iv.validity(scanner, searchByType.size());
            String target = switch (userChoice) {
                case 1 -> "truck";
                case 2 -> "van";
                default -> null;
            };
            results = textEntryManager.searchByField("Type", target);
        }

        if (results.size() > 1) {
            System.out.println("Please enter the driver ID: ");
            iv.validityString(scanner);
            results = textEntryManager.searchByField("Driver ID", userStringInput);
        }

        if (results.size() > 1) {
            System.out.println("Multiple entries still match. Refining by maintenance history...");
            results = textEntryManager.searchByField("Vehicle maintenance history", userStringInput);
        }

        if (results.size() > 1) {
            System.out.println("Refining by mileage range...");
            double low = iv.validityDouble(scanner);
            double high = iv.validityDouble(scanner);
            CustomArrayList<Object> range = textEntryManager.searchByRange("Mileage", low, high);
        }

        if (results.size() > 1) {
            System.out.println("Refining by fuel usage range...");
            double low = iv.validityDouble(scanner);
            double high = iv.validityDouble(scanner);
            CustomArrayList<Object> range = textEntryManager.searchByRange("Fuel Usage", low, high);
        }

        if (results.size() == 1) {
            String header = ((CustomArrayList<?>) results.getElement(0))
                    .getElement(0).toString();
            String entryId = header.replace("Entry ", "").trim();
            System.out.print("Confirm remove for Entry " + entryId + "? (yes/no): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("yes")) {
                try {
                    textEntryManager.markDeleted(entryId, "Status ");
                    System.out.println("Entry " + entryId + " deleted.");
                } catch (IOException e) {
                    System.out.println("Error updating file: " + e.getMessage());
                }
            } else {
                System.out.println("Remove cancelled.");
            }
        } else if (results.isEmpty()) {
            System.out.println("No matching vehicle found after refinement.");
        } else {
            System.out.println("Still multiple entries. Manual review required.");
        }
    }

    public boolean doesDriverExist(String driverID) {
        try {
            CustomArrayList<String> allLines = textEntryManager.readAllLines();
            for (int i = 0; i < allLines.size(); i++) {
                String line = allLines.getElement(i);
                if (line.startsWith("Entry ")) {
                    boolean idMatch = false;
                    String status = "Active";
                    while (i < allLines.size() - 1) {
                        line = allLines.getElement(++i);
                        if (line.startsWith("Driver ID:")) {
                            String id = line.substring("Driver ID:".length()).trim();
                            idMatch = id.equalsIgnoreCase(driverID);
                            continue;
                        }
                        if (line.startsWith("Status:")) {
                            status = line.substring("Status:".length()).trim();
                            if (idMatch && !status.equalsIgnoreCase("Deleted")) {
                                return true;
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading driverDetails.txt: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        vehicleDatabaseRefactor vdb = new vehicleDatabaseRefactor(50);
        vdb.selectMenuItem();
    }
}
