package rpg;

/**
 * Represents an armor item that can be equipped by the player to increase defense.
 */
public class Armor extends Item {
    private final int defenseValue;

    /** Minimum defense value for randomly generated armor */
    public static int ARMOR_MIN_DEFENSE = 1;
    /** Maximum defense value for randomly generated armor */
    public static int ARMOR_MAX_DEFENSE = 4;

    /**
     * Constructs armor with the specified name and defense value.
     *
     * @param name the name of the armor
     * @param defenseValue the defense bonus this armor provides
     */
    public Armor(String name, int defenseValue) {
        super(name);
        this.defenseValue = defenseValue;
    }

    /**
     * Gets the defense value of this armor.
     *
     * @return the defense bonus
     */
    public int getDefenseValue() { return defenseValue; }

    /**
     * Equips this armor to the player.
     *
     * @param player the player to equip the armor to
     */
    @Override
    public void use(Player player) {
        player.equipArmor(this);
    }

    /**
     * Returns a string representation of this armor.
     *
     * @return a string showing the armor name and defense value
     */
    @Override
    public String toString() {
        return name + " (ARMOR, def=" + defenseValue + ")";
    }

    /**
     * Enumeration of predefined armor types.
     */
    public enum ArmorType {
        LEATHER_ARMOR("Leather Armor"),
        CLOTH_ROBE("Cloth Robe"),
        HIDE_VEST("Hide Vest");

        private final String displayName;
        
        /**
         * Constructs an armor type with the specified display name.
         *
         * @param displayName the display name for this armor type
         */
        ArmorType(String displayName) { this.displayName = displayName; }
        
        /**
         * Gets the display name of this armor type.
         *
         * @return the display name
         */
        public String getDisplayName() { return displayName; }
    }
}
