package AdomLogisticsConsoleApp;

import CustomDataStructures.CustomArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class VehicleDataBase {

    BannerElements bElements;
    BannerElements addVehicle;
    CustomArrayList<Object> addVehicleDetails;
    CustomArrayList<Object> addNewEntry;
    CustomArrayList<Object> results;
    BannerElements searchBy;
    BannerElements searchByType;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    String userStringInput;
    double userDoubleInput;
    File jsonStorageDir;
    File jsonStorage;

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
        // Ensure the JSON storage directory exists
        jsonStorageDir = new File("LogisticsProject/src/JSONDatabase").getAbsoluteFile();
        if (!jsonStorageDir.exists()) {
            jsonStorageDir.mkdirs();
        }

        // Use a relative path for the JSON storage file
        jsonStorage = new File(
                "LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile(); // Changed to relative path
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
            newEntry.append("          \"maintenanceHistory\": \"").append(vehicleDetails.getElement(5))
                    .append("\",\n");
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
        CustomArrayList<Object> searchResults = searchVehicleString("registrationNumber");

        if (searchResults.size() > 1) {
            CustomArrayList<Object> typeResults = searchVehicleType();
            CustomArrayList<Object> temp = new CustomArrayList<>();

            for (int i = 0; i < searchResults.size(); i++) {
                Object entry = searchResults.getElement(i);
                if (!(entry instanceof CustomArrayList))
                    continue;
                String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                for (int j = 0; j < typeResults.size(); j++) {
                    Object candidate = typeResults.getElement(j);
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

        if (searchResults.size() > 1) {
            CustomArrayList<Object> driverResults = searchVehicleString("driverID");
            CustomArrayList<Object> temp = new CustomArrayList<>();

            for (int i = 0; i < searchResults.size(); i++) {
                Object entry = searchResults.getElement(i);
                if (!(entry instanceof CustomArrayList))
                    continue;
                String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                for (int j = 0; j < driverResults.size(); j++) {
                    Object candidate = driverResults.getElement(j);
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
                updateJson(entryNumber);
            } else {
                System.out.println("Unexpected result format.");
            }
        } else if (searchResults.size() == 0) {
            System.out.println("No matching vehicle found.");
        } else {
            System.out.println("Multiple entries still match. Refining search...");

            // Step 1: maintenanceHistory
            CustomArrayList<Object> maintenanceResults = searchVehicleString("maintenanceHistory");
            CustomArrayList<Object> temp = new CustomArrayList<>();
            for (int i = 0; i < searchResults.size(); i++) {
                Object entry = searchResults.getElement(i);
                if (!(entry instanceof CustomArrayList))
                    continue;
                String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                for (int j = 0; j < maintenanceResults.size(); j++) {
                    Object candidate = maintenanceResults.getElement(j);
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

            // Step 2: mileage
            if (searchResults.size() > 1) {
                CustomArrayList<Object> mileageResults = searchVehicleDoubleRange("mileage");
                temp = new CustomArrayList<>();
                for (int i = 0; i < searchResults.size(); i++) {
                    Object entry = searchResults.getElement(i);
                    if (!(entry instanceof CustomArrayList))
                        continue;
                    String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                    for (int j = 0; j < mileageResults.size(); j++) {
                        Object candidate = mileageResults.getElement(j);
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

            // Step 3: fuelUsage
            if (searchResults.size() > 1) {
                CustomArrayList<Object> fuelResults = searchVehicleDoubleRange("fuelUsage");
                temp = new CustomArrayList<>();
                for (int i = 0; i < searchResults.size(); i++) {
                    Object entry = searchResults.getElement(i);
                    if (!(entry instanceof CustomArrayList))
                        continue;
                    String entryHeader = ((CustomArrayList<?>) entry).getElement(0).toString().trim();

                    for (int j = 0; j < fuelResults.size(); j++) {
                        Object candidate = fuelResults.getElement(j);
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

            // Final check
            if (searchResults.size() == 1) {
                Object entryObj = searchResults.getElement(0);
                if (entryObj instanceof CustomArrayList) {
                    CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                    String header = entry.getElement(0).toString();
                    String entryNumber = header.replace("Entry ", "").trim();
                    updateJson(entryNumber);
                } else {
                    System.out.println("Unexpected result format.");
                }
            } else if (searchResults.size() == 0) {
                System.out.println("No matching vehicle found after refinement.");
            } else {
                System.out.println("Still multiple entries. Manual review may be required.");
            }
        }
    }

    public void updateJson(String uniqueIdentifier) {
        // Ensure the JSON storage directory exists
        jsonStorageDir = new File("LogisticsProject/src/JSONDatabase").getAbsoluteFile();
        if (!jsonStorageDir.exists()) {
            jsonStorageDir.mkdirs();
        }

        jsonStorage = new File(
                "LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();
        StringBuilder fileContent = new StringBuilder();

        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(jsonStorage))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }

            String content = fileContent.toString();
            int entryIndex = content.indexOf(uniqueIdentifier);
            if (entryIndex != -1) {
                int statusIndex = content.indexOf("\"status\": \"!Deleted\"", entryIndex);
                if (statusIndex != -1) {
                    scanner = new Scanner(System.in);
                    System.out.print("Confirm remove for " + uniqueIdentifier + "? (yes/no): ");
                    String response = scanner.nextLine().trim().toLowerCase();
                    if (!response.equals("yes")) {
                        System.out.println("Remove cancelled.");
                        return;
                    }

                    content = content.substring(0, statusIndex) + "\"status\": \"Deleted\""
                            + content.substring(statusIndex + "\"status\": \"!Deleted\"".length());
                    try (FileWriter writer = new FileWriter(jsonStorage)) {
                        writer.write(content);
                        System.out.println("File updated successfully for" + uniqueIdentifier + ".");
                    } catch (IOException e) {
                        System.out.println("Error writing to file: " + e.getMessage());
                    }
                } else {
                    System.out.println("Status field not found near the identifier");
                }
            } else {
                System.out.println("Entry not found for " + uniqueIdentifier + ".");
            }

        } catch (Exception e) {
            System.out.println("Error rewriting file: " + e.getMessage());
        }
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
                    searchVehicleDoubleRange("mileage");
                    break;
                case 4:
                    System.out.println("Please enter the fuel usage as a range. Example: 10-20");
                    searchVehicleDoubleRange("fuelUsage");
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

    public CustomArrayList<Object> searchVehicleString(String searchString) {
        System.out.println("Enter :" + searchString);
        validityString(scanner); // Assume this validates input
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput;

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            System.out.println(
                    "Searching for vehicles with " + searchString + " matching \"" + userSearchInputString + "\"");

            jsonStorage = new File("LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(jsonStorage))) {
                String line;
                boolean insideEntry = false;
                String currentEntryNumber = null;
                CustomArrayList<String> entryLines = new CustomArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();

                    // Detect entry start
                    if (!insideEntry && trimmedLine.startsWith("\"Entry")) {
                        int start = trimmedLine.indexOf("Entry") + 6;
                        int end = trimmedLine.indexOf("\"", start);
                        currentEntryNumber = trimmedLine.substring(start, end);
                        insideEntry = true;
                        entryLines.clear();
                        entryLines.addElement(trimmedLine);
                        continue;
                    }

                    if (insideEntry) {
                        entryLines.addElement(trimmedLine);

                        // Detect end of entry block
                        if (trimmedLine.equals("]") || trimmedLine.equals("],")) {
                            String matchedFieldValue = null;
                            boolean isDeleted = false;

                            for (int i = 0; i < entryLines.size(); i++) {
                                String e = entryLines.getElement(i).trim();

                                int q1 = e.indexOf("\"");
                                int q2 = e.indexOf("\"", q1 + 1);
                                if (q1 != -1 && q2 != -1) {
                                    String fieldName = e.substring(q1 + 1, q2);

                                    // Check status
                                    if (fieldName.equals("status")) {
                                        int colonIndex = e.indexOf(":");
                                        if (colonIndex != -1) {
                                            String rawStatus = e.substring(colonIndex + 1).trim();
                                            if (rawStatus.endsWith(",")) {
                                                rawStatus = rawStatus.substring(0, rawStatus.length() - 1);
                                            }
                                            if (rawStatus.startsWith("\"") && rawStatus.endsWith("\"")) {
                                                rawStatus = rawStatus.substring(1, rawStatus.length() - 1);
                                            }
                                            if (rawStatus.equalsIgnoreCase("Deleted")) {
                                                isDeleted = true;
                                                break;
                                            }
                                        }
                                    }

                                    // Check target field
                                    if (fieldName.equals(searchString)) {
                                        int colonIndex = e.indexOf(":");
                                        if (colonIndex != -1) {
                                            String rawValue = e.substring(colonIndex + 1).trim();
                                            if (rawValue.endsWith(",")) {
                                                rawValue = rawValue.substring(0, rawValue.length() - 1);
                                            }
                                            if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
                                                rawValue = rawValue.substring(1, rawValue.length() - 1);
                                            }
                                            matchedFieldValue = rawValue;
                                        }
                                    }
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
                System.out.println("Error reading JSON database: " + e.getMessage());
            }
        }

        for (int index = 0; index < results.size(); index++) {
            Object entryObj = results.getElement(index);

            if (entryObj instanceof CustomArrayList) {
                CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                String header = entry.getElement(0).toString(); // "Entry 001"
                String entryNumber = header.replace("Entry ", "").trim();
                System.out.println("Entry number found: " + entryNumber);
            } else {
                System.out.println(entryObj); // fallback
            }
        }

        return results;
    }

    // Logic to search for a vehicle by type
    public CustomArrayList<Object> searchVehicleType() {
        searchByType.printMenu();
        validity(scanner, searchByType.size());
        boolean choice = trueFalse;
        int choiceInput = userInput;

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            String targetType = null;

            switch (choiceInput) {
                case 0:
                    System.out.println("Searching for all vehicle types...");
                    break;
                case 1:
                    System.out.println("Searching for trucks...");
                    targetType = "truck";
                    break;
                case 2:
                    System.out.println("Searching for vans...");
                    targetType = "van";
                    break;
                case 3:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    return results;
            }

            jsonStorage = new File("LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(jsonStorage))) {
                String line;
                boolean insideEntry = false;
                String currentEntryNumber = null;
                CustomArrayList<String> entryLines = new CustomArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();

                    if (!insideEntry && trimmedLine.startsWith("\"Entry")) {
                        int start = trimmedLine.indexOf("Entry") + 6;
                        int end = trimmedLine.indexOf("\"", start);
                        currentEntryNumber = trimmedLine.substring(start, end);
                        insideEntry = true;
                        entryLines.clear();
                        entryLines.addElement(trimmedLine);
                        continue;
                    }

                    if (insideEntry) {
                        entryLines.addElement(trimmedLine);

                        if (trimmedLine.equals("]") || trimmedLine.equals("],")) {
                            String matchedType = null;
                            boolean isDeleted = false;

                            for (int i = 0; i < entryLines.size(); i++) {
                                String e = entryLines.getElement(i).trim();

                                int q1 = e.indexOf("\"");
                                int q2 = e.indexOf("\"", q1 + 1);
                                if (q1 != -1 && q2 != -1) {
                                    String fieldName = e.substring(q1 + 1, q2);

                                    if (fieldName.equals("status")) {
                                        int colonIndex = e.indexOf(":");
                                        if (colonIndex != -1) {
                                            String rawStatus = e.substring(colonIndex + 1).trim();
                                            if (rawStatus.endsWith(",")) {
                                                rawStatus = rawStatus.substring(0, rawStatus.length() - 1);
                                            }
                                            if (rawStatus.startsWith("\"") && rawStatus.endsWith("\"")) {
                                                rawStatus = rawStatus.substring(1, rawStatus.length() - 1);
                                            }
                                            if (rawStatus.equalsIgnoreCase("Deleted")) {
                                                isDeleted = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (fieldName.equals("vehicleType")) {
                                        int colonIndex = e.indexOf(":");
                                        if (colonIndex != -1) {
                                            String rawValue = e.substring(colonIndex + 1).trim();
                                            if (rawValue.endsWith(",")) {
                                                rawValue = rawValue.substring(0, rawValue.length() - 1);
                                            }
                                            if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
                                                rawValue = rawValue.substring(1, rawValue.length() - 1);
                                            }
                                            matchedType = rawValue;
                                        }
                                    }
                                }
                            }

                            boolean match = !isDeleted && ((targetType == null) ||
                                    (matchedType != null && matchedType.equalsIgnoreCase(targetType)));

                            if (match) {
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
                System.out.println("Error reading JSON database: " + e.getMessage());
            }
        }

        return results;
    }

    public CustomArrayList<Object> searchVehicleDoubleRange(String searchString) {
        System.out.println("First input");
        validityDouble(scanner); // Assume this validates input
        double lowerBound = userDoubleInput; // First input
        System.out.println("Second input");
        validityDouble(scanner); // Assume this validates input
        double upperBound = userDoubleInput; // Second input

        if (lowerBound > upperBound) {
            double temp = lowerBound;
            lowerBound = upperBound;
            upperBound = temp;
        }

        System.out.println(
                "Searching for vehicles with " + searchString + " between " + lowerBound + " and " + upperBound);

        jsonStorage = new File("LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();
        CustomArrayList<Object> results = new CustomArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonStorage))) {
            String line;
            boolean insideEntry = false;
            String currentEntryNumber = null;
            CustomArrayList<String> entryLines = new CustomArrayList<>();

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (!insideEntry && trimmedLine.startsWith("\"Entry")) {
                    int start = trimmedLine.indexOf("Entry") + 6;
                    int end = trimmedLine.indexOf("\"", start);
                    currentEntryNumber = trimmedLine.substring(start, end);

                    insideEntry = true;
                    entryLines.clear();
                    entryLines.addElement(trimmedLine);
                    continue;
                }

                if (insideEntry) {
                    entryLines.addElement(trimmedLine);

                    if (trimmedLine.equals("]") || trimmedLine.equals("],")) {
                        String matchedFieldValue = null;
                        boolean isDeleted = false;

                        for (int i = 0; i < entryLines.size(); i++) {
                            String e = entryLines.getElement(i).trim();

                            int q1 = e.indexOf("\"");
                            int q2 = e.indexOf("\"", q1 + 1);
                            if (q1 != -1 && q2 != -1) {
                                String fieldName = e.substring(q1 + 1, q2);

                                if (fieldName.equals("status")) {
                                    int colonIndex = e.indexOf(":");
                                    if (colonIndex != -1) {
                                        String rawStatus = e.substring(colonIndex + 1).trim();
                                        if (rawStatus.endsWith(",")) {
                                            rawStatus = rawStatus.substring(0, rawStatus.length() - 1);
                                        }
                                        if (rawStatus.startsWith("\"") && rawStatus.endsWith("\"")) {
                                            rawStatus = rawStatus.substring(1, rawStatus.length() - 1);
                                        }
                                        if (rawStatus.equalsIgnoreCase("Deleted")) {
                                            isDeleted = true;
                                            break;
                                        }
                                    }
                                }

                                if (fieldName.equals(searchString)) {
                                    int colonIndex = e.indexOf(":");
                                    if (colonIndex != -1) {
                                        String rawValue = e.substring(colonIndex + 1).trim();
                                        if (rawValue.endsWith(",")) {
                                            rawValue = rawValue.substring(0, rawValue.length() - 1);
                                        }
                                        if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
                                            rawValue = rawValue.substring(1, rawValue.length() - 1);
                                        }
                                        matchedFieldValue = rawValue;
                                    }
                                }
                            }
                        }

                        if (!isDeleted && matchedFieldValue != null) {
                            try {
                                double value = Double.parseDouble(matchedFieldValue);
                                if (value >= lowerBound && value <= upperBound) {
                                    results.addElement(
                                            "Found \"" + searchString + "\": " + matchedFieldValue + " in Entry "
                                                    + currentEntryNumber);
                                }
                            } catch (NumberFormatException ex) {
                                // Skip non-numeric values
                            }
                        }

                        insideEntry = false;
                        currentEntryNumber = null;
                        entryLines.clear();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON database: " + e.getMessage());
        }

        return results;
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
