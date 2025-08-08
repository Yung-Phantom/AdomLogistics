package AdomLogisticsConsoleApp;

import java.util.Scanner;

public class DriverAssignment {
    
    BannerElements bElements;
    BannerElements searchByElements;

    Scanner scanner = new Scanner(System.in);
    boolean trueFalse;
    int userInput;

    public DriverAssignment(int width) {
        bElements = new BannerElements(width, "Adom Logistics Vehicle Database");
        bElements.printBanner();
        bElements.addToMenu("Add new vehicle");
        bElements.addToMenu("Remove vehicle");
        bElements.addToMenu("Search for vehicle");
        bElements.addToMenu("Exit");
        bElements.printMenu();
    }

    public void selectMenuItem() {
        // call the validity method to get the boolean and int that will be used for the
        validity(scanner, 4);
        boolean choice = trueFalse;
        int choiceInput = userInput;

        if (choice) {
            switch (choiceInput) {
                case 0:
                    bElements.printMenu();
                    selectMenuItem();
                    break;
                case 1:
                    ;
                    break;
                case 2:
                    ;
                    break;
                case 3:
                    ;
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

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
