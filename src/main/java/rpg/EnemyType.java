package rpg;

/**
 * Enumeration of enemy types with their stat ranges.
 * Used to generate random enemies with appropriate difficulty levels.
 */
public enum EnemyType {
    /** Goblin - weak enemy with low HP and attack */
    GOBLIN("Goblin", 10, 25, 10, 15),
    /** Troll - strong enemy with high HP and attack */
    TROLL("Troll", 40, 50, 15, 25),
    /** Wolf - medium difficulty enemy */
    WOLF("Wolf", 20, 35, 10, 20);

    private final String name;
    private final int minHp;
    private final int maxHp;
    private final int minAttack;
    private final int maxAttack;

    /**
     * Constructs an enemy type with specified stat ranges.
     *
     * @param name the name of the enemy type
     * @param minHp the minimum HP value
     * @param maxHp the maximum HP value
     * @param minAttack the minimum attack value
     * @param maxAttack the maximum attack value
     */
    EnemyType(String name, int minHp, int maxHp, int minAttack, int maxAttack) {
        this.name = name;
        this.minHp = minHp;
        this.maxHp = maxHp;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
    }

    /**
     * Gets the name of this enemy type.
     *
     * @return the enemy type name
     */
    public String getName() { return name; }
    
    /**
     * Gets the minimum HP for this enemy type.
     *
     * @return the minimum health points
     */
    public int getMinHp() { return minHp; }
    
    /**
     * Gets the maximum HP for this enemy type.
     *
     * @return the maximum health points
     */
    public int getMaxHp() { return maxHp; }
    
    /**
     * Gets the minimum attack for this enemy type.
     *
     * @return the minimum attack power
     */
    public int getMinAttack() { return minAttack; }
    
    /**
     * Gets the maximum attack for this enemy type.
     *
     * @return the maximum attack power
     */
    public int getMaxAttack() { return maxAttack; }
}
