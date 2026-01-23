package rpg;

/**
 * Enumeration of hero classes available to the player.
 * Each class has different starting stats and inventory capacity.
 */
public enum HeroClass {
    /** Warrior class - high HP, moderate attack, large inventory */
    WARRIOR("Warrior", 100, 10, 10),
    /** Archer class - moderate HP, high attack, medium inventory */
    ARCHER("Archer", 80, 15, 7),
    /** Mage class - low HP, very high attack, small inventory */
    MAGE("Mage", 50, 25, 5);

    private final String name;
    private final int hp;
    private final int attack;
    private final int inventorySlots;

    /**
     * Constructs a hero class with the specified stats.
     *
     * @param name the display name of the class
     * @param hp the starting health points
     * @param attack the starting attack power
     * @param inventorySlots the maximum inventory capacity
     */
    HeroClass(String name, int hp, int attack, int inventorySlots) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.inventorySlots = inventorySlots;
    }

    /**
     * Gets the name of this hero class.
     *
     * @return the class name
     */
    public String getName() { return name; }
    
    /**
     * Gets the starting HP for this hero class.
     *
     * @return the starting health points
     */
    public int getHp() { return hp; }
    
    /**
     * Gets the starting attack for this hero class.
     *
     * @return the starting attack power
     */
    public int getAttack() { return attack; }
    
    /**
     * Gets the inventory capacity for this hero class.
     *
     * @return the number of inventory slots
     */
    public int getInventorySlots() { return inventorySlots; }
}