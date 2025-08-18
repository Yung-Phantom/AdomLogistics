package AdomLogisticsConsoleApp.ObjectClass;

import java.util.Scanner;

public class InputValidity {

    private int userInput;
    private boolean trueFalse;
    private double userDoubleInput;
    private String userStringInput;
    private Scanner scanner;

    public int validity(Scanner scanner, int i) {
        while (true) {
            System.out.print("Please enter your choice (0 - " + i + "): ");
            try {
                String s = scanner.nextLine().trim();
                userInput = Integer.parseInt(s);
                if (userInput >= 0 && userInput <= i) {
                    trueFalse = true;
                    break;
                }
                System.out.println("Invalid number. Try again. Enter 0 to view the menu");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number. Enter 0 to view the menu");
            }
        }
        return userInput;
    }
    
    public double validityDouble(Scanner scanner) {
        while (true) {
            try {
                String s = scanner.nextLine().trim();
                userDoubleInput = Double.parseDouble(s);
                if (userDoubleInput >= 0) {
                    trueFalse = true;
                    break;
                }
                System.out.println("Invalid number. Please enter a positive double.");
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid double.");
            }
        }
        return userDoubleInput;
    }
    
    public String validityString(Scanner scanner) {
        while (true) {
            try {
                userStringInput = scanner.nextLine();
                if (userStringInput != null && userStringInput.trim().length() > 0) {
                    trueFalse = true;
                    break;
                }
                System.out.println("Invalid input. Please enter a non-empty string.");
            } catch (Exception e) {
                System.out.println(e + ". Invalid input. Please enter a valid string.");
            }
        }
        return userStringInput;
    }

    public String readNonEmptyLine(Scanner scanner,String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Invalid input. Please enter a non-empty string.");
        }
    }

    public double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = scanner.nextLine().trim();
            try {
                double v = Double.parseDouble(s);
                if (v >= 0) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid number. Please enter a positive double.");
        }
    }

    public int readMenuChoice(int i) {
        while (true) {
            int v = validity(scanner, i);
            try {
                if (v >= 0 && v <= i) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid number. Enter 0 to view menu.");
        }
    }
}
