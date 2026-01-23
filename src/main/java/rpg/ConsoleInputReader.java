package rpg;

import java.util.Scanner;

/**
 * Console-based implementation of InputReader.
 * Reads integer input from the console using a Scanner.
 */
public class ConsoleInputReader implements InputReader {
    private final Scanner scanner;

    /**
     * Constructs a ConsoleInputReader with the specified scanner.
     *
     * @param scanner the scanner to use for reading input
     */
    public ConsoleInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads an integer from the console with the specified prompt.
     * Continuously prompts until a valid integer is entered.
     *
     * @param prompt the message to display to the user
     * @return the integer entered by the user
     */
    @Override
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number.");
            }
        }
    }

    /**
     * Reads an integer from the console within a specified range.
     * Continuously prompts until a valid integer within the range is entered.
     *
     * @param prompt the message to display to the user
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @return the integer entered by the user, guaranteed to be within [min, max]
     */
    @Override
    public int readInt(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val < min || val > max) {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } else {
                return val;
            }
        }
    }

    /**
     * Gets the scanner used by this input reader.
     *
     * @return the scanner instance
     */
    public Scanner getScanner() { return scanner; }
}
