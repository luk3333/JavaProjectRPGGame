
package rpg;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========== RPG Game Menu ==========");
        System.out.println("1. Start new game");
        System.out.println("2. Continue (load save file)");
        System.out.println("===================================");
        System.out.print("Choose an option (1 or 2): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Game game = new Game();
        
        switch (choice) {
            case 1:
                game.startNewGame();
                break;
            case 2:
                game.loadGame();
                break;
            default:
                System.out.println("Invalid choice! Starting new game...");
                game.startNewGame();
                break;
        }
    }
}