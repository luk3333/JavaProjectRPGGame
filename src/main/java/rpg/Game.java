package rpg;

import java.util.Random;
import java.util.Scanner;

/**
 * Main game controller that manages the RPG game flow.
 * Handles character creation, combat, exploration, inventory management, and saving/loading.
 */
public class Game {
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    private InputReader inputReader = new ConsoleInputReader(scanner);
    private final SaveGameManager saveGameManager = new SaveGameManager();
    
    /** Percent chance (0-100) to drop an item after winning a fight */
    public static int ITEM_DROP_CHANCE = 100;

    /**
     * Default constructor for Game.
     */
    public Game() {}

    /**
     * Constructor for Game with custom input reader and scanner.
     *
     * @param inputReader the input reader to use
     * @param scanner the scanner to use
     */
    public Game(InputReader inputReader, Scanner scanner) {
        this.scanner = scanner;
        this.inputReader = inputReader;
    }

    /** Gets the player. @return the current player */
    public Player getPlayer() { return player; }
    
    /** Sets the player. @param player the player to set */
    public void setPlayer(Player player) { this.player = player; }
    
    /** Gets the scanner. @return the scanner */
    public Scanner getScanner() { return scanner; }
    
    /** Sets the scanner. @param scanner the scanner to set */
    public void setScanner(Scanner scanner) { this.scanner = scanner; }
    
    /** Gets the random number generator. @return the random generator */
    public Random getRandom() { return random; }
    
    /** Sets the random number generator. @param random the random generator to set */
    public void setRandom(Random random) { this.random = random; }
    
    /** Gets the input reader. @return the input reader */
    public InputReader getInputReader() { return inputReader; }
    
    /** Sets the input reader. @param inputReader the input reader to set */
    public void setInputReader(InputReader inputReader) { this.inputReader = inputReader; }
    
    /** Gets the item drop chance. @return the drop chance percentage */
    public static int getItemDropChance() { return ITEM_DROP_CHANCE; }
    
    /** Sets the item drop chance. @param chance the new drop chance percentage */
    public static void setItemDropChance(int chance) { ITEM_DROP_CHANCE = chance; }

    /**
     * Starts a new game with character creation.
     * Prompts the user to select a class and name, then starts the main game loop.
     */
    public void startNewGame() {
        System.out.println("\n========== Character Creation ==========");
        System.out.println("Choose your class:");
        HeroClass[] classes = HeroClass.values();
        for (int i = 0; i < classes.length; i++) {
            HeroClass hc = classes[i];
            System.out.println((i + 1) + ". " + hc.getName() + " (HP: " + hc.getHp() + ", ATK: " + hc.getAttack() + ")");
        }
        System.out.print("Enter your choice (1-3): ");
        int classChoice = inputReader.readInt("", 1, 3);

        HeroClass heroClass;
        switch (classChoice) {
            case 1 -> heroClass = HeroClass.WARRIOR;
            case 2 -> heroClass = HeroClass.ARCHER;
            case 3 -> heroClass = HeroClass.MAGE;
            default -> {
                System.out.println("Invalid choice! Defaulting to Warrior...");
                heroClass = HeroClass.WARRIOR;
            }
        }

        System.out.print("Enter your character name: ");
        String playerName = scanner.nextLine();

        player = new Player(playerName, heroClass.getHp(), heroClass.getAttack(), heroClass.getInventorySlots());

        System.out.println("\nWelcome, " + playerName + " the " + heroClass.getName() + "!");
        System.out.println(player);

        start();
    }

    /**
     * Loads a saved game from a file.
     * Prompts for the save file name and loads the player data.
     */
    public void loadGame() {
        System.out.print("Enter save filename to load (default: savegame.json): ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) fileName = "savegame.json";

        Player loadedPlayer = saveGameManager.loadGame(fileName);
        if (loadedPlayer != null) {
            this.player = loadedPlayer;
            start();
        }
    }

    /**
     * Starts the main game loop.
     * Displays the main menu and handles player actions until game ends.
     */
    public void start() {
        System.out.println("\nWelcome to Simple RPG");

        boolean running = true;
        while (running && player.isAlive()) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Stats");
            System.out.println("2. Inventory");
            System.out.println("3. Explore");
            System.out.println("4. Save & Quit");
            System.out.print("Enter choice: ");
            int choice = inputReader.readInt("");

            switch (choice) {
                case 1 -> handleStats();
                case 2 -> handleInventory();
                case 3 -> handleExplore();
                case 4 -> running = handleSaveAndQuit();
                default -> System.out.println("Invalid choice, try again.");
            }
        }

        if (!player.isAlive()) {
            System.out.println("You died!");
        }
    }

    /**
     * Handles the stats menu option.
     * Displays the player's current statistics.
     */
    private void handleStats() {
        System.out.println(player);
    }

    /**
     * Handles the inventory menu option.
     * Displays inventory and allows managing items.
     */
    private void handleInventory() {
        System.out.println("Inventory (" + player.getInventoryCount() + "/" + player.getInventoryCapacity() + "):");
        if (player.getInventory().isEmpty()) {
            System.out.println("  (empty)");
            return;
        }

        displayInventoryItems();
        
        System.out.print("Enter item number to manage (0 to go back): ");
        int useChoice = inputReader.readInt("");
        if (useChoice > 0 && useChoice <= player.getInventoryCount()) {
            manageInventoryItem(useChoice - 1);
        }
    }

    /**
     * Displays all items in the inventory with equipment markers.
     */
    private void displayInventoryItems() {
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
    }

    /**
     * Manages an item at a specific inventory index.
     * Routes to appropriate handler based on item type.
     *
     * @param itemIndex the inventory index of the item
     */
    private void manageInventoryItem(int itemIndex) {
        Item chosen = player.getInventory().get(itemIndex);
        boolean isEquippedWeapon = chosen instanceof Weapon && player.getEquippedWeaponIndex() == itemIndex;
        boolean isEquippedArmor = chosen instanceof Armor && player.getEquippedArmorIndex() == itemIndex;

        switch (chosen) {
            case Potion p -> handlePotionUse(p);
            case Weapon weapon -> handleWeaponEquipment(weapon, isEquippedWeapon);
            case Armor armor -> handleArmorEquipment(armor, isEquippedArmor);
            default -> System.out.println("You can't use that item right now.");
        }
    }

    /**
     * Handles using a potion from inventory.
     *
     * @param p the potion to use
     */
    private void handlePotionUse(Potion p) {
        System.out.print("Use " + p.getName() + " and heal " + p.getHealAmount() + " HP? (1=Yes, 2=No): ");
        int confirm = inputReader.readInt("");
        if (confirm == 1) {
            int heal = p.getHealAmount();
            boolean ok = player.consumePotion(p);
            if (ok) {
                System.out.println("You used " + p.getName() + " and healed " + heal + " HP.");
            } else {
                System.out.println("Failed to use the potion.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }

    /**
     * Handles equipping or unequipping a weapon.
     *
     * @param weapon the weapon to manage
     * @param isEquipped whether the weapon is currently equipped
     */
    private void handleWeaponEquipment(Weapon weapon, boolean isEquipped) {
        if (isEquipped) {
            System.out.print("This weapon is currently equipped. Unequip it? (1=Yes, 2=No): ");
            int c = inputReader.readInt("");
            if (c == 1) {
                player.unequipWeapon();
                System.out.println("Weapon unequipped.");
            } else {
                System.out.println("Cancelled.");
            }
        } else {
            System.out.print("Equip " + weapon.getName() + "? (1=Yes, 2=No): ");
            int c = inputReader.readInt("");
            if (c == 1) {
                weapon.use(player);
                System.out.println("Equipped " + weapon.getName() + ".");
            } else {
                System.out.println("Cancelled.");
            }
        }
    }

    /**
     * Handles equipping or unequipping armor.
     *
     * @param armor the armor to manage
     * @param isEquipped whether the armor is currently equipped
     */
    private void handleArmorEquipment(Armor armor, boolean isEquipped) {
        if (isEquipped) {
            System.out.print("This armor is currently equipped. Unequip it? (1=Yes, 2=No): ");
            int c = inputReader.readInt("");
            if (c == 1) {
                player.unequipArmor();
                System.out.println("Armor unequipped.");
            } else {
                System.out.println("Cancelled.");
            }
        } else {
            System.out.print("Equip " + armor.getName() + "? (1=Yes, 2=No): ");
            int c = inputReader.readInt("");
            if (c == 1) {
                armor.use(player);
                System.out.println("Equipped " + armor.getName() + ".");
            } else {
                System.out.println("Cancelled.");
            }
        }
    }

    /**
     * Handles saving the game and quitting.
     *
     * @return false to stop the game loop
     */
    private boolean handleSaveAndQuit() {
        System.out.print("Save file name (default: savegame.json): ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) fileName = "savegame.json";
        saveGameManager.saveGame(player, fileName);
        System.out.println("Quitting game.");
        return false;
    }

    /**
     * Handles the explore action.
     * Randomly generates encounters: nothing, enemy, or lootbox.
     */
    private void handleExplore() {
        int roll = random.nextInt(100) + 1; // 1..100
        if (roll <= 50) { // 50%
            System.out.println("You explore the area but find nothing.");
        } else if (roll <= 50 + 35) { // next 35% => up to 85
            Enemy enemy = Enemy.generateRandomEnemy();
            System.out.println("\nWhile exploring, a " + enemy.getName() + " appears!");
            System.out.println(enemy);
            System.out.print("Do you want to fight? (1=Yes, 2=No): ");
            int choice = inputReader.readInt("");
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

    /**
     * Handles a fight with an enemy.
     * Continues until either player or enemy is defeated.
     *
     * @param enemy the enemy to fight
     */
    private void fight(Enemy enemy) {
        OUTER:
        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\nChoose action:");
            System.out.println("1. Attack");
            System.out.println("2. Use potion");
            System.out.print("Enter choice: ");
            int act = inputReader.readInt("");
            switch (act) {
                case 1 -> {
                    enemy.takeDamage(player.getAttack());
                    System.out.println("You hit the " + enemy.getName());
                    if (!enemy.isAlive()) {
                        break OUTER;
                    }
                    player.takeDamage(enemy.getAttack());
                    System.out.println(enemy.getName() + " hits you!");
                }
                case 2 -> usePotionInCombat(enemy);
                default -> System.out.println("Invalid action, try again.");
            }
        }

        if (player.isAlive()) {
            System.out.println("You won!");
            player.gainExp(50); // Award 50 EXP for defeating the enemy
            System.out.println("Gained 50 EXP!");
            System.out.println(player);

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

    /**
     * Handles using a potion during combat.
     *
     * @param enemy the enemy being fought
     * @return false if no potion available, true otherwise
     */
    private boolean usePotionInCombat(Enemy enemy) {
        int potionIndex = -1;
        for (int i = 0; i < player.getInventory().size(); i++) {
            if (player.getInventory().get(i) instanceof Potion) {
                potionIndex = i;
                break;
            }
        }
        if (potionIndex == -1) {
            System.out.println("You have no potions to use.");
            return false;
        }
        Item pot = player.getInventory().get(potionIndex);
        if (pot instanceof Potion p) {
            System.out.print("Use " + p.getName() + " and heal " + p.getHealAmount() + " HP? (1=Yes, 2=No): ");
            int conf = inputReader.readInt("");
            if (conf == 1) {
                int healed = p.getHealAmount();
                boolean ok = player.consumePotion(p);
                if (ok) System.out.println("You used " + p.getName() + " and healed " + healed + " HP.");
                else System.out.println("Failed to use the potion.");
                if (enemy.isAlive()) {
                    player.takeDamage(enemy.getAttack());
                    System.out.println(enemy.getName() + " hits you while you used the potion!");
                }
            } else {
                System.out.println("You decided not to use the potion.");
            }
        }
        return true;
    }

    /**
     * Offers an item to the player and handles inventory management.
     * Allows replacing items if inventory is full.
     *
     * @param drop the item to offer
     */
    private void offerItemToPlayer(Item drop) {
        System.out.print("Take it? (1=Yes, 2=No): ");
        int take = inputReader.readInt("");
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
                int replaceChoice = inputReader.readInt("");
                    if (replaceChoice > 0 && replaceChoice <= player.getInventoryCount()) {
                    Item removed = player.getInventory().get(replaceChoice - 1);
                    System.out.print("Replace " + removed + " with " + drop + "? (1=Yes, 2=No): ");
                    int conf2 = inputReader.readInt("");
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