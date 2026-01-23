package rpg;

/**
 * Interface for reading integer input from the user.
 * Provides methods for basic input and validated range input.
 */
public interface InputReader {
    /**
     * Reads an integer from the user with the specified prompt.
     *
     * @param prompt the message to display to the user
     * @return the integer entered by the user
     */
    int readInt(String prompt);
    
    /**
     * Reads an integer from the user within a specified range.
     *
     * @param prompt the message to display to the user
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @return the integer entered by the user, guaranteed to be within [min, max]
     */
    int readInt(String prompt, int min, int max);
}
