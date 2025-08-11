package AdomLogisticsConsoleApp;

import java.io.File;
import java.util.Scanner;

import CustomDataStructures.CustomArrayList;

public class DriverAssignment {

    BannerElements bElements;
    BannerElements addDriver;
    BannerElements searchByElements;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;
    String userStringInput;
    double userDoubleInput;
    int userIntInput;

    File jsonStorageDir;
    File jsonStorage;

    CustomArrayList<Object> addDriverDetails;
    CustomArrayList<Object> addNewEntry;

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
        addDriver.addToMenu("Please enter the maintenance history of the vehicle.");
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
                    updateDriverActivity();
                    break;
                case 6:
                    System.out.println("Exiting Adom Logistics System. Goodbye!");
                    scanner.close();
                    break;

            }
        }
    }

    private void assignDriverMenu() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'assignDriverMenu'");
    }

    private void updateDriverActivity() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'updateDriverActivity'");
    }

    private void removeDriver() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'removeDriver'");
    }

    private void addNewDriver() {
        addDriverDetails = new CustomArrayList<>();
        addNewEntry = new CustomArrayList<>();

        for (int i = 0; i < addDriver.size(); i++) {
            System.out.println(addDriver.getElement(i));
            switch (i) {
                case 0:
                case 1:
                case 3:
                case 4:
                case 5:
                    validityString(scanner);
                    addDriverDetails.addElement(userStringInput);
                    break;
                case 2:
                    validityInt(scanner);
                    addDriverDetails.addElement(userIntInput);
            }
        }
        System.out.println("Operation successful");
        for (int index = 0; index < addDriverDetails.size(); index++) {
            if (index == 0) {
                addNewEntry.addElement("Driver Name: " + addDriverDetails.getElement(index));
            } else if (index == 1) {
                addNewEntry.addElement("Driver's license: " + addDriverDetails.getElement(index));
            } else if (index == 2) {
                addNewEntry.addElement("Driver's phone number: " + addDriverDetails.getElement(index));
            } else if (index == 3) {
                addNewEntry.addElement("Driver's email address: " +
                        addDriverDetails.getElement(index));
            } else if (index == 4) {
                addNewEntry.addElement("Driver address: " + addDriverDetails.getElement(index));
            } else if (index == 5) {
                addNewEntry.addElement("Driver maintenance history: " +
                        addDriverDetails.getElement(index));
            }
        }
        // saveJSON(addDriverDetails);
        for (int i = 0; i < addNewEntry.size(); i++) {
            System.out.println(addNewEntry.getElement(i));
        }
    }

    private void validityInt(Scanner scanner2) {
        while (true) {
            scanner.nextLine(); // clear the buffer
            try {
                userIntInput = scanner.nextInt();
                if (userIntInput >= 0) {
                    trueFalse = true;
                    break;
                } else {
                    System.out.println("Invalid number. Please enter a positive integer.");
                }
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid integer.");
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
        DriverAssignment driverAssignment = new DriverAssignment(50);
        driverAssignment.selectMenuItem();
    }
}
// Driver Assignment
// o Manage a stack or queue of available drivers.
// o Drivers are assigned based on proximity or experience.
// o Track driver activity: assigned routes, recent delays, infractions.
