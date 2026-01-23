package rpg;

/**
 * Represents a weapon item that can be equipped by the player to increase attack power.
 */
public class Weapon extends Item {
    private int attackValue;

    /** Minimum attack value for randomly generated weapons */
    public static int WEAPON_MIN_ATTACK = 1;
    /** Maximum attack value for randomly generated weapons */
    public static int WEAPON_MAX_ATTACK = 6;

    /**
     * Constructs a weapon with the specified name and attack value.
     *
     * @param name the name of the weapon
     * @param attackValue the attack bonus this weapon provides
     */
    public Weapon(String name, int attackValue) {
        super(name);
        this.attackValue = attackValue;
    }

    /**
     * Gets the attack value of this weapon.
     *
     * @return the attack bonus
     */
    public int getAttackValue() { return attackValue; }
    
    /**
     * Sets the attack value of this weapon.
     *
     * @param attackValue the new attack bonus
     */
    public void setAttackValue(int attackValue) { this.attackValue = attackValue; }

    /**
     * Equips this weapon to the player.
     *
     * @param player the player to equip the weapon to
     */
    @Override
    public void use(Player player) {
        player.equipWeapon(this);
    }

    /**
     * Returns a string representation of this weapon.
     *
     * @return a string showing the weapon name and attack value
     */
    @Override
    public String toString() {
        return name + " (WEAPON, atk=" + attackValue + ")";
    }

    /**
     * Enumeration of predefined weapon types.
     */
    public enum WeaponType {
        RUSTY_SWORD("Rusty Sword"),
        SHORT_BOW("Short Bow"),
        WOODEN_STAFF("Wooden Staff");

        private final String displayName;
        
        /**
         * Constructs a weapon type with the specified display name.
         *
         * @param displayName the display name for this weapon type
         */
        WeaponType(String displayName) { this.displayName = displayName; }
        
        /**
         * Gets the display name of this weapon type.
         *
         * @return the display name
         */
        public String getDisplayName() { return displayName; }
    }
}
