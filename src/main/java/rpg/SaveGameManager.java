package rpg;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages saving and loading player game state to/from JSON files.
 * Handles serialization of player stats, inventory, and equipment.
 */
public class SaveGameManager {

    /**
     * Saves the player's state to a JSON file.
     * Includes all player stats, inventory items, and equipped items.
     *
     * @param player the player to save
     * @param fileName the file name to save to
     */
    public void saveGame(Player player, String fileName) {
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
            switch (it) {
                case Potion p -> {
                    sb.append("\"type\":\"Potion\",");
                    sb.append("\"name\":\"").append(escapeJson(p.getName())).append("\",");
                    sb.append("\"heal\":").append(p.getHealAmount());
                }
                case Weapon w -> {
                    sb.append("\"type\":\"Weapon\",");
                    sb.append("\"name\":\"").append(escapeJson(w.getName())).append("\",");
                    sb.append("\"attack\":").append(w.getAttackValue());
                }
                case Armor a -> {
                    sb.append("\"type\":\"Armor\",");
                    sb.append("\"name\":\"").append(escapeJson(a.getName())).append("\",");
                    sb.append("\"defense\":").append(a.getDefenseValue());
                }
                default -> {
                    sb.append("\"type\":\"Unknown\",");
                    sb.append("\"name\":\"").append(escapeJson(it.getName())).append("\"");
                }
            }
            sb.append("}");
            if (i < player.getInventory().size() - 1) sb.append(",");
        }
        sb.append("]");

        sb.append("}");

        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(sb.toString());
            System.out.println("Game saved to '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Loads a player from a JSON save file.
     *
     * @param fileName the file name to load from
     * @return the loaded player, or null if loading failed
     */
    public Player loadGame(String fileName) {
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

                if (equippedWeaponIndex >= 0 && equippedWeaponIndex < p.getInventoryCount()) {
                    p.equipWeaponAt(equippedWeaponIndex);
                }
                if (equippedArmorIndex >= 0 && equippedArmorIndex < p.getInventoryCount()) {
                    p.equipArmorAt(equippedArmorIndex);
                }
            }

            System.out.println("Loaded save for '" + name + "'.");
            return p;
        } catch (IOException e) {
            System.out.println("Failed to load save: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts a string value from JSON by key.
     *
     * @param json the JSON string
     * @param key the key to extract
     * @return the extracted string, or empty string if not found
     */
    private String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);
        return "";
    }

    /**
     * Extracts an integer value from JSON by key.
     *
     * @param json the JSON string
     * @param key the key to extract
     * @param defaultVal the default value if not found or invalid
     * @return the extracted integer, or defaultVal if extraction fails
     */
    private int extractInt(String json, String key, int defaultVal) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (NumberFormatException ex) { return defaultVal; }
        }
        return defaultVal;
    }

    /**
     * Escapes special characters in a string for JSON format.
     *
     * @param s the string to escape
     * @return the escaped string
     */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
