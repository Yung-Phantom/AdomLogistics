package AdomLogisticsConsoleApp.ObjectClass;

import java.util.Scanner;

/**
 * Utility class for validating different types of user input from the console.
 * <p>
 * This class centralizes interactive input validation for integers, doubles,
 * and strings,
 * ensuring that only acceptable values are returned to the caller.
 * Validation loops continue prompting the user until correct input is entered.
 * </p>
 *
 * <h3>Usage Notes:</h3>
 * <ul>
 * <li>Most methods prompt and re-prompt until a valid value is provided.</li>
 * <li>Supports validation for:
 * <ul>
 * <li>Menu/range-based integer choices</li>
 * <li>Positive or non-negative doubles</li>
 * <li>Non-empty strings</li>
 * </ul>
 * </li>
 * <li>Some methods accept a {@link Scanner} parameter; others use the instance
 * field {@code scanner}.</li>
 * <li>For {@link #readMenuChoice(int)}, ensure {@link #scanner} is initialized
 * or set.</li>
 * </ul>
 */
public class InputValidity {

    /** Stores the most recent integer entered by the user. */
    private int userInput;

    /** Boolean flag indicating whether the last validation succeeded. */
    private boolean trueFalse;

    /** Stores the most recent double entered by the user. */
    private double userDoubleInput;

    /** Stores the most recent string entered by the user. */
    private String userStringInput;

    /** Scanner instance used by methods without explicit Scanner parameters. */
    private Scanner scanner;

    /**
     * Reads and validates an integer input between 0 and {@code i}, inclusive.
     * <p>
     * Continues prompting until a valid number is entered.
     * Entering 0 may be used by calling code to trigger menu display.
     * </p>
     *
     * @param scanner the Scanner to read from
     * @param i       the maximum valid integer (inclusive)
     * @return the validated integer entered by the user
     */
    public int validity(Scanner scanner, int i) {
        while (true) {
            System.out.print("Please enter your choice (1 - " + i + "). Enter 0 to view the menu: ");
            try {
                String s = scanner.nextLine().trim(); // read and trim whitespace
                userInput = Integer.parseInt(s); // parse to integer
                if (userInput >= 0 && userInput <= i) { // check range
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

    /**
     * Reads and validates a non-negative double input from the user.
     * <p>
     * Continues prompting until the input is a valid double greater than or equal
     * to zero.
     * </p>
     *
     * @param scanner the Scanner to read from
     * @return the validated double value
     */
    public double validityDouble(Scanner scanner) {
        while (true) {
            try {
                String s = scanner.nextLine().trim(); // read and trim input
                userDoubleInput = Double.parseDouble(s); // parse to double
                if (userDoubleInput >= 0) { // non-negative check
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

    /**
     * Reads and validates a string input from the user, ensuring it matches a
     * specific regular expression (10-16 digits).
     * <p>
     * Continues prompting until the input is a valid phone number.
     * </p>
     *
     * @param scanner the Scanner to read from
     */
    public String validityPhoneString(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            if (s.matches("\\d{10,16}")) {
                userStringInput = s;
                trueFalse = true;
                break;
            } else {
                System.out.println("Invalid phone number. Enter 10–16 digits only.");
            }
        }
        return userStringInput;
    }

    /**
     * Reads and validates a non-empty string from the user.
     * <p>
     * Trims whitespace to determine emptiness but returns the raw user-entered
     * string.
     * </p>
     *
     * @param scanner the Scanner to read from
     * @return the validated, non-empty string
     */
    public String validityString(Scanner scanner) {
        while (true) {
            try {
                userStringInput = scanner.nextLine(); // read full line (without trimming)
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

    /**
     * Prompts the user with a message until they provide a non-empty string.
     * <p>
     * Input is trimmed before checking for emptiness.
     * </p>
     *
     * @param scanner the Scanner to read from
     * @param prompt  the message displayed to the user before reading input
     * @return a trimmed, non-empty string entered by the user
     */
    public String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty())
                return s; // return only if non-empty
            System.out.println("Invalid input. Please enter a non-empty string.");
        }
    }

    /**
     * Prompts the user with a message until they provide a non-negative double.
     * <p>
     * Trims and parses the input, re-prompting if invalid or negative.
     * </p>
     *
     * @param scanner the Scanner to read from
     * @param prompt  the message displayed to the user before reading input
     * @return the validated non-negative double
     */
    public double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = scanner.nextLine().trim();
            try {
                double v = Double.parseDouble(s);
                if (v >= 0)
                    return v; // allow zero and positives
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid number. Please enter a positive double.");
        }
    }

    /**
     * Reads a menu choice from the user using the instance {@link #scanner} field.
     * <p>
     * Wraps {@link #validity(Scanner, int)} in its own loop for additional
     * guarding.
     * </p>
     *
     * @param i the maximum valid integer choice (inclusive)
     * @return the validated menu choice
     */
    public int readMenuChoice(int i) {
        while (true) {
            int v = validity(scanner, i); // validate using stored scanner
            try {
                if (v >= 0 && v <= i)
                    return v; // range check
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid number. Enter 0 to view menu.");
        }
    }
}
