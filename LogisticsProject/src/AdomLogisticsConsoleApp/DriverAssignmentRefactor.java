package AdomLogisticsConsoleApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import AdomLogisticsConsoleApp.ObjectClass.Driver;
import AdomLogisticsConsoleApp.ObjectClass.InputValidity;
import AdomLogisticsConsoleApp.ObjectClass.TextEntryManager;
import AdomLogisticsConsoleApp.ObjectClass.TxtEntry;
import CustomDataStructures.CustomArrayList;
import CustomDataStructures.CustomQueue;
import CustomDataStructures.DoublyLinkedList;

public class DriverAssignmentRefactor {
    BannerElements bElements;
    BannerElements addDriver;
    BannerElements searchByElements;
    BannerElements updateDriver;

    private final Scanner scanner = new Scanner(System.in);
    private final File txtStorageDir;
    private final File txtStorage;
    TextEntryManager textEntryManager;
    TextEntryManager assignmentOfDrivers;
    InputValidity iv = new InputValidity();
    private String userStringInput;
    private double low;
    private double high;
    CustomArrayList<String> updateDriverDetails;
    CustomArrayList<Object> addNewEntry;
    CustomQueue<Driver> proximityQueue;
    CustomQueue<Driver> experienceQueue;
    private File helperStorage;
    private File helperStorageDir;
    Driver d;

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

        helperStorageDir = new File("LogisticsProject/src/finalDatabase");
        helperStorage = new File("LogisticsProject/src/finalDatabase/drivers.txt");
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

        try {
            if (!helperStorageDir.exists() || !helperStorage.exists()) {
                helperStorageDir.mkdirs();
                if (!helperStorage.exists()) {
                    helperStorage.createNewFile();
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

    private void updateDriverActivity() {
        // Prepare storage of inputs
        updateDriverDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();

        System.out.println("\n=== Update Driver Activity ===");

        // 1) Prompt 0: Driver ID
        System.out.println("\n" + updateDriver.getElement(0));
        String driverID = iv.validityString(scanner);
        updateDriverDetails.addElement(driverID);

        // 2) Gate: only proceed if driver exists in drivers.txt
        if (!textEntryManager.doesDriverExist(driverID)) {
            System.out.println(
                    "\nDriver ID \"" + driverID + "\" not found in drivers.txt.");
            System.out.println("Activity update cancelled.\n");
            return;
        }

        // 3) Complete any pending delivery tasks
        try {
            deliveryComplete(driverID);
        } catch (Exception e) {
            System.out.println("Error updating delivery status: " + e.getMessage());
        }

        // 4) Load the driver’s TxtEntry (fields populated by parseFields)
        TxtEntry driverEntry = null;
        try {
            CustomArrayList<TxtEntry> all = textEntryManager.readAllEntries(textEntryManager.lineStarter);
            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                String idField = e.fields.get("Driver ID");
                if (!e.deleted && driverID.equalsIgnoreCase(idField)) {
                    driverEntry = e;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading driver details: " + e.getMessage());
        }

        // 5) Pull Assigned Routes from parsed fields
        String routeID = driverEntry == null
                ? ""
                : driverEntry.fields.get("Assigned Routes");
        updateDriverDetails.addElement(routeID);

        // 6) Prompt 2: Delay? (yes/no only)
        while (true) {
            System.out.println("\n" + updateDriver.getElement(2));
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                updateDriverDetails.addElement(input);
                break;
            }
            System.out.println("Invalid input. Please enter \"yes\" or \"no\".");
        }

        // 7) Prompt 3: Infraction (optional)
        System.out.println("\n" + updateDriver.getElement(3));
        String infraction = scanner.nextLine().trim();
        updateDriverDetails.addElement(infraction);

        boolean delayed = updateDriverDetails
                .getElement(2)
                .equalsIgnoreCase("yes");

        // 9) Compose the same log line (without timestamp)
        String driverName = driverEntry == null
                ? "Unknown"
                : driverEntry.fields.get("Driver Name") != null
                        ? driverEntry.fields.get("Driver Name")
                        : "Unknown";

        int delayCount = delayed ? 1 : 0;
        int infractionCount = infraction.isEmpty() ? 0 : 1;
        String infractionsValue = infraction.isEmpty() ? "" : infraction;

        String logLine = driverID + "|"
                + driverName + "|"
                + routeID + "|"
                + delayCount + "|"
                + infractionCount + "|"
                + infractionsValue;

        // 10) Only update if Assigned is true
        if (!isDriverAssigned(driverID)) {
            System.out.println("Driver is not assigned. Skipping update.");
            return;
        }

        // 11) Append or replace existing record in drivers.txt
        CustomArrayList<String> existing = new CustomArrayList<>();
        if (txtStorage.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(txtStorage))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    existing.addElement(line);
                }
            } catch (IOException ignored) {
            }
        }
        // filter out old record using a for loop
        CustomArrayList<String> driverLines = new CustomArrayList<>();
        for (int i = 0; i < existing.size(); i++) {
            String line = existing.getElement(i);
            if (!line.startsWith(driverID + "|")) {
                driverLines.addElement(line);
            }
        }
        driverLines.addElement(logLine);

        try {
            textEntryManager.writeAllLines(driverLines);
            System.out.println("Driver activity updated successfully.");
        } catch (IOException e) {
            System.out.println("File access error: " + e.getMessage());
        }

        // 12) Clear Assigned & Assigned Routes in drivers.txt
        try {
            CustomArrayList<TxtEntry> all = textEntryManager.readAllEntries(textEntryManager.lineStarter);

            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                if (!e.deleted
                        && driverID.equalsIgnoreCase(e.fields.get("Driver ID"))) {
                    e.fields.put("Assigned", "false");
                    e.fields.put("Assigned Routes", "");
                    // rebuild rawLines
                    CustomArrayList<String> raw = new CustomArrayList<>();
                    for (int j = 0; j < e.rawLines.size(); j++) {
                        String ln = e.rawLines.getElement(j);
                        if (ln.startsWith("Assigned: ")) {
                            raw.addElement("Assigned: false");
                        } else if (ln.startsWith("Assigned Routes: ")) {
                            raw.addElement("Assigned Routes: ");
                        } else {
                            raw.addElement(ln);
                        }
                    }
                    e.rawLines = raw;
                    break;
                }
            }

            // flatten back to lines with for loops
            CustomArrayList<String> dump = new CustomArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                TxtEntry e = all.getElement(i);
                for (int j = 0; j < e.rawLines.size(); j++) {
                    dump.addElement(e.rawLines.getElement(j));
                }
            }

            textEntryManager.writeAllLines(dump);
        } catch (IOException e) {
            System.out.println("Error updating driver details: " + e.getMessage());
        }
    }

    public void deliveryComplete(String driverID) {
    }
    private boolean isDriverAssigned(String driverID) {
        // 1) Centralize file paths and entry prefix

        try {
            // 2) Parse every driver block into TxtEntry objects
            CustomArrayList<TxtEntry> drivers = textEntryManager.readAllEntries(textEntryManager.lineStarter);

            // 3) Locate the matching Driver ID and return its Assigned flag
            for (int i = 0; i < drivers.size(); i++) {
                TxtEntry entry = drivers.getElement(i);
                if (entry.deleted) {
                    continue;
                }

                String idValue = entry.fields.get("Driver ID");
                if (driverID.equalsIgnoreCase(idValue)) {
                    String assignedValue = entry.fields.get("Assigned");
                    return Boolean.parseBoolean(assignedValue);
                }
            }
        } catch (IOException ioe) {
            // 4) Fail gracefully (optionally log)
            System.err.println("Failed to read drivers.txt: " + ioe.getMessage());
        }

        // 5) Default: either no file, no match, or unassigned
        return false;
    }

    private void assignDriverMenu() {
        // 1) Check that all needed files exist
        File detailsFile = new File("LogisticsProject/src/TXTDatabase/drivers.txt").getAbsoluteFile();
        File driversFile = new File("LogisticsProject/src/finalDatabase/drivers.txt").getAbsoluteFile();
        File packagesFile = new File("LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();
        if (!detailsFile.exists()) {
            System.out.println("No driver details found.");
            return;
        }
        if (!packagesFile.exists()) {
            System.out.println("No package details found.");
            return;
        }

        // 2) Read raw driverDetails.txt into memory for updates
        CustomArrayList<String> driverLines = new CustomArrayList<>();
        try (BufferedReader rdr = new BufferedReader(new FileReader(detailsFile))) {
            String line;
            while ((line = rdr.readLine()) != null) {
                driverLines.addElement(line);
            }
        } catch (IOException ioe) {
            System.out.println("Error reading driver details: " + ioe.getMessage());
            return;
        }

        // 3) Initialize our two queues if needed
        if (proximityQueue == null)
            proximityQueue = new CustomQueue<>();
        if (experienceQueue == null)
            experienceQueue = new CustomQueue<>();

        // 4) Parse driver entries from driverLines into both queues
        int idx = 0, entryStart = -1;
        String id = null, name = null, status = "Active";
        double proximity = Double.MAX_VALUE;
        int delays = 0, infractions = 0, routes = 0;
        boolean assignedFlag = false;

        for (int j = 0; j < driverLines.size(); j++) {
            String raw = driverLines.getElement(j);
            if (raw.startsWith("Entry")) {
                entryStart = j;
                // reset
                id = name = null;
                status = "Active";
                proximity = Double.MAX_VALUE;
                delays = infractions = routes = 0;
                assignedFlag = false;
            }
            if (raw.startsWith("Driver ID: ")) {
                id = raw.substring(11).trim();
            } else if (raw.startsWith("Driver Name: ")) {
                name = raw.substring(13).trim();
            } else if (raw.startsWith("Proximity: ")) {
                try {
                    proximity = Double.parseDouble(raw.substring(11).trim());
                } catch (Exception e) {
                    proximity = Double.MAX_VALUE;
                }
            } else if (raw.startsWith("Delays: ")) {
                try {
                    delays = Integer.parseInt(raw.substring(8).trim());
                } catch (Exception e) {
                    delays = 0;
                }
            } else if (raw.startsWith("Infractions: ")) {
                try {
                    infractions = Integer.parseInt(raw.substring(13).trim());
                } catch (Exception e) {
                    infractions = 0;
                }
            } else if (raw.startsWith("Assigned: ")) {
                assignedFlag = raw.substring(10).trim().equalsIgnoreCase("true");
            } else if (raw.startsWith("Assigned Routes: ")) {
                String r = raw.substring(17).trim();
                routes = r.isBlank() ? 0 : r.split(",").length;
            } else if (raw.startsWith("Status: ")) {
                status = raw.substring(8).trim();
            }

            // end of block
            if (raw.startsWith("----------")) {
                // only free, active drivers
                if (id != null && !assignedFlag && !"Deleted".equalsIgnoreCase(status)) {
                    boolean inProx = false, inExp = false;
                    // check duplicates in proximityQueue
                    for (int i = 0; i < proximityQueue.size(); i++) {
                        Driver d = proximityQueue.peek();
                        proximityQueue.enqueue(proximityQueue.dequeue());
                        if (d.driverID.equals(id)) {
                            inProx = true;
                            break;
                        }
                    }
                    // check duplicates in experienceQueue
                    for (int i = 0; i < experienceQueue.size(); i++) {
                        Driver d = experienceQueue.peek();
                        experienceQueue.enqueue(experienceQueue.dequeue());
                        if (d.driverID.equals(id)) {
                            inExp = true;
                            break;
                        }
                    }
                    if (!inProx) {
                        proximityQueue.enqueue(
                                new Driver(id, proximity, 0, entryStart, name == null ? "" : name,
                                        delays, infractions, routes));
                    }
                    if (!inExp) {
                        experienceQueue.enqueue(
                                new Driver(id, proximity, 0, entryStart, name == null ? "" : name,
                                        delays, infractions, routes));
                    }
                }
            }
            idx++;
        }

        // 5) Prompt assignment type
        int assignType;
        while (true) {
            System.out.println("Select assignment type:");
            System.out.println("1. Assign by proximity");
            System.out.println("2. Assign by experience");
            System.out.print("Enter 1 or 2: ");
            String in = scanner.nextLine().trim();
            if ("1".equals(in) || "2".equals(in)) {
                assignType = Integer.parseInt(in);
                break;
            }
            System.out.println("Invalid input; choose 1 or 2.");
        }

        // 6) Pick the driver from the chosen queue
        Driver chosen = null;
        if (assignType == 1) {
            // lowest proximity
            for (int i = 0; i < proximityQueue.size(); i++) {
                Driver d = proximityQueue.peek();
                proximityQueue.enqueue(proximityQueue.dequeue());
                if (chosen == null || d.proximity < chosen.proximity) {
                    chosen = d;
                }
            }

        } else {
            // experience fairness logic (group into noRoute, perfect, fewestDelays,
            // fewestInfractions, rest)
            CustomArrayList<Driver> noRoute = new CustomArrayList<>();
            CustomArrayList<Driver> perfect = new CustomArrayList<>();
            CustomArrayList<Driver> fewestDelays = new CustomArrayList<>();
            CustomArrayList<Driver> fewestInfractions = new CustomArrayList<>();
            CustomArrayList<Driver> rest = new CustomArrayList<>();

            int minDelays = Integer.MAX_VALUE, minInfractions = Integer.MAX_VALUE;
            // find minima
            for (int i = 0; i < experienceQueue.size(); i++) {
                Driver d = experienceQueue.peek();
                experienceQueue.enqueue(experienceQueue.dequeue());
                if (d.routes == 0) {
                    noRoute.addElement(d);
                } else if (d.delays == 0 && d.numInfractions == 0) {
                    perfect.addElement(d);
                } else {
                    minDelays = Math.min(minDelays, d.delays);
                    minInfractions = Math.min(minInfractions, d.numInfractions);
                }
            }
            // bucket them
            for (int i = 0; i < experienceQueue.size(); i++) {
                Driver d = experienceQueue.peek();
                experienceQueue.enqueue(experienceQueue.dequeue());
                if (d.routes > 0 && d.delays == minDelays && d.delays > 0) {
                    fewestDelays.addElement(d);
                } else if (d.routes > 0 && d.numInfractions == minInfractions && d.numInfractions > 0) {
                    fewestInfractions.addElement(d);
                } else if (!(d.routes == 0 || (d.delays == 0 && d.numInfractions == 0) ||
                        (d.delays == minDelays && d.delays > 0) ||
                        (d.numInfractions == minInfractions && d.numInfractions > 0))) {
                    rest.addElement(d);
                }
            }
            // concat fairQueue
            CustomArrayList<Driver> fairQueue = new CustomArrayList<>();
            for (int i = 0; i < noRoute.size(); i++) {
                fairQueue.addElement(noRoute.getElement(i));
            }
            for (int i = 0; i < perfect.size(); i++) {
                fairQueue.addElement(perfect.getElement(i));
            }
            for (int i = 0; i < fewestDelays.size(); i++) {
                fairQueue.addElement(fewestDelays.getElement(i));
            }
            for (int i = 0; i < fewestInfractions.size(); i++) {
                fairQueue.addElement(fewestInfractions.getElement(i));
            }
            for (int i = 0; i < rest.size(); i++) {
                fairQueue.addElement(rest.getElement(i));
            }

            // pick best via score = 10000/(1+infractions+delays) + routes
            for (int i = 0; i < fairQueue.size(); i++) {
                Driver d = fairQueue.getElement(i);
                if (chosen == null) {
                    chosen = d;
                } else {
                    int cScore = (int) (10000.0 / (1 + chosen.numInfractions + chosen.delays) + chosen.routes);
                    int dScore = (int) (10000.0 / (1 + d.numInfractions + d.delays) + d.routes);
                    if (dScore > cScore)
                        chosen = d;
                }
            }
        }

        // 7) Bail if nothing found
        if (chosen == null) {
            System.out.println("No suitable driver found.");
            return;
        }

        // 8) Read packageDetails.txt, assign to first pending package
        CustomArrayList<String> pkgLines = new CustomArrayList<>();
        try (BufferedReader rdr = new BufferedReader(new FileReader(packagesFile))) {
            String l;
            while ((l = rdr.readLine()) != null)
                pkgLines.addElement(l);
        } catch (IOException e) {
            System.out.println("Error reading package details: " + e.getMessage());
            return;
        }
        // find first “Entry… Pending” block
        int pStart = -1, pEnd = -1;
        for (int i = 0; i < pkgLines.size(); i++) {
            if (pkgLines.getElement(i).startsWith("Entry ")) {
                // scan forward to see if this block contains “Status: Pending”
                for (int j = i; j < pkgLines.size() && !pkgLines.getElement(j)
                        .startsWith("----------"); j++) {
                    if (pkgLines.getElement(j).startsWith("Status: ")
                            && pkgLines.getElement(j).endsWith("Pending")) {
                        pStart = i;
                        // find block end
                        pEnd = j;
                        while (pEnd < pkgLines.size() &&
                                !pkgLines.getElement(pEnd)
                                        .startsWith("----------")) {
                            pEnd++;
                        }
                        break;
                    }
                }
                if (pStart != -1)
                    break;
            }
        }
        if (pStart == -1) {
            System.out.println("No pending package found.");
            return;
        }
        // reset all Last Update flags
        for (int i = 0; i < pkgLines.size(); i++) {
            if (pkgLines.getElement(i).startsWith("Last Update:")) {
                pkgLines.setElement(i, "Last Update: false");
            }
        }
        // mark chosen assignment
        for (int i = pStart; i < pEnd; i++) {
            String l = pkgLines.getElement(i);
            if (l.startsWith("Assigned Driver: ")) {
                pkgLines.setElement(i, "Assigned Driver: " + chosen.driverID);
            }
            if (l.startsWith("Status: ")) {
                pkgLines.setElement(i, "Status: Assigned");
            }
            if (l.startsWith("Last Update:")) {
                pkgLines.setElement(i, "Last Update: true");
            }
        }
        // write back packagesFile
        try (BufferedWriter w = new BufferedWriter(new FileWriter(packagesFile))) {
            for (int i = 0; i < pkgLines.size(); i++) {
                w.write(pkgLines.getElement(i));
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating package assignment: " + e.getMessage());
            return;
        }

        // 9) Update driverDetails.txt: mark assigned & add route entry (“package ID”)
        String pkgID = pkgLines.getElement(pStart).split("\\|")[1].trim();
        // assumes “Entry|<pkgID>…” formatting
        // reset Last Update flags in driverLines
        for (int i = 0; i < driverLines.size(); i++) {
            if (driverLines.getElement(i).startsWith("Last Update:")) {
                driverLines.setElement(i, "Last Update: false");
            }
        }
        // locate chosen driver block
        entryStart = chosen.lineIdx;
        entryStart = Math.max(0, entryStart);
        entryStart = whileEntryStart(driverLines, entryStart);
        entryStart = whileEntryEnd(driverLines, entryStart);

        for (int i = entryStart; i < driverLines.size(); i++) {
            String l = driverLines.getElement(i);
            if (l.startsWith("Assigned: ")) {
                driverLines.setElement(i, "Assigned: true");
            }
            if (l.startsWith("Assigned Routes: ")) {
                driverLines.setElement(i, "Assigned Routes: " + pkgID);
            }
            if (l.startsWith("Last Update:")) {
                driverLines.setElement(i, "Last Update: true");
            }
            if (l.startsWith("----------"))
                break;
        }
        // write driverDetails.txt back
        try {
            textEntryManager.writeAllLines(driverLines);
        } catch (IOException ioe) {
            System.out.println("Error updating driver details: " + ioe.getMessage());
            return;
        }

        // 10) Update or append in drivers.txt (accumulate packages)
        CustomArrayList<String> dsOut = new CustomArrayList<>();
        boolean found = false;
        try (BufferedReader rdr = new BufferedReader(new FileReader(driversFile))) {
            String line;
            while ((line = rdr.readLine()) != null) {
                if (line.startsWith(chosen.driverID + "|")) {
                    String[] parts = line.split("\\|", -1);
                    String assignRoutes = parts.length > 2 ? parts[2] : "";
                    if (!assignRoutes.contains(pkgID)) {
                        assignRoutes = assignRoutes.isEmpty() ? pkgID : assignRoutes + "," + pkgID;
                    }
                    String updated = chosen.driverID + "|" + chosen.name + "|" + routes;
                    for (int j = 3; j < parts.length; j++) {
                        updated += "|" + parts[j];
                    }
                    dsOut.addElement(updated);
                    found = true;
                } else {
                    dsOut.addElement(line);
                }
            }
        } catch (IOException ignored) {
        }
        if (!found) {
            dsOut.addElement(chosen.driverID + "|" + chosen.name + "|" + pkgID);
        }
        try (BufferedWriter w = new BufferedWriter(new FileWriter(driversFile))) {
            for (int i = 0; i < dsOut.size(); i++) {
                w.write(dsOut.getElement(i));
                w.newLine();
            }
        } catch (IOException ioe) {
            System.out.println("Error writing drivers.txt: " + ioe.getMessage());
        }

        // 11) Remove driver from proximityQueue session if chosen
        if (assignType == 1) {
            CustomQueue<Driver> tmp = new CustomQueue<>();
            while (!proximityQueue.isEmpty()) {
                Driver d = proximityQueue.dequeue();
                if (!d.driverID.equals(chosen.driverID)) {
                    tmp.enqueue(d);
                }
            }
            while (!tmp.isEmpty())
                proximityQueue.enqueue(tmp.dequeue());
        }

        // 12) Finalize
        System.out.printf("Assigned driver %s (%s) to package %s.%n",
                chosen.driverID, chosen.name, pkgID);
        deliveryInTransit();
    }

    // Helpers to find block boundaries
    private int whileEntryStart(CustomArrayList<String> lines, int idx) {
        while (idx > 0 && !lines.getElement(idx).startsWith("Entry")) {
            idx--;
        }
        return idx;
    }

    private int whileEntryEnd(CustomArrayList<String> lines, int idx) {
        while (idx < lines.size()
                && !lines.getElement(idx).startsWith("----------")) {
            idx++;
        }
        return idx;
    }

    public void deliveryInTransit() {
System.out.println("in here");
        // --- File handles for drivers, vehicles, and packages
        File driverDetailsFile = new File(
                "LogisticsProject/src/finalTxtDatabase/drivers.txt").getAbsoluteFile();

        File vehicleStorageFile = new File(
                "LogisticsProject/src/finalTxtDatabase/vehicles.txt").getAbsoluteFile();

        File packageDetailsFile = new File(
                "LogisticsProject/src/TXTDatabase/packageDetails.txt").getAbsoluteFile();

        String lastDriverID = null;
        String vehicleReg = null;

        // 1) Find the driver with Last Update: true
        try (BufferedReader reader = new BufferedReader(
                new FileReader(driverDetailsFile))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String data = sb.toString();
            int updateIdx = data.indexOf("Last Update: true");
            if (updateIdx < 0) {
                System.out.println("No driver with 'Last Update: true' found.");
                return;
            }

            int idIdx = data.lastIndexOf("Driver ID: ", updateIdx);
            int idEnd = data.indexOf("\n", idIdx);
            lastDriverID = data.substring(
                    idIdx + "Driver ID: ".length(),
                    idEnd).trim();

        } catch (IOException e) {
            System.out.println("Error reading driverDetails.txt: "
                    + e.getMessage());
            return;
        }

        // 2) Look up the vehicle assigned to that driver
        TextEntryManager vehManager = new TextEntryManager(
                "LogisticsProject/src/finalTxtDatabase/",
                vehicleStorageFile.getPath(),
                "Entry ");
        try {
            CustomArrayList<TxtEntry> vehEntries = vehManager.readAllEntries(vehManager.lineStarter);

            for (int i = 0; i < vehEntries.size(); i++) {
                TxtEntry ve = vehEntries.getElement(i);
                String status = ve.fields.get("Status");
                String did = ve.fields.get("Driver ID");
                if (!ve.deleted
                        && "Active".equalsIgnoreCase(status)
                        && lastDriverID.equalsIgnoreCase(did)) {

                    vehicleReg = ve.fields.get("Registration Number");
                    break;
                }
            }
            if (vehicleReg == null) {
                System.out.println("No active vehicle found for driver ID: "
                        + lastDriverID);
                return;
            }

        } catch (IOException ioe) {
            System.out.println("Error reading vehicle data: "
                    + ioe.getMessage());
            return;
        }

        // 3) Read and inspect all package entries
        TextEntryManager pkgManager = new TextEntryManager(
                "LogisticsProject/src/TXTDatabase/",
                packageDetailsFile.getPath(),
                "Entry ");

        try {
            // 3a) Read them in
            CustomArrayList<TxtEntry> entries = pkgManager.readAllEntries(pkgManager.lineStarter);

            // 3c) find the first Pending
            TxtEntry match = null;
            int matchIndex = -1;
            for (int i = 0; i < entries.size(); i++) {
                TxtEntry e = entries.getElement(i);
                String status = e.fields.get("Status");
                if (!e.deleted && "Pending".equalsIgnoreCase(status)) {
                    match = e;
                    matchIndex = i;
                    break;
                }
            }

            if (match == null) {
                System.out.println("No pending package found to assign.");
                return;
            }

            // 4) Update that block in memory
            for (int j = 0; j < match.rawLines.size(); j++) {
                String ln = match.rawLines.getElement(j).trim();
                if (ln.startsWith("Assigned to:")) {
                    match.rawLines.setElement(
                            j, "Assigned to: " + lastDriverID);
                } else if (ln.startsWith("Vehicle Registration:")) {
                    match.rawLines.setElement(
                            j, "Vehicle Registration: " + vehicleReg);
                } else if (ln.startsWith("Status:")) {
                    match.rawLines.setElement(j, "Status: In Transit");
                    match.fields.put("Status", "In Transit");
                }
            }

            // 5) Flatten + record start‐offsets
            CustomArrayList<String> allLines = new CustomArrayList<>();
            CustomArrayList<Integer> entryStarts = new CustomArrayList<>();
            int cursor = 0;

            for (int e = 0; e < entries.size(); e++) {
                TxtEntry te = entries.getElement(e);
                entryStarts.addElement(cursor);
                for (int r = 0; r < te.rawLines.size(); r++) {
                    allLines.addElement(te.rawLines.getElement(r));
                    cursor++;
                }
            }

            // 6) Overwrite only the matched block
            int writeStart = entryStarts.getElement(matchIndex);
            for (int j = 0; j < match.rawLines.size(); j++) {
                allLines.setElement(
                        writeStart + j,
                        match.rawLines.getElement(j));
            }

            // 7) Persist everything back
            pkgManager.writeAllLines(allLines);
            System.out.println("Package updated to 'In Transit' for driver ID: "
                    + lastDriverID);

        } catch (IOException ioe) {
            System.out.println("Error processing packageDetails.txt: "
                    + ioe.getMessage());
        }
    }

    public CustomArrayList<String> findFirstMatchingBlock(
            DoublyLinkedList<String> lines,
            String status,
            String driverID) {
        for (int i = 0; i < lines.size(); i++) {
            String header = lines.getElement(i).trim();
            if (!header.startsWith("Entry")) {
                continue;
            }

            // Start a new block
            CustomArrayList<String> block = new CustomArrayList<>();
            block.addElement(lines.getElement(i)); // keep original formatting

            boolean statusMatch = false;
            boolean driverMatch = (driverID == null);
            boolean delivered = false;

            // Walk until delimiter or end of input
            int j = i + 1;
            for (; j < lines.size(); j++) {
                String raw = lines.getElement(j);
                String trimmed = raw.trim();
                block.addElement(raw);

                if (trimmed.startsWith("Status:")) {
                    if (trimmed.contains("Delivered")) {
                        delivered = true;
                    }
                    if (trimmed.contains(status)) {
                        statusMatch = true;
                    }
                } else if (trimmed.startsWith("Assigned to:") && driverID != null) {
                    String found = trimmed.substring("Assigned to:".length()).trim();
                    if (found.equalsIgnoreCase(driverID)) {
                        driverMatch = true;
                    }
                }
                // End of this block
                else if (trimmed.equals("----------")
                        || trimmed.isEmpty()) {
                    break;
                }
            }

            // If this block qualifies, return it
            if (!delivered && statusMatch && driverMatch) {
                return block;
            }

            // Otherwise skip ahead to continue searching from j
            i = j;
        }

        return null;
    }

    private void viewDriverActivity() throws IOException {
        System.out.println("\n=== View Driver Activity ===");
        System.out.print("Enter Driver ID: ");
        String driverID = scanner.nextLine().trim();

        // 1) Load driver details via TextEntryManager

        TxtEntry targetEntry = null;
        try {
            CustomArrayList<TxtEntry> entries = textEntryManager.readAllEntries(textEntryManager.lineStarter);
            for (int i = 0; i < entries.size(); i++) {
                TxtEntry e = entries.getElement(i);
                String idField = e.fields.get("Driver ID");
                if (!e.deleted && driverID.equalsIgnoreCase(idField)) {
                    targetEntry = e;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading drivers.txt: " + e.getMessage());
            return;
        }

        if (targetEntry == null) {
            System.out.println("Driver ID not found.");
            return;
        }

        // 2) Extract fields from TxtEntry
        String driverName = targetEntry.fields.get("Driver Name");
        if (driverName == null) {
            driverName = "Unknown";
        }

        String assignmentStatus = targetEntry.fields.get("Assigned");
        if (assignmentStatus == null) {
            assignmentStatus = "false";
        }

        String currentAssignment = targetEntry.fields.get("Assigned Routes");
        if (currentAssignment == null || currentAssignment.isEmpty()) {
            currentAssignment = "";
        } else {
            currentAssignment = currentAssignment.trim();
        }
        if (currentAssignment.isEmpty()) {
            currentAssignment = null;
        }

        // 3) Read activity log for delays and infractions
        String allRoutes = null;
        int delayCount = 0;
        String allInfractions = null;

        TxtEntry entry = assignmentOfDrivers.getEntry(driverID + "|");
        if (entry != null) {
            allRoutes = entry.getFieldValue("Route ID");
            delayCount = entry.tryGetInt("Delay Count", 0);
            allInfractions = entry.getFieldValue("Infractions");
        }

        // 4) Display results
        System.out.println("\nDriver Name: " + driverName);

        if (allRoutes != null && !allRoutes.isEmpty()) {
            System.out.println("All Routes: " + allRoutes);
        } else {
            System.out.println("No routes assigned yet.");
        }

        System.out.println("Assignment Status: " + assignmentStatus);

        if (currentAssignment != null) {
            System.out.println("Current Assignment: " + currentAssignment);
        }

        System.out.println("Number of Delays: " + delayCount);

        if (allInfractions != null) {
            System.out.println("Infractions: " + allInfractions);
        }
    }

    public static void main(String[] args) throws IOException {
        DriverAssignmentRefactor d = new DriverAssignmentRefactor(50);
        d.selectMenuItem();
    }

}