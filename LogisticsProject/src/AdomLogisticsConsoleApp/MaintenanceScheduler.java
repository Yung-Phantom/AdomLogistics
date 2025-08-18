package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import CustomDataStructures.CustomArrayList;

public class MaintenanceScheduler {

    BannerElements bElements;
    BannerElements searchByElements;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    String userStringInput;
    File jsonStorage;
    File jsonStorageDir;

    public MaintenanceScheduler(int width) {
        bElements = new BannerElements(width, "Adom Logistics Vehicle Database");
        bElements.printBanner();
        bElements.addToMenu("Schedule maintenance");
        bElements.addToMenu("Process maintenance");
        bElements.addToMenu("Search scheduled maintenance");
        bElements.addToMenu("Exit");
        bElements.printMenu();
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
                    scheduleMaintenance();
                    break;
                case 2:
                    processMaintenance();
                    break;
                case 3:
                    searchMaintenance();
                    break;
                case 4:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;

            }
        }
    }

    private void scheduleMaintenance() {
        System.out.println("Please enter the vehicle registration number.");
        validityString(scanner); // validates input and sets trueFalse + userStringInput
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput == null ? "" : userStringInput.trim();

        if (choice) {
            // Check if the vehicle exists in the JSON storage
            File jsonStorage = new File("LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();
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
                int entryIndex = content.indexOf("\"registrationNumber\": \"" + userSearchInputString + "\"");
                if (entryIndex != -1) {
                    int maintenanceScheduleIndex = content.indexOf("\"maintenanceSchedule\": \"null\"", entryIndex);
                    if (maintenanceScheduleIndex != -1) {
                        System.out.println("Vehicle found.");
                        System.out.println("Please enter the new maintenance schedule.");
                        validityString(scanner);
                        String newMaintenanceSchedule = userStringInput;

                        content = content.substring(0, maintenanceScheduleIndex) + "\"maintenanceSchedule\": \""
                                + newMaintenanceSchedule + "\"" + content.substring(
                                        maintenanceScheduleIndex + "\"maintenanceSchedule\": \"null\"".length());
                        try (FileWriter writer = new FileWriter(jsonStorage)) {
                            writer.write(content);
                            System.out.println("File updated successfully for" + userSearchInputString + ".");
                        } catch (IOException e) {
                            System.out.println("Error writing to file: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Maintenance schedule already exists and is unprocessed.");
                    }
                } else {
                    System.out.println("Entry not found for " + userSearchInputString + ".");
                }

            } catch (Exception e) {
                System.out.println("Error rewriting file: " + e.getMessage());
            }
        }
    }

    private void processMaintenance() {
        System.out.println("Please enter the vehicle registration number.");
        validityString(scanner); // validates input and sets trueFalse + userStringInput
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput == null ? "" : userStringInput.trim();

        if (choice) {
            // Check if the vehicle exists in the JSON storage
            File jsonStorage = new File("LogisticsProject/src/JSONDatabase/jsonStorage.json").getAbsoluteFile();
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
                int entryIndex = content.indexOf("\"registrationNumber\": \"" + userSearchInputString + "\"");
                if (entryIndex != -1) {
                    int maintenanceHistoryIndex = content.indexOf("\"maintenanceHistory\": \"", entryIndex);
                    if (maintenanceHistoryIndex != -1) {
                        int maintenanceScheduleIndex = content.indexOf("\"maintenanceSchedule\": \"", entryIndex);
                        if (maintenanceScheduleIndex != -1) {
                            int maintenanceScheduleEndIndex = content.indexOf("\"",
                                    maintenanceScheduleIndex + "\"maintenanceSchedule\": \"".length());
                            String maintenanceSchedule = content.substring(
                                    maintenanceScheduleIndex + "\"maintenanceSchedule\": \"".length(),
                                    maintenanceScheduleEndIndex);
                            if (!maintenanceSchedule.equals("null")) {
                                System.out.println("Vehicle found.");
                                System.out.print(
                                        "Confirm process maintenance for " + userSearchInputString + "? (yes/no): ");
                                validityString(scanner);
                                String response = userStringInput;

                                if (response.equalsIgnoreCase("yes")) {
                                    // Update maintenance history
                                    StringBuilder updatedContent = new StringBuilder(
                                            content.substring(0, maintenanceHistoryIndex))
                                            .append("\"maintenanceHistory\": \"").append(maintenanceSchedule)
                                            .append(content.substring(
                                                    maintenanceHistoryIndex + "\"maintenanceHistory\": \"".length()
                                                            + maintenanceSchedule.length(),
                                                    maintenanceScheduleIndex))
                                            .append("\"maintenanceSchedule\": \"null") // remove the extra double
                                                                                         // quote here
                                            .append(content.substring(
                                                    maintenanceScheduleIndex + "\"maintenanceSchedule\": \"".length()
                                                            + maintenanceSchedule.length()));

                                    try (FileWriter writer = new FileWriter(jsonStorage)) {
                                        writer.write(updatedContent.toString());
                                        System.out
                                                .println("File updated successfully for" + userSearchInputString + ".");
                                    } catch (IOException e) {
                                        System.out.println("Error writing to file: " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Maintenance processing cancelled.");
                                }
                            } else {
                                System.out.println("No pending maintenance schedule.");
                            }
                        } else {
                            System.out.println("Maintenance schedule not found.");
                        }
                    } else {
                        System.out.println("Maintenance history not found.");
                    }
                } else {
                    System.out.println("Entry not found for " + userSearchInputString + ".");
                }

            } catch (Exception e) {
                System.out.println("Error rewriting file: " + e.getMessage());
            }
        }
    }

    public CustomArrayList<Object> searchMaintenance() {
        System.out.println("Please enter the maintenance date.");
        validityString(scanner); // validates input and sets trueFalse + userStringInput
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput == null ? "" : userStringInput.trim();

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            System.out.println(
                    "Searching for vehicles with maintenance dates matching \"" + userSearchInputString + "\"");

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
                                    if (fieldName.equals("maintenanceHistory")) {
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

        // Display concise confirmation of matches found
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

    public static void main(String[] args) {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(50);
        scheduler.selectMenuItem();
    }
}
