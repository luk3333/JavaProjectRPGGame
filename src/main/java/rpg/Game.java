package rpg;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        System.out.print("Enter save filename to load (default: savegame.json): ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) fileName = "savegame.json";

        try {
            String json = Files.readString(Paths.get(fileName));

            String name = extractString(json, "name");
            int maxHp = extractInt(json, "hp", 50);
            int currentHp = extractInt(json, "currentHp", maxHp);
            int baseAttack = extractInt(json, "baseAttack", 5);
            int defense = extractInt(json, "defense", 0);
            int exp = extractInt(json, "exp", 0);
            int level = extractInt(json, "level", 1);
            int inventoryCapacity = extractInt(json, "inventoryCapacity", 10);
            int equippedWeaponIndex = extractInt(json, "equippedWeaponIndex", -1);
            int equippedArmorIndex = extractInt(json, "equippedArmorIndex", -1);

            Player p = new Player(name, maxHp, baseAttack, inventoryCapacity);
            p.setHp(currentHp);
            p.setBaseAttack(baseAttack);
            p.setDefense(defense);
            p.setExp(exp);
            p.setLevel(level);

            // parse inventory
            Pattern invPattern = Pattern.compile("\"inventory\"\\s*:\\s*\\[(.*)\\]", Pattern.DOTALL);
            Matcher invMatcher = invPattern.matcher(json);
            if (invMatcher.find()) {
                String invContent = invMatcher.group(1);
                Pattern itemPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
                Matcher itemMatcher = itemPattern.matcher(invContent);
                List<Item> items = new ArrayList<>();
                while (itemMatcher.find()) {
                    String itemJson = itemMatcher.group(1);
                    String type = extractString(itemJson, "type");
                    String iname = extractString(itemJson, "name");
                    if ("Potion".equalsIgnoreCase(type)) {
                        int heal = extractInt(itemJson, "heal", Potion.POTION_MIN_HEAL);
                        items.add(new Potion(iname, heal));
                    } else if ("Weapon".equalsIgnoreCase(type)) {
                        int atk = extractInt(itemJson, "attack", Weapon.WEAPON_MIN_ATTACK);
                        items.add(new Weapon(iname, atk));
                    } else if ("Armor".equalsIgnoreCase(type)) {
                        int def = extractInt(itemJson, "defense", Armor.ARMOR_MIN_DEFENSE);
                        items.add(new Armor(iname, def));
                    }
                }
                for (Item it : items) p.addItem(it);

                // equip indices
                if (equippedWeaponIndex >= 0 && equippedWeaponIndex < p.getInventoryCount()) {
                    p.equipWeaponAt(equippedWeaponIndex);
                }
                if (equippedArmorIndex >= 0 && equippedArmorIndex < p.getInventoryCount()) {
                    p.equipArmorAt(equippedArmorIndex);
                }
            }

            this.player = p;
            System.out.println("Loaded save for '" + name + "'.");
            start();
        } catch (IOException e) {
            System.out.println("Failed to load save: " + e.getMessage());
        }
    }

    private String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);
        return "";
    }

    private int extractInt(String json, String key, int defaultVal) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (NumberFormatException ex) { return defaultVal; }
        }
        return defaultVal;
    }

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
                    System.out.print("Save file name (default: savegame.json): ");
                    String fileName = scanner.nextLine().trim();
                    if (fileName.isEmpty()) fileName = "savegame.json";
                    saveGame(fileName);
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

    // Save the player's state to a JSON file (name, stats, level, inventory, equipped indices)
    private void saveGame(String fileName) {
        if (player == null) {
            System.out.println("No player to save.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\":\"").append(escapeJson(player.getName())).append("\",");
        sb.append("\"hp\":").append(player.getMaxHp()).append(",");
        sb.append("\"currentHp\":").append(player.getHp()).append(",");
        sb.append("\"baseAttack\":").append(player.getBaseAttack()).append(",");
        sb.append("\"defense\":").append(player.getDefense()).append(",");
        sb.append("\"exp\":").append(player.getExp()).append(",");
        sb.append("\"level\":").append(player.getLevel()).append(",");
        sb.append("\"inventoryCapacity\":").append(player.getInventoryCapacity()).append(",");
        sb.append("\"equippedWeaponIndex\":").append(player.getEquippedWeaponIndex()).append(",");
        sb.append("\"equippedArmorIndex\":").append(player.getEquippedArmorIndex()).append(",");

        sb.append("\"inventory\":[");
        for (int i = 0; i < player.getInventory().size(); i++) {
            Item it = player.getInventory().get(i);
            sb.append("{");
            if (it instanceof Potion) {
                Potion p = (Potion) it;
                sb.append("\"type\":\"Potion\",");
                sb.append("\"name\":\"").append(escapeJson(p.getName())).append("\",");
                sb.append("\"heal\":").append(p.getHealAmount());
            } else if (it instanceof Weapon) {
                Weapon w = (Weapon) it;
                sb.append("\"type\":\"Weapon\",");
                sb.append("\"name\":\"").append(escapeJson(w.getName())).append("\",");
                sb.append("\"attack\":").append(w.getAttackValue());
            } else if (it instanceof Armor) {
                Armor a = (Armor) it;
                sb.append("\"type\":\"Armor\",");
                sb.append("\"name\":\"").append(escapeJson(a.getName())).append("\",");
                sb.append("\"defense\":").append(a.getDefenseValue());
            } else {
                sb.append("\"type\":\"Unknown\",");
                sb.append("\"name\":\"").append(escapeJson(it.getName())).append("\"");
            }
            sb.append("}");
            if (i < player.getInventory().size() - 1) sb.append(",");
        }
        sb.append("]");

        sb.append("}");

        // write to file
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(sb.toString());
            System.out.println("Game saved to '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    // minimal JSON string escaper
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}