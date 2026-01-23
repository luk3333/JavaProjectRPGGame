
package rpg;

/**
 * Abstract base class for all items in the game.
 * Items can be weapons, armor, or potions that the player can collect and use.
 */
public abstract class Item {
    protected String name;

    /**
     * Constructs an item with the specified name.
     *
     * @param name the name of the item
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this item.
     *
     * @return the item's name
     */
    public String getName() { return name; }
    
    /**
     * Sets the name of this item.
     *
     * @param name the new name for the item
     */
    public void setName(String name) { this.name = name; }

    /**
     * Applies the item's effect to the given player.
     * Implementation depends on the specific item type.
     *
     * @param player the player to apply the effect to
     */
    public abstract void use(Player player);

    /**
     * Returns a string representation of this item.
     *
     * @return a string describing the item
     */
    @Override
    public abstract String toString();

    /**
     * Creates a random item (weapon, armor, or potion) with random stats.
     *
     * @param random the random number generator to use
     * @return a randomly generated item
     */
    public static Item createRandom(java.util.Random random) {
        int t = random.nextInt(3); // 0 = potion, 1 = weapon, 2 = armor
        switch (t) {
            case 0 -> {
                int heal = random.nextInt(Potion.POTION_MAX_HEAL - Potion.POTION_MIN_HEAL + 1) + Potion.POTION_MIN_HEAL;
                String pname = "HP Potion (" + heal + ")";
                return new Potion(pname, heal);
            }
            case 1 -> {
                Weapon.WeaponType[] types = Weapon.WeaponType.values();
                Weapon.WeaponType choice = types[random.nextInt(types.length)];
                int atk = random.nextInt(Weapon.WEAPON_MAX_ATTACK - Weapon.WEAPON_MIN_ATTACK + 1) + Weapon.WEAPON_MIN_ATTACK;
                return new Weapon(choice.getDisplayName(), atk);
            }
            default -> {
                Armor.ArmorType[] types = Armor.ArmorType.values();
                Armor.ArmorType choice = types[random.nextInt(types.length)];
                int def = random.nextInt(Armor.ARMOR_MAX_DEFENSE - Armor.ARMOR_MIN_DEFENSE + 1) + Armor.ARMOR_MIN_DEFENSE;
                return new Armor(choice.getDisplayName(), def);
            }
        }
    }
}