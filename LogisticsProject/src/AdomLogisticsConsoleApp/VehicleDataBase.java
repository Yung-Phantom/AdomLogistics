package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import CustomDataStructures.CustomArrayList;

public class VehicleDataBase {
    BannerElements bElements;
    BannerElements addVehicle;
    CustomArrayList<Object> addVehicleDetails;
    CustomArrayList<Object> addNewEntry;
    BannerElements searchBy;
    BannerElements searchByType;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    String userStringInput;
    double userDoubleInput;

    public VehicleDataBase(int width) {
        bElements = new BannerElements(width, "Adom Logistics Vehicle Database");
        bElements.printBanner();
        bElements.addToMenu("Add new vehicle");
        bElements.addToMenu("Remove vehicle");
        bElements.addToMenu("Search for vehicle");
        bElements.addToMenu("Exit");
        bElements.printMenu();

        addVehicle = new BannerElements(userInput, "Add new vehicle");
        addVehicle.addToMenu("Please enter the registration number.");
        addVehicle.addToMenu("Please enter the type of vehicle (e.g., Truck, Van).");
        addVehicle.addToMenu("Please enter the mileage of the vehicle.");
        addVehicle.addToMenu("Please enter the fuel usage of the vehicle.");
        addVehicle.addToMenu("Please enter the driver ID.");
        addVehicle.addToMenu("Please enter the maintenance history of the vehicle.");

        searchBy = new BannerElements(userInput, "Search for vehicles");
        searchBy.addToMenu("Search by registration number");
        searchBy.addToMenu("Search for vehicle by type");
        searchBy.addToMenu("Search for vehicle by mileage");
        searchBy.addToMenu("Search for vehicle by fuel usage");
        searchBy.addToMenu("Search for vehicle by driver ID");
        searchBy.addToMenu("Search for vehicle by maintenance history");
        searchBy.addToMenu("Exit");

        searchByType = new BannerElements(userInput, "Search for vehicles by type");
        searchByType.addToMenu("Search for truck");
        searchByType.addToMenu("Search for vans");
        searchByType.addToMenu("Exit");
    }

    public void addVehicle() {
        addVehicleDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();
        for (int i = 0; i < addVehicle.size(); i++) {
            System.out.println(addVehicle.getElement(i));
            switch (i) {
                case 0:
                case 4:
                case 5:
                    scanner.nextLine();
                    userStringInput = scanner.next();
                    addVehicleDetails.addElement(userStringInput);
                    break;
                case 1:
                    scanner.nextLine();
                    while (true) {
                        userStringInput = scanner.nextLine().trim();
                        if (userStringInput.equalsIgnoreCase("truck") || userStringInput.equalsIgnoreCase("van")) {
                            addVehicleDetails.addElement(userStringInput);
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter either 'truck' or 'van'.");
                        }
                    }
                    break;
                case 2:
                case 3:
                    userDoubleInput = scanner.nextDouble();
                    addVehicleDetails.addElement(userDoubleInput);
                    break;
                default:

                    break;
            }
        }
        System.out.println("Operation successful");
        for (int index = 0; index < addVehicleDetails.size(); index++) {
            if (index == 0) {
                addNewEntry.addElement("Registration number: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            } else if (index == 1) {
                addNewEntry.addElement("Vehicle type: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            } else if (index == 2) {
                addNewEntry.addElement("Vehicle milage: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            } else if (index == 3) {
                addNewEntry.addElement("Vehicle fuel usage: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            } else if (index == 4) {
                addNewEntry.addElement("Driver ID: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            } else if (index == 5) {
                addNewEntry.addElement("Vehicle maintenance history: " + addVehicleDetails.getElement(index));
                System.out.println(addNewEntry.getElement(index));
            }
        }
        saveJSON(addVehicleDetails);
    }

    public void saveJSON(CustomArrayList<Object> vehicleDetails) {
        File jsonStorage = new File(
                "C:/Users/Kotei Justice/Documents/AdomLogistics/LogisticsProject/src/JSONDatabase/jsonStorage.json");
        StringBuilder fileContent = new StringBuilder();
        int entryCount = 1;

        // check if file exists
        try {
            if (!jsonStorage.exists()) {
                jsonStorage.createNewFile();
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(jsonStorage))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line);
                    }

                    // count existing entries
                    String content = fileContent.toString();
                    int index = 0;
                    while ((index = content.indexOf("\"Entry", index)) != -1) {
                        entryCount++;
                        index = index + 6;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            // build new entry
            StringBuilder newEntry = new StringBuilder();
            newEntry.append("    {\n");
            newEntry.append("      \"Entry ").append(String.format("%03d", entryCount)).append("\": [\n");
            newEntry.append("        {\n");
            newEntry.append("          \"registrationNumber\": \"").append(vehicleDetails.getElement(0))
                    .append("\",\n");
            newEntry.append("          \"vehicleType\": \"").append(vehicleDetails.getElement(1)).append("\",\n");
            newEntry.append("          \"mileage\": ").append(vehicleDetails.getElement(2)).append(",\n");
            newEntry.append("          \"fuelUsage\": ").append(vehicleDetails.getElement(3)).append(",\n");
            newEntry.append("          \"driverID\": \"").append(vehicleDetails.getElement(4)).append("\",\n");
            newEntry.append("          \"maintenanceHistory\": \"").append(vehicleDetails.getElement(5)).append("\",\n");
            newEntry.append("          \"status\": \"").append("!Deleted").append("\"\n");
            newEntry.append("        }\n");
            newEntry.append("      ]\n");
            newEntry.append("    }");
            // Final JSON structure
            StringBuilder finalJSON = new StringBuilder();
            finalJSON.append("{\n");
            finalJSON.append("  \"Adom Logistics\": [\n");

            // If previous content exists, inject it
            if (fileContent.length() > 0) {
                int insertIndex = fileContent.lastIndexOf("]");
                String existingEntries = fileContent.substring(fileContent.indexOf("[") + 1, insertIndex).trim();
                if (!existingEntries.isEmpty()) {
                    finalJSON.append(existingEntries).append(",\n");
                }
            }

            finalJSON.append(newEntry).append("\n");
            finalJSON.append("  ]\n");
            finalJSON.append("}");

            // Write to file (overwrite)
            try (FileWriter writer = new FileWriter(jsonStorage)) {
                writer.write(finalJSON.toString());
                System.out.println("Entry " + entryCount + " saved successfully.");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public void removeVehicle() {
        // Logic to remove a vehicle
    }

    public void searchVehicle() {
        // Logic to search for a vehicle
        // String searchBy
        // String searchBy, String value
        // String searchBy, double value

        searchBy.printMenu();
        validity(scanner, searchBy.size());
        boolean choice = trueFalse;
        int choiceInput = userInput;
        if (choice) {
            switch (choiceInput) {
                case 0:
                    searchBy.printMenu();
                    selectMenuItem();
                    break;
                case 1:
                    System.out.println("Please enter the registration number.");
                    searchVehicleString("registrationNumber");
                    break;
                case 2:
                    searchVehicleType();
                    break;
                case 3:
                    System.out.println("Please enter the milage as a range. Example: 1000-2000");
                    searchVehicleDouble("mileage");
                    break;
                case 4:
                    System.out.println("Please enter the fuel usage as a range. Example: 10-20");
                    searchVehicleDouble("fuelUsage");
                    break;
                case 5:
                    System.out.println("Please enter the driver ID.");
                    searchVehicleString("driverID");
                    break;
                case 6:
                    System.out.println("Please enter the maintenance history.");
                    searchVehicleString("maintenanceHistory");
                    break;
                case 7:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;
            }
        }
    }

    // Logic to search for a vehicle by string
    public void searchVehicleString(String searchString) {
        validityString(scanner);
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput;
        if (choice) {
            // search algo
            System.out.println("Searching for vehicle with registration number: " + userSearchInputString);
            // Implement the search logic here

        }
    }

    // Logic to search for a vehicle by type
    public void searchVehicleType() {
        searchByType.printMenu();
        validity(scanner, searchByType.size());
        boolean choice = trueFalse;
        int choiceInput = userInput;
        if (choice) {
            switch (choiceInput) {
                case 0:
                    // search algo
                    break;
                case 1:
                    System.out.println("Searching for trucks...");
                    // search algo
                    break;
                case 2:
                    System.out.println("Searching for vans...");
                    // search algo
                    break;
                case 3:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    break;
            }
        }
    }

    // Logic to search for a vehicle by double
    public void searchVehicleDouble(String searchString) {
        validityDouble(scanner);
        boolean choice = trueFalse;
        double userSearchInputDouble = userDoubleInput;
        if (choice) {
            // search algo
            System.out.println("Searching for vehicle with mileage: " + userSearchInputDouble);
            // Implement the search logic here

        }
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
                    break;

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
        VehicleDataBase vehicleDB = new VehicleDataBase(50);
        vehicleDB.selectMenuItem();
    }
}
// 1. Vehicle Database
// o Use a tree or hash table to organize vehicles by type or mileage.
