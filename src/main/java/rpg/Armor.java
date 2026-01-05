package rpg;

public class Armor extends Item {
    private int defenseValue;

    // Generation bounds for armor
    public static int ARMOR_MIN_DEFENSE = 1;
    public static int ARMOR_MAX_DEFENSE = 4;

    public Armor(String name, int defenseValue) {
        super(name);
        this.defenseValue = defenseValue;
    }

    public int getDefenseValue() { return defenseValue; }

    @Override
    public String toString() {
        return name + " (ARMOR, def=" + defenseValue + ")";
    }

    public enum ArmorType {
        LEATHER_ARMOR("Leather Armor"),
        CLOTH_ROBE("Cloth Robe"),
        HIDE_VEST("Hide Vest");

        private final String displayName;
        ArmorType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
