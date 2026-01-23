package rpg;

/**
 * Represents a potion item that can be consumed to restore the player's health.
 */
public class Potion extends Item {
    private int healAmount;

    /** Minimum heal amount for randomly generated potions */
    public static int POTION_MIN_HEAL = 15;
    /** Maximum heal amount for randomly generated potions */
    public static int POTION_MAX_HEAL = 30;

    /**
     * Constructs a potion with the specified name and heal amount.
     *
     * @param name the name of the potion
     * @param healAmount the amount of HP this potion restores
     */
    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    /**
     * Gets the heal amount of this potion.
     *
     * @return the amount of HP restored
     */
    public int getHealAmount() { return healAmount; }
    
    /**
     * Sets the heal amount of this potion.
     *
     * @param healAmount the new heal amount
     */
    public void setHealAmount(int healAmount) { this.healAmount = healAmount; }

    /**
     * Consumes this potion to heal the player.
     *
     * @param player the player to heal
     */
    @Override
    public void use(Player player) {
        player.consumePotion(this);
    }

    /**
     * Returns a string representation of this potion.
     *
     * @return a string showing the potion name and heal amount
     */
    @Override
    public String toString() {
        return name + " (POTION, heal=" + healAmount + ")";
    }
}
