package rpg;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    // percent chance (0-100) to drop an item after winning a fight
    public static int ITEM_DROP_CHANCE = 100;

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

        // Create player with selected class stats and inventory capacity
        player = new Player(playerName, heroClass.getHp(), heroClass.getAttack(), heroClass.getInventorySlots());

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
                    System.out.println("Inventory (" + player.getInventoryCount() + "/" + player.getInventoryCapacity() + "):");
                    if (player.getInventory().isEmpty()) {
                        System.out.println("  (empty)");
                    } else {
                        int idx = 1;
                        for (Item it : player.getInventory()) {
                            String mark = "";
                            if (player.getEquippedWeaponIndex() == (idx - 1) && it instanceof Weapon) {
                                mark = " (equipped weapon)";
                            } else if (player.getEquippedArmorIndex() == (idx - 1) && it instanceof Armor) {
                                mark = " (equipped armor)";
                            }
                            System.out.println("  " + (idx++) + ". " + it + mark);
                        }

                        System.out.print("Enter item number to manage (0 to go back): ");
                        int useChoice = scanner.nextInt();
                        scanner.nextLine();
                        if (useChoice > 0 && useChoice <= player.getInventoryCount()) {
                            Item chosen = player.getInventory().get(useChoice - 1);
                            if (chosen instanceof Potion) {
                                Potion p = (Potion) chosen;
                                System.out.print("Use " + p.getName() + " and heal " + p.getHealAmount() + " HP? (1=Yes, 2=No): ");
                                int confirm = scanner.nextInt();
                                scanner.nextLine();
                                if (confirm == 1) {
                                    int heal = p.getHealAmount();
                                    boolean ok = player.usePotionByIndex(useChoice - 1);
                                    if (ok) System.out.println("You used " + p.getName() + " and healed " + heal + " HP.");
                                    else System.out.println("Failed to use the potion.");
                                } else {
                                    System.out.println("Cancelled.");
                                }
                            } else if (chosen instanceof Weapon) {
                                int eqIndex = player.getEquippedWeaponIndex();
                                if (eqIndex == (useChoice - 1)) {
                                    System.out.print("This weapon is currently equipped. Unequip it? (1=Yes, 2=No): ");
                                    int c = scanner.nextInt();
                                    scanner.nextLine();
                                    if (c == 1) {
                                        player.unequipWeapon();
                                        System.out.println("Weapon unequipped.");
                                    } else {
                                        System.out.println("Cancelled.");
                                    }
                                } else {
                                    System.out.print("Equip " + chosen.getName() + "? (1=Yes, 2=No): ");
                                    int c = scanner.nextInt();
                                    scanner.nextLine();
                                    if (c == 1) {
                                        boolean ok = player.equipWeaponAt(useChoice - 1);
                                        if (ok) System.out.println("Equipped " + chosen.getName() + ".");
                                        else System.out.println("Failed to equip.");
                                    } else {
                                        System.out.println("Cancelled.");
                                    }
                                }
                            } else if (chosen instanceof Armor) {
                                int eqIndex = player.getEquippedArmorIndex();
                                if (eqIndex == (useChoice - 1)) {
                                    System.out.print("This armor is currently equipped. Unequip it? (1=Yes, 2=No): ");
                                    int c = scanner.nextInt();
                                    scanner.nextLine();
                                    if (c == 1) {
                                        player.unequipArmor();
                                        System.out.println("Armor unequipped.");
                                    } else {
                                        System.out.println("Cancelled.");
                                    }
                                } else {
                                    System.out.print("Equip " + chosen.getName() + "? (1=Yes, 2=No): ");
                                    int c = scanner.nextInt();
                                    scanner.nextLine();
                                    if (c == 1) {
                                        boolean ok = player.equipArmorAt(useChoice - 1);
                                        if (ok) System.out.println("Equipped " + chosen.getName() + ".");
                                        else System.out.println("Failed to equip.");
                                    } else {
                                        System.out.println("Cancelled.");
                                    }
                                }
                            } else {
                                System.out.println("You can't use that item right now.");
                            }
                        }
                    }
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
            System.out.println("You found a lootbox!");
                Item drop = Item.createRandom(random);
            System.out.println("Inside you find: " + drop);
            offerItemToPlayer(drop);
        }
    }

    private void fight(Enemy enemy) {
        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\nChoose action:");
            System.out.println("1. Attack");
            System.out.println("2. Use potion");
            System.out.print("Enter choice: ");
            int act = scanner.nextInt();
            scanner.nextLine();

            if (act == 1) {
                enemy.takeDamage(player.getAttack());
                System.out.println("You hit the " + enemy.getName());
                if (!enemy.isAlive()) break;
                player.takeDamage(enemy.getAttack());
                System.out.println(enemy.getName() + " hits you!");
            } else if (act == 2) {
                // find a potion
                int potionIndex = -1;
                for (int i = 0; i < player.getInventory().size(); i++) {
                    if (player.getInventory().get(i) instanceof Potion) {
                        potionIndex = i;
                        break;
                    }
                }
                if (potionIndex == -1) {
                    System.out.println("You have no potions to use.");
                    continue;
                }
                Item pot = player.getInventory().get(potionIndex);
                if (pot instanceof Potion) {
                    Potion p = (Potion) pot;
                    System.out.print("Use " + p.getName() + " and heal " + p.getHealAmount() + " HP? (1=Yes, 2=No): ");
                    int conf = scanner.nextInt();
                    scanner.nextLine();
                    if (conf == 1) {
                        int healed = p.getHealAmount();
                        boolean ok = player.usePotionByIndex(potionIndex);
                        if (ok) System.out.println("You used " + p.getName() + " and healed " + healed + " HP.");
                        else System.out.println("Failed to use the potion.");
                        // using potion consumes the turn -> enemy attacks if alive
                        if (enemy.isAlive()) {
                            player.takeDamage(enemy.getAttack());
                            System.out.println(enemy.getName() + " hits you while you used the potion!");
                        }
                    } else {
                        System.out.println("You decided not to use the potion.");
                    }
                }
            } else {
                System.out.println("Invalid action, try again.");
            }
        }

        if (player.isAlive()) {
            System.out.println("You won!");
            player.gainExp(50); // Award 50 EXP for defeating the enemy
            System.out.println("Gained 50 EXP!");
            System.out.println(player);

            // Item drop logic (configurable chance)
            int roll = random.nextInt(100) + 1;
            if (roll <= ITEM_DROP_CHANCE) {
                    Item drop = Item.createRandom(random);
                System.out.println("You found an item: " + drop);
                offerItemToPlayer(drop);
            } else {
                System.out.println("No items dropped this time.");
            }
        }
        else System.out.println("You died!");
    }

    

    // Offer the given item to the player: ask to take, add to inventory or offer replacement if full.
    private void offerItemToPlayer(Item drop) {
        System.out.print("Take it? (1=Yes, 2=No): ");
        int take = scanner.nextInt();
        scanner.nextLine();
        if (take == 1) {
            boolean added = player.addItem(drop);
            if (added) {
                System.out.println(drop + " added to inventory.");
            } else {
                System.out.println("Your inventory is full. Do you want to replace an existing item?");
                System.out.println("Inventory:");
                int idx = 1;
                for (Item it : player.getInventory()) {
                    System.out.println("  " + (idx++) + ". " + it);
                }
                System.out.print("Enter item number to replace with " + drop + " (0 to cancel): ");
                int replaceChoice = scanner.nextInt();
                scanner.nextLine();
                    if (replaceChoice > 0 && replaceChoice <= player.getInventoryCount()) {
                    Item removed = player.getInventory().get(replaceChoice - 1);
                    System.out.print("Replace " + removed + " with " + drop + "? (1=Yes, 2=No): ");
                    int conf2 = scanner.nextInt();
                    scanner.nextLine();
                    if (conf2 == 1) {
                        boolean replaced = player.replaceItemAt(replaceChoice - 1, drop);
                        if (replaced) {
                            System.out.println("Replaced " + removed + " with " + drop + ".");
                        } else {
                            System.out.println("Failed to replace the selected item. " + drop + " discarded.");
                        }
                    } else {
                        System.out.println("Replacement cancelled. " + drop + " discarded.");
                    }
                } else {
                    System.out.println("No replacement chosen. " + drop + " discarded.");
                }
            }
        } else {
            System.out.println("You discarded " + drop + ".");
        }
    }
}