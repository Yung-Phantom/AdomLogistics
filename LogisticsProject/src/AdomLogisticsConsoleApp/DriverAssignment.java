// Main driver assignment and management logic for Adom Logistics
package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import CustomDataStructures.CustomArrayList;
// import java.util.ArrayList;
// import java.util.List;
import java.util.Scanner;


/**
 * Main class for driver assignment, management, and activity tracking.
 * Handles adding, removing, updating, assigning, and viewing drivers.
 */
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

    /**
     * Constructor: Initializes banners and menu options.
     */
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
        addDriver.addToMenu("Please enter the driver's proximity.");

        updateDriver = new BannerElements(userInput, "Update Driver Activity");
        updateDriver.addToMenu("Please enter the driver's ID.");
        updateDriver.addToMenu("Please enter the new route ID.");
        updateDriver.addToMenu("Was there a delay? (yes/no)");
        updateDriver.addToMenu("Please enter any infraction notes.(leave blank if none):");
    }

    /**
     * Main menu selection logic. Handles user input for menu navigation.
     */
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
                    viewDriverActivity();
                    break;
                case 6:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;

            }
        }
    }

    /**
     * View a driver's activity, including all routes, assignment status, delays, and infractions.
     */
    private void viewDriverActivity() {
        System.out.println("\n=== View Driver Activity ===");
        System.out.print("Enter Driver ID: ");
        String driverID = scanner.nextLine().trim();

        // Check if driver exists in driverDetails.txt and get details
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
        if (!detailsFile.exists()) {
            System.out.println("No driver details found.");
            return;
        }
        String driverName = null;
        String assignmentStatus = null;
        String currentAssignment = null;
        boolean found = false;
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
                        driverName = line.substring("Driver Name: ".length()).trim();
                    }
                    if (line.startsWith("Assigned: ")) {
                        assignmentStatus = line.substring("Assigned: ".length()).trim();
                    }
                    if (line.startsWith("Assigned Routes: ")) {
                        String val = line.substring("Assigned Routes: ".length()).trim();
                        if (!val.isEmpty())
                            currentAssignment = val;
                    }
                    if (line.startsWith("Entry")) {
                        insideBlock = false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading driverDetails.txt: " + e.getMessage());
            return;
        }
        if (driverName == null) {
            System.out.println("Driver ID not found.");
            return;
        }

        // Now check drivers.txt for routes, delays, infractions
        File driverStorage = new File("LogisticsProject/src/finalDatabase/drivers.txt").getAbsoluteFile();
        String allRoutes = null;
        int delayCount = 0;
        String allInfractions = null;
        if (driverStorage.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(driverStorage))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(driverID + "|")) {
                        String[] parts = line.split("\\|", -1);
                        if (parts.length > 2)
                            allRoutes = parts[2];
                        if (parts.length > 3) {
                            try {
                                delayCount = Integer.parseInt(parts[3]);
                            } catch (Exception e) {
                                delayCount = 0;
                            }
                        }
                        if (parts.length > 5 && !parts[5].isEmpty())
                            allInfractions = parts[5];
                        break;
                    }
                }
            } catch (IOException e) {
                /* ignore */ }
        }

        // Output
        System.out.println("\nDriver Name: " + driverName);
        if (allRoutes != null && !allRoutes.isEmpty()) {
            System.out.println("All Routes: " + allRoutes);
        } else {
            System.out.println("No routes assigned yet.");
        }
        System.out.println("Assignment Status: " + (assignmentStatus != null ? assignmentStatus : "Unknown"));
        if (currentAssignment != null && !currentAssignment.isEmpty()) {
            System.out.println("Current Assignment: " + currentAssignment);
        }
        System.out.println("Number of Delays: " + delayCount);
        if (allInfractions != null && !allInfractions.isEmpty()) {
            System.out.println("Infractions: " + allInfractions);
        }
    }

    /**
     * Assigns a driver to a route based on proximity or experience/fairness.
     * Maintains two queues and updates persistent storage.
     */
    private void assignDriverMenu() {
        // New logic: maintain two queues (proximity and experience)
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
        File driverStorage = new File("LogisticsProject/src/finalDatabase/drivers.txt").getAbsoluteFile();
        if (!detailsFile.exists()) {
            System.out.println("No driver details found.");
            return;
        }

    // Helper class for queueing drivers for assignment
    class DriverQ {
            String id;
            double proximity;
            int experienceScore;
            int lineIdx;
            String name;
            int delays;
            int infractions;
            int routes;

            DriverQ(String id, double proximity, int experienceScore, int lineIdx, String name, int delays,
                    int infractions, int routes) {
                this.id = id;
                this.proximity = proximity;
                this.experienceScore = experienceScore;
                this.lineIdx = lineIdx;
                this.name = name;
                this.delays = delays;
                this.infractions = infractions;
                this.routes = routes;
            }
        }

        // Build proximityQueue and experienceQueue from driverDetails.txt
        CustomArrayList<DriverQ> proximityQueue = new CustomArrayList<>();
        CustomArrayList<DriverQ> experienceQueue = new CustomArrayList<>();
        CustomArrayList<String> allLines = new CustomArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
            String line;
            int idx = 0;
            int entryStart = -1;
            String id = null, name = null;
            double proximity = Double.MAX_VALUE;
            int delays = 0, infractions = 0, routes = 0;
            boolean assigned = false;
            String status = "Active";
            while ((line = reader.readLine()) != null) {
                allLines.addElement(line);
                if (line.startsWith("Entry")) {
                    entryStart = idx;
                    id = null;
                    name = null;
                    proximity = Double.MAX_VALUE;
                    delays = 0;
                    infractions = 0;
                    routes = 0;
                    assigned = false;
                    status = "Active";
                }
                if (line.startsWith("Driver ID: ")) {
                    id = line.substring("Driver ID: ".length()).trim();
                }
                if (line.startsWith("Driver Name: ")) {
                    name = line.substring("Driver Name: ".length()).trim();
                }
                if (line.startsWith("Proximity: ")) {
                    try {
                        proximity = Double.parseDouble(line.substring("Proximity: ".length()).trim());
                    } catch (Exception e) {
                        proximity = Double.MAX_VALUE;
                    }
                }
                if (line.startsWith("Delays: ")) {
                    try {
                        delays = Integer.parseInt(line.substring("Delays: ".length()).trim());
                    } catch (Exception e) {
                        delays = 0;
                    }
                }
                if (line.startsWith("Infractions: ")) {
                    try {
                        infractions = Integer.parseInt(line.substring("Infractions: ".length()).trim());
                    } catch (Exception e) {
                        infractions = 0;
                    }
                }
                if (line.startsWith("Assigned: ")) {
                    assigned = line.trim().equalsIgnoreCase("Assigned: true");
                }
                if (line.startsWith("Assigned Routes: ")) {
                    String routesStr = line.substring("Assigned Routes: ".length()).trim();
                    if (!routesStr.isEmpty()) {
                        routes = routesStr.split(",").length;
                    } else {
                        routes = 0;
                    }
                }
                if (line.startsWith("Status: ")) {
                    status = line.substring("Status: ".length()).trim();
                }
                if (line.startsWith("--------------------------------------------------")) {
                    if (id != null && proximity != Double.MAX_VALUE && !assigned && !status.equalsIgnoreCase("Deleted")) {
                        DriverQ dq = new DriverQ(id, proximity, 0, entryStart, name == null ? "" : name, delays,
                                infractions, routes);
                        proximityQueue.addElement(dq);
                        experienceQueue.addElement(dq);
                    }
                }
                idx++;
            }
        } catch (IOException e) {
            System.out.println("Error reading driver details: " + e.getMessage());
            return;
        }

        DriverQ chosen = null;
        // Ask user for assignment type
        int assignType = 0;
        while (true) {
            System.out.println("Select assignment type:");
            System.out.println("1. Assign by proximity");
            System.out.println("2. Assign by experience/fairness");
            System.out.print("Enter 1 or 2: ");
            String assignTypeInput = scanner.nextLine().trim();
            if (assignTypeInput.equals("1") || assignTypeInput.equals("2")) {
                assignType = Integer.parseInt(assignTypeInput);
                break;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
        String routeID = null;
        if (assignType == 1) {
            // Proximity: lowest number (double)
            for (int i = 0; i < proximityQueue.size(); i++) {
                if (chosen == null || proximityQueue.getElement(i).proximity < chosen.proximity) {
                    chosen = proximityQueue.getElement(i);
                }
            }
        } else {
            // Experience: fairness logic
            // 1. On first run, assign all drivers until each has at least one route
            // 2. On subsequent runs, queue perfect drivers first, then by fewest delays,
            // then by fewest infractions, then by formula
            // Build lists for each group
            CustomArrayList<DriverQ> noRoute = new CustomArrayList<>();
            CustomArrayList<DriverQ> perfect = new CustomArrayList<>();
            CustomArrayList<DriverQ> fewestDelays = new CustomArrayList<>();
            CustomArrayList<DriverQ> fewestInfractions = new CustomArrayList<>();
            CustomArrayList<DriverQ> rest = new CustomArrayList<>();
            int minDelays = Integer.MAX_VALUE;
            int minInfractions = Integer.MAX_VALUE;
            for (int i = 0; i < experienceQueue.size(); i++) {
                DriverQ dq = experienceQueue.getElement(i);
                if (dq.routes == 0) {
                    noRoute.addElement(dq);
                } else if (dq.delays == 0 && dq.infractions == 0) {
                    perfect.addElement(dq);
                } else {
                    if (dq.delays < minDelays)
                        minDelays = dq.delays;
                    if (dq.infractions < minInfractions)
                        minInfractions = dq.infractions;
                }
            }
            // Add drivers with fewest delays
            for (int i = 0; i < experienceQueue.size(); i++) {
                DriverQ dq = experienceQueue.getElement(i);
                if (dq.routes > 0 && dq.delays == minDelays && dq.delays > 0) {
                    fewestDelays.addElement(dq);
                }
            }
            // Add drivers with fewest infractions
            for (int i = 0; i < experienceQueue.size(); i++) {
                DriverQ dq = experienceQueue.getElement(i);
                if (dq.routes > 0 && dq.infractions == minInfractions && dq.infractions > 0) {
                    fewestInfractions.addElement(dq);
                }
            }
            // Add the rest
            for (int i = 0; i < experienceQueue.size(); i++) {
                DriverQ dq = experienceQueue.getElement(i);
                if (dq.routes > 0 && dq.delays != minDelays && dq.infractions != minInfractions
                        && !(dq.delays == 0 && dq.infractions == 0)) {
                    rest.addElement(dq);
                }
            }
            // Priority: noRoute > perfect > fewestDelays > fewestInfractions > rest (by
            // formula)
            CustomArrayList<DriverQ> fairQueue = new CustomArrayList<>();
            for (int i = 0; i < noRoute.size(); i++)
                fairQueue.addElement(noRoute.getElement(i));
            for (int i = 0; i < perfect.size(); i++)
                fairQueue.addElement(perfect.getElement(i));
            for (int i = 0; i < fewestDelays.size(); i++)
                fairQueue.addElement(fewestDelays.getElement(i));
            for (int i = 0; i < fewestInfractions.size(); i++)
                fairQueue.addElement(fewestInfractions.getElement(i));
            for (int i = 0; i < rest.size(); i++)
                fairQueue.addElement(rest.getElement(i));
            // Now pick the best from fairQueue using formula: score =
            // 10000/(1+infractions+delays) + routes
            for (int i = 0; i < fairQueue.size(); i++) {
                DriverQ dq = fairQueue.getElement(i);
                if (chosen == null) {
                    chosen = dq;
                } else {
                    int chosenScore = (int) (10000.0 / (1 + chosen.infractions + chosen.delays) + chosen.routes);
                    int dqScore = (int) (10000.0 / (1 + dq.infractions + dq.delays) + dq.routes);
                    if (dqScore > chosenScore) {
                        chosen = dq;
                    }
                }
            }
        }
        if (chosen == null) {
            System.out.println("No suitable driver found.");
            return;
        }

        // Ask for route to assign only after driver is chosen
        System.out.print("Enter Route ID to assign: ");
        routeID = scanner.nextLine().trim();

        // ...existing code for updating driverDetails.txt and drivers.txt...
        int entryStart = chosen.lineIdx;
        int entryEnd = chosen.lineIdx;
        while (entryStart > 0 && !allLines.getElement(entryStart).startsWith("Entry"))
            entryStart--;
        while (entryEnd < allLines.size()
                && !allLines.getElement(entryEnd).startsWith("--------------------------------------------------"))
            entryEnd++;
        for (int i = entryStart; i < entryEnd; i++) {
            String l = allLines.getElement(i);
            if (l.startsWith("Assigned: ")) {
                allLines.setElement(i, "Assigned: true");
            }
            if (l.startsWith("Assigned Routes: ")) {
                allLines.setElement(i, "Assigned Routes: " + routeID);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(detailsFile))) {
            for (int i = 0; i < allLines.size(); i++) {
                writer.write(allLines.getElement(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating driver assignment status: " + e.getMessage());
            return;
        }
        // Update drivers.txt: accumulate routes as comma-separated
        CustomArrayList<String> driverLines = new CustomArrayList<>();
        boolean found = false;
        String newRoute = routeID;
        if (driverStorage.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(driverStorage))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(chosen.id + "|")) {
                        // Accumulate routes as comma-separated
                        String[] parts = line.split("\\|", -1);
                        String routes = parts.length > 2 ? parts[2] : "";
                        if (!routes.contains(newRoute)) {
                            if (!routes.isEmpty())
                                routes += "," + newRoute;
                            else
                                routes = newRoute;
                        }
                        String updated = chosen.id + "|" + chosen.name + "|" + routes;
                        for (int i = 3; i < parts.length; i++) {
                            updated += "|" + parts[i];
                        }
                        driverLines.addElement(updated);
                        found = true;
                    } else {
                        driverLines.addElement(line);
                    }
                }
            } catch (IOException e) {
                /* ignore */ }
        }
        if (!found) {
            String newLine = chosen.id + "|" + chosen.name + "|" + newRoute;
            driverLines.addElement(newLine);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(driverStorage))) {
            for (int i = 0; i < driverLines.size(); i++) {
                writer.write(driverLines.getElement(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to drivers.txt: " + e.getMessage());
        }
        System.out.println("Assigned driver " + chosen.id + " (" + chosen.name + ") to route " + routeID + ".");
        // Remove assigned driver from queue for this session (if proximity)
        if (assignType == 1) {
            for (int i = 0; i < proximityQueue.size(); i++) {
                if (proximityQueue.getElement(i).id.equals(chosen.id)) {
                    proximityQueue.removeElement(i);
                    break;
                }
            }
        }
        // Return immediately after assignment to prevent double prompt
        return;
    }

    // Helper: check if driverID exists in driverDetails.txt (supports a few common
    // formats)

    /**
     * Checks if a driver ID exists in driverDetails.txt (supports several formats).
     */
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

                if (trimmed.startsWith("Driver ID:")) {
                    String id = trimmed.substring("Driver ID:".length()).trim();
                    if (driverID.equals(id))
                        return true;
                    continue;
                }

                if (trimmed.contains("|")) {
                    String first = trimmed.split("\\|", -1)[0].trim();
                    if (driverID.equals(first))
                        return true;
                    continue;
                }

                if (driverID.equals(trimmed))
                    return true;
            }
        } catch (IOException e) {
            System.out.println("Error checking driverDetails.txt: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates a driver's activity (route, delay, infraction) and accumulates history.
     * Also resets assignment status after update.
     */
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

        // Use the currently assigned route from driverDetails.txt
        String routeID = null;
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
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
                        if (line.startsWith("Assigned Routes: ")) {
                            routeID = line.substring("Assigned Routes: ".length()).trim();
                            break;
                        }
                        if (line.startsWith("Entry")) {
                            insideBlock = false;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading driverDetails.txt: " + e.getMessage());
            }
        }
        if (routeID == null)
            routeID = "";
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

        // Compose full line for drivers.txt:
        // id|name|route|timestamp|delayCount|infractionCount|infractions
        String driverName = "Unknown";
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
                            driverName = line.substring("Driver Name: ".length()).trim();
                        }
                        if (line.startsWith("Entry")) {
                            insideBlock = false;
                        }
                    }
                }
            } catch (IOException e) {
                /* ignore */ }
        }
        int delayCount = delayed ? 1 : 0;
        int infractionCount = infraction.isEmpty() ? 0 : 1;
        String infractions = infraction.isEmpty() ? "" : infraction;
        long timestamp = System.currentTimeMillis();
        String logLine = driverID + "|" + driverName + "|" + routeID + "|" + timestamp + "|" + delayCount + "|"
                + infractionCount + "|" + infractions;
        // Only update if Assigned is true
        if (!isDriverAssigned(driverID)) {
            System.out.println("Driver is not assigned. Skipping update.");
            return;
        }
        CustomArrayList<String> driverLines = new CustomArrayList<>();
        boolean found = false;
        int newDelayCount = delayCount;
        int newInfractionCount = infractionCount;
        String newInfractions = infractions;
        if (driverStorage.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(driverStorage))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(driverID + "|")) {
                        // Accumulate routes as comma-separated
                        String[] parts = line.split("\\|", -1);
                        String routes = parts.length > 2 ? parts[2] : "";
                        if (!routes.contains(routeID)) {
                            if (!routes.isEmpty())
                                routes += "," + routeID;
                            else
                                routes = routeID;
                        }
                        String delays = parts.length > 3 ? parts[3] : "0";
                        String infractionsCount = parts.length > 4 ? parts[4] : "0";
                        String infractionDetails = parts.length > 5 ? parts[5] : "";
                        // Sum delay and infraction counts
                        int totalDelay = 0, totalInfraction = 0;
                        try {
                            totalDelay = Integer.parseInt(delays);
                        } catch (Exception e) {
                            totalDelay = 0;
                        }
                        try {
                            totalInfraction = Integer.parseInt(infractionsCount);
                        } catch (Exception e) {
                            totalInfraction = 0;
                        }
                        totalDelay += newDelayCount;
                        totalInfraction += newInfractionCount;
                        // Append infraction details
                        if (!infractionDetails.isEmpty() && !newInfractions.isEmpty())
                            infractionDetails += "," + newInfractions;
                        else if (!newInfractions.isEmpty())
                            infractionDetails = newInfractions;
                        String updated = driverID + "|" + driverName + "|" + routes + "|" + totalDelay + "|"
                                + totalInfraction + "|" + infractionDetails;
                        driverLines.addElement(updated);
                        found = true;
                    } else {
                        driverLines.addElement(line);
                    }
                }
            } catch (IOException e) {
                /* ignore */ }
        }
        if (!found) {
            driverLines.addElement(logLine);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(driverStorage))) {
            for (int i = 0; i < driverLines.size(); i++) {
                writer.write(driverLines.getElement(i));
                writer.newLine();
            }
            System.out.println("Driver activity updated successfully.");
        } catch (IOException e) {
            System.out.println("File access error: " + e.getMessage());
        }

        // After update, set Assigned: false and Assigned Routes: empty in
        // driverDetails.txt
        // Update in-place
        if (detailsFile.exists()) {
            CustomArrayList<String> allLines = new CustomArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
                String line;
                boolean insideBlock = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Driver ID: ")) {
                        insideBlock = line.equals("Driver ID: " + driverID);
                        allLines.addElement(line);
                        continue;
                    }
                    if (insideBlock) {
                        if (line.startsWith("Assigned: ")) {
                            allLines.addElement("Assigned: false");
                            continue;
                        }
                        if (line.startsWith("Assigned Routes: ")) {
                            allLines.addElement("Assigned Routes: ");
                            continue;
                        }
                        if (line.startsWith("Entry")) {
                            insideBlock = false;
                        }
                    }
                    allLines.addElement(line);
                }
            } catch (IOException e) {
                /* ignore */ }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(detailsFile))) {
                for (int i = 0; i < allLines.size(); i++) {
                    String l = allLines.getElement(i);
                    writer.write(l);
                    writer.newLine();
                }
            } catch (IOException e) {
                /* ignore */ }
        }
    }

    // Inline-safe integer parser for delay/infraction counts
    /**
     * Inline-safe integer parser for delay/infraction counts.
     */
    @SuppressWarnings("unused")
    private int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Removes a driver by marking their status as Deleted in driverDetails.txt.
     * Uses license and phone number for disambiguation.
     */
    private void removeDriver() {
        // Remove by Driver ID
        System.out.println("Enter: Driver ID");
        validityString(scanner); // Validate input
        boolean choice = trueFalse;
        String userSearchInputString = userStringInput;

        CustomArrayList<Object> results = new CustomArrayList<>();

        if (choice) {
            System.out.println("Searching for driver with ID matching '" + userSearchInputString + "'");
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
                            String matchedFieldValue = null;
                            for (int i = 0; i < entryLines.size(); i++) {
                                String e = entryLines.getElement(i).trim();
                                if (e.startsWith("Driver ID:")) {
                                    matchedFieldValue = e.substring(e.indexOf(":") + 1).trim();
                                    break;
                                }
                            }
                            if (matchedFieldValue != null && matchedFieldValue.equalsIgnoreCase(userSearchInputString)) {
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

        if (results.size() == 1) {
            Object entryObj = results.getElement(0);
            if (entryObj instanceof CustomArrayList) {
                CustomArrayList<?> entry = (CustomArrayList<?>) entryObj;
                String header = entry.getElement(0).toString();
                String entryNumber = header.replace("Entry ", "").trim();
                updateTxt(entryNumber);
            } else {
                System.out.println("Unexpected result format.");
            }
        } else if (results.size() == 0) {
            System.out.println("No matching driver found.");
        } else {
            System.out.println("Multiple entries still match. Manual review may be required.");
        }
    }

    /**
     * Marks a driver as deleted in driverDetails.txt by unique entry identifier.
     */
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

    /**
     * Searches for drivers by a given field (e.g., License Number, Phone Number).
     * Returns a list of matching entries.
     */
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

    /**
     * Adds a new driver, capturing all required fields with validation.
     * Persists to driverDetails.txt and echoes a summary.
     */
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

                    validityString(scanner);
                    addDriverDetails.addElement(userStringInput);
                    break;
                case 2: // Driver's phone number
                    validityPhoneString(scanner);
                    addDriverDetails.addElement(userStringInput);
                    break;
                case 5: // Proximity (must be a double)
                    validityDouble(scanner);
                    addDriverDetails.addElement(userDoubleInput);
                    break;
                default:
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
                addNewEntry.addElement("Driver proximity: " + addDriverDetails.getElement(index));
            }
        }
        // Add assigned status (default: false)
        addNewEntry.addElement("Assigned: false");

        // 3) Persist: saveTXT now receives the ID as the first element
        // Ensure your saveTXT respects this order so search-by-ID works cleanly
        saveTXT(addDriverDetails);

        // 4) Echo summary
        for (int i = 0; i < addNewEntry.size(); i++) {
            System.out.println(addNewEntry.getElement(i));
        }
    }

    /**
     * Persists a new driver entry to driverDetails.txt.
     */
    public void saveTXT(CustomArrayList<Object> driverDetails) {
        txtStorageDir = new File("LogisticsProject/src/TXTDatabase").getAbsoluteFile();
        if (!txtStorageDir.exists()) {
            txtStorageDir.mkdirs();
        }

        txtStorage = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();

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

            StringBuilder newEntry = new StringBuilder();
            newEntry.append("Entry ").append(String.format("%03d", entryCount)).append("\n");
            newEntry.append("Driver ID: ").append(driverDetails.getElement(0)).append("\n");
            newEntry.append("Driver Name: ").append(driverDetails.getElement(1)).append("\n");
            newEntry.append("License Number: ").append(driverDetails.getElement(2)).append("\n");
            newEntry.append("Phone Number: ").append(driverDetails.getElement(3)).append("\n");
            newEntry.append("Email Address: ").append(driverDetails.getElement(4)).append("\n");
            newEntry.append("Address: ").append(driverDetails.getElement(5)).append("\n");
            newEntry.append("Proximity: ").append(driverDetails.getElement(6)).append("\n");
            newEntry.append("Assigned: false\n");
            newEntry.append("Assigned Routes: \n"); // Correct placement
            newEntry.append("Status: Active\n");
            newEntry.append("--------------------------------------------------\n");

            try (FileWriter writer = new FileWriter(txtStorage, true)) {
                writer.write(newEntry.toString());
                System.out.println("Driver entry " + entryCount + " saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Validates phone number input (digits only, 10-16 digits).
     */
    public void validityPhoneString(Scanner scanner) {
        while (true) {
            System.out.print("Enter phone number (digits only): ");
            String s = scanner.nextLine().trim();
            if (s.matches("\\d{10,16}")) {
                userStringInput = s;
                trueFalse = true;
                break;
            } else {
                System.out.println("Invalid phone number. Enter 10–16 digits only.");
            }
        }
    }

    /**
     * Validates integer menu input within a given range.
     */
    public void validity(Scanner scanner, int i) {
        while (true) {
            System.out.print("Please enter your choice (1 - " + i + "): ");
            String s = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(s);
                if (value >= 0 && value <= i) {
                    userInput = value;
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Try again. Enter 0 to view the menu");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number. Enter 0 to view the menu");
            }
        }
    }

    /**
     * Validates double input (positive only).
     */
    public void validityDouble(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(s);
                if (value >= 0) {
                    userDoubleInput = value;
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Please enter a positive double.");
                }
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid double.");
            }
        }
    }

    /**
     * Validates non-empty string input.
     */
    public void validityString(Scanner scanner) {
        while (true) {
            System.out.print("Enter text: ");
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

    // Checks if the driver with the given ID is currently assigned in
    // driverDetails.txt
    /**
     * Checks if the driver with the given ID is currently assigned in driverDetails.txt.
     */
    private boolean isDriverAssigned(String driverID) {
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/driverDetails.txt").getAbsoluteFile();
        if (!detailsFile.exists())
            return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
            String line;
            boolean insideBlock = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Driver ID: ")) {
                    insideBlock = line.equals("Driver ID: " + driverID);
                    continue;
                }
                if (insideBlock) {
                    if (line.startsWith("Assigned: ")) {
                        return line.trim().equalsIgnoreCase("Assigned: true");
                    }
                    if (line.startsWith("Entry")) {
                        insideBlock = false;
                    }
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    /**
     * Main entry point for the Adom Logistics Driver Management system.
     */
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
