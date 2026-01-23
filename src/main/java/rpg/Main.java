
package rpg;

import java.util.Scanner;

/**
 * Main entry point for the RPG game application.
 * Provides a menu for starting a new game or loading a saved game.
 */
public class Main {
    /**
     * Main method that launches the RPG game.
     * Displays a menu for the user to choose between starting a new game or loading a saved game.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputReader input = new ConsoleInputReader(scanner);
        
        System.out.println("========== RPG Game Menu ==========");
        System.out.println("1. Start new game");
        System.out.println("2. Continue (load save file)");
        System.out.println("===================================");
        int choice = input.readInt("Choose an option (1 or 2): ", 1, 2);
        
        Game game = new Game(input, scanner);
        
        switch (choice) {
            case 1 -> game.startNewGame();
            case 2 -> game.loadGame();
            default -> {
                System.out.println("Invalid choice! Starting new game...");
                game.startNewGame();
            }
        }
    }
}