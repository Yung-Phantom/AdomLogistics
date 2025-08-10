package AdomLogisticsConsoleApp;

import java.util.Scanner;

public class AdomBanner {
    BannerElements bElements;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    int width;

    public AdomBanner(int width) {
        this.width = width;
        bElements = new BannerElements(width, "Adom Logistics Main Menu");
        bElements.printBanner();
        bElements.addToMenu("Vehicle Database");
        bElements.addToMenu("Driver Assignment");
        bElements.addToMenu("Delivery Tracking");
        bElements.addToMenu("Maintenance Scheduler");
        bElements.addToMenu("Fuel Efficiency Reports");
        bElements.addToMenu("File Storage");
        bElements.addToMenu("Search & Sort Features");
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
                    VehicleDataBase vehicleDataBase = new VehicleDataBase(width);
                    vehicleDataBase.selectMenuItem();
                    break;
                case 2:
                    DriverAssignment driverAssignment = new DriverAssignment(width);
                    driverAssignment.selectMenuItem();
                    break;
                case 3:
                    DeliveryTracking deliveryTracking = new DeliveryTracking(width);
                    deliveryTracking.selectMenuItem();
                    break;
                case 4:
                    MaintenanceScheduler maintenanceScheduler = new MaintenanceScheduler(width);
                    maintenanceScheduler.selectMenuItem();
                    break;
                case 5:
                    FuelEfficiencyReports fuelEfficiencyReports = new FuelEfficiencyReports(width);
                    fuelEfficiencyReports.selectMenuItem();
                    break;
                case 6:
                    FileStorage fileStorage = new FileStorage(width);
                    fileStorage.selectMenuItem();
                    break;
                case 7:
                    SearchSortFeatures searchSortFeatures = new SearchSortFeatures(width);
                    searchSortFeatures.selectMenuItem();
                    break;
                case 8:
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

}
