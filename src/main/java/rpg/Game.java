package rpg;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    public void startNewGame() {
        System.out.println("\n========== Character Creation ==========");
        System.out.println("Choose your class:");
        HeroClass[] classes = HeroClass.values();
        for (int i = 0; i < classes.length; i++) {
            HeroClass hc = classes[i];
            System.out.println((i + 1) + ". " + hc.getName() + " (HP: " + hc.getHp() + ", ATK: " + hc.getAttack() + ")");
        }
        System.out.print("Enter your choice (1-3): ");

        int classChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        HeroClass heroClass = null;
        switch (classChoice) {
            case 1:
                heroClass = HeroClass.WARRIOR;
                break;
            case 2:
                heroClass = HeroClass.ARCHER;
                break;
            case 3:
                heroClass = HeroClass.MAGE;
                break;
            default:
                System.out.println("Invalid choice! Defaulting to Warrior...");
                heroClass = HeroClass.WARRIOR;
                break;
        }

        System.out.print("Enter your character name: ");
        String playerName = scanner.nextLine();

        // Create player with selected class stats
        player = new Player(playerName, heroClass.getHp(), heroClass.getAttack());

        System.out.println("\nWelcome, " + playerName + " the " + heroClass.getName() + "!");
        System.out.println(player);

        start();
    }

    public void loadGame() {
        System.out.println("Load game feature coming soon...");
    }

    public void start() {
        System.out.println("\nWelcome to Simple RPG");

        boolean running = true;
        while (running && player.isAlive()) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Stats");
            System.out.println("2. Inventory");
            System.out.println("3. Explore");
            System.out.println("4. Quit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(player);
                    break;
                case 2:
                    System.out.println("Inventory: TODO");
                    break;
                case 3:
                    handleExplore();
                    break;
                case 4:
                    System.out.println("Quitting game.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }

        if (!player.isAlive()) {
            System.out.println("You died!");
        }
    }

    private void handleExplore() {
        int roll = random.nextInt(100) + 1; // 1..100
        if (roll <= 50) { // 50%
            System.out.println("You explore the area but find nothing.");
        } else if (roll <= 50 + 35) { // next 35% => up to 85
            Enemy enemy = Enemy.generateRandomEnemy();
            System.out.println("\nWhile exploring, a " + enemy.getName() + " appears!");
            System.out.println(enemy);
            System.out.print("Do you want to fight? (1=Yes, 2=No): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                fight(enemy);
            } else {
                System.out.println("You avoid the fight and move on.");
            }
        } else { // remaining 15%
            System.out.println("You found a lootbox! (TODO: implement lootbox)");
        }
    }

    private void fight(Enemy enemy) {
        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("Press ENTER to attack...");
            scanner.nextLine();
            enemy.takeDamage(player.getAttack());
            System.out.println("You hit the " + enemy.getName());
            if (!enemy.isAlive()) break;
            player.takeDamage(enemy.getAttack());
            System.out.println(enemy.getName() + " hits you!");
        }

        if (player.isAlive()) {
            System.out.println("You won!");
            player.gainExp(50); // Award 50 EXP for defeating the enemy
            System.out.println("Gained 50 EXP!");
            System.out.println(player);
        }
        else System.out.println("You died!");
    }
}