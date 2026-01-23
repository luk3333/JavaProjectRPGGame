
package rpg;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player character in the RPG game.
 * Manages player stats, inventory, equipment, and leveling system.
 */
public class Player {
    private String name;
    private int hp;
    private int maxHp;
    private int baseAttack;
    private int defense;
    private int equippedWeaponIndex = -1; // index in inventory, -1 = none
    private int equippedArmorIndex = -1; // index in inventory, -1 = none
    private int exp;
    private int level;
    private int expNeeded;
    private List<Item> inventory;
    private int inventoryCapacity;

    /**
     * Constructs a new player with the specified name and stats.
     *
     * @param name the player's name
     * @param hp the starting (and maximum) health points
     * @param attack the base attack power
     * @param inventoryCapacity the maximum number of items that can be carried
     */
    public Player(String name, int hp, int attack, int inventoryCapacity) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.baseAttack = attack;
        this.defense = 0;
        this.exp = 0;
        this.level = 1;
        this.expNeeded = 100; // Experience needed to level up
        this.inventoryCapacity = inventoryCapacity;
        this.inventory = new ArrayList<>();
    }

    /**
     * Attempts to add an item to the player's inventory.
     *
     * @param item the item to add
     * @return true if the item was added, false if inventory is full
     */
    public boolean addItem(Item item) {
        if (inventory.size() >= inventoryCapacity) return false;
        inventory.add(item);
        return true;
    }

    /**
     * Finds the index of an item in the inventory.
     *
     * @param item the item to find
     * @return the index of the item, or -1 if not found
     */
    public int findItemIndex(Item item) {
        return inventory.indexOf(item);
    }

    /**
     * Equips a weapon from the inventory.
     *
     * @param weapon the weapon to equip
     * @return true if the weapon was equipped, false if not in inventory
     */
    public boolean equipWeapon(Weapon weapon) {
        int idx = findItemIndex(weapon);
        if (idx == -1) return false;
        equippedWeaponIndex = idx;
        return true;
    }

    /**
     * Equips armor from the inventory.
     *
     * @param armor the armor to equip
     * @return true if the armor was equipped, false if not in inventory
     */
    public boolean equipArmor(Armor armor) {
        int idx = findItemIndex(armor);
        if (idx == -1) return false;
        equippedArmorIndex = idx;
        return true;
    }

    /**
     * Consumes a potion from the inventory to heal the player.
     *
     * @param potion the potion to consume
     * @return true if the potion was consumed, false if not in inventory
     */
    public boolean consumePotion(Potion potion) {
        int idx = findItemIndex(potion);
        if (idx == -1) return false;
        heal(potion.getHealAmount());
        removeItemAt(idx);
        return true;
    }

    /**
     * Uses a potion at a specific inventory index.
     *
     * @param index the inventory index of the potion
     * @return true if the potion was used, false if index invalid or item is not a potion
     */
    public boolean usePotionByIndex(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Potion)) return false;
        int heal = ((Potion) it).getHealAmount();
        heal(heal);
        removeItemAt(index);
        return true;
    }

    /**
     * Uses the first potion found in the inventory.
     *
     * @return the amount healed, or 0 if no potion was found
     */
    public int useFirstPotion() {
        for (int i = 0; i < inventory.size(); i++) {
            Item it = inventory.get(i);
            if (it instanceof Potion potion) {
                int heal = potion.getHealAmount();
                heal(heal);
                removeItemAt(i);
                return heal;
            }
        }
        return 0; // no potion
    }

    /**
     * Removes an item from the inventory at the specified index.
     * Adjusts equipped indices if necessary.
     *
     * @param index the inventory index to remove
     * @return true if the item was removed, false if index invalid
     */
    public boolean removeItemAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        inventory.remove(index);
        if (equippedWeaponIndex == index) {
            equippedWeaponIndex = -1;
        } else if (equippedWeaponIndex > index) {
            equippedWeaponIndex--; 
        }
        if (equippedArmorIndex == index) {
            equippedArmorIndex = -1;
        } else if (equippedArmorIndex > index) {
            equippedArmorIndex--;
        }
        return true;
    }

    /**
     * Replaces an item at a specific inventory index with a new item.
     * Unequips if the new item is not the same type as equipped.
     *
     * @param index the inventory index to replace
     * @param newItem the new item to place at that index
     * @return true if the item was replaced, false if index invalid
     */
    public boolean replaceItemAt(int index, Item newItem) {
        if (index < 0 || index >= inventory.size()) return false;
        inventory.set(index, newItem);
        if (equippedWeaponIndex == index) {
            if (!(newItem instanceof Weapon)) equippedWeaponIndex = -1;
        }

        if (equippedArmorIndex == index) {
            if (!(newItem instanceof Armor)) equippedArmorIndex = -1;
        }
        return true;
    }

    /**
     * Equips a weapon at the specified inventory index.
     *
     * @param index the inventory index of the weapon
     * @return true if the weapon was equipped, false if index invalid or item is not a weapon
     */
    public boolean equipWeaponAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Weapon)) return false;
        equippedWeaponIndex = index;
        return true;
    }

    /**
     * Unequips the currently equipped weapon.
     */
    public void unequipWeapon() {
        equippedWeaponIndex = -1;
    }

    /**
     * Equips armor at the specified inventory index.
     *
     * @param index the inventory index of the armor
     * @return true if the armor was equipped, false if index invalid or item is not armor
     */
    public boolean equipArmorAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Armor)) return false;
        equippedArmorIndex = index;
        return true;
    }

    /**
     * Unequips the currently equipped armor.
     */
    public void unequipArmor() {
        equippedArmorIndex = -1;
    }

    /** Gets the equipped weapon index. @return the index, or -1 if none */
    public int getEquippedWeaponIndex() { return equippedWeaponIndex; }
    
    /** Gets the equipped weapon item. @return the weapon, or null if none */
    public Item getEquippedWeapon() { return (equippedWeaponIndex >= 0) ? inventory.get(equippedWeaponIndex) : null; }
    
    /** Gets the equipped armor index. @return the index, or -1 if none */
    public int getEquippedArmorIndex() { return equippedArmorIndex; }
    
    /** Gets the equipped armor item. @return the armor, or null if none */
    public Item getEquippedArmor() { return (equippedArmorIndex >= 0) ? inventory.get(equippedArmorIndex) : null; }

    /** Gets the player's inventory. @return the list of items */
    public List<Item> getInventory() { return inventory; }
    
    /** Gets the inventory capacity. @return the maximum inventory size */
    public int getInventoryCapacity() { return inventoryCapacity; }
    
    /** Gets the current number of items in inventory. @return the inventory size */
    public int getInventoryCount() { return inventory.size(); }

    /**
     * Applies damage to the player, reduced by total defense.
     *
     * @param d the raw damage amount
     */
    public void takeDamage(int d) {
        int damage = Math.max(0, d - getTotalDefense());
        hp = Math.max(0, hp - damage);
    }
    
    /**
     * Heals the player by the specified amount, not exceeding max HP.
     *
     * @param d the amount to heal
     */
    public void heal(int d) { hp = Math.min(maxHp, hp + d); }
    
    /** Gets the maximum HP. @return the max HP value */
    public int getMaxHp() { return maxHp; }
    
    /**
     * Sets the maximum HP and adjusts current HP if necessary.
     *
     * @param maxHp the new maximum HP (minimum 1)
     */
    public void setMaxHp(int maxHp) { this.maxHp = Math.max(1, maxHp); if (hp > this.maxHp) hp = this.maxHp; }
    
    /** Gets the current HP. @return the current health points */
    public int getHp() { return hp; }
    
    /** Gets the player's name. @return the name */
    public String getName() { return name; }
    
    /** Sets the player's name. @param name the new name */
    public void setName(String name) { this.name = name; }
    
    /** Gets the base defense value (without armor). @return the defense value */
    public int getDefense() { return defense; }
    
    /** Checks if the player is alive. @return true if HP > 0 */
    public boolean isAlive() { return hp > 0; }
    
    /** Gets the base attack value (without weapon). @return the base attack */
    public int getBaseAttack() { return baseAttack; }
    
    /**
     * Gets the total attack power including equipped weapon.
     *
     * @return the total attack value
     */
    public int getAttack() {
        int bonus = 0;
        Item w = getEquippedWeapon();
        if (w instanceof Weapon weapon) bonus = weapon.getAttackValue();
        return baseAttack + bonus;
    }

    /**
     * Gets the total defense including equipped armor.
     *
     * @return the total defense value
     */
    public int getTotalDefense() {
        int bonus = 0;
        Item a = getEquippedArmor();
        if (a instanceof Armor armor) bonus = armor.getDefenseValue();
        return defense + bonus;
    }
    
    /** Sets the current HP (used when loading). @param hp the HP value */
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    
    /** Sets the base attack (used when loading). @param attack the attack value */
    public void setBaseAttack(int attack) { this.baseAttack = attack; }
    
    /** Sets the experience (used when loading). @param exp the experience value */
    public void setExp(int exp) { this.exp = exp; }
    
    /** Sets the level (used when loading). @param level the level value */
    public void setLevel(int level) { this.level = level; }
    
    /** Sets the defense (used when loading). @param def the defense value */
    public void setDefense(int def) { this.defense = def; }
    
    /**
     * Awards experience points to the player and triggers level up if enough XP.
     *
     * @param amount the amount of experience to gain
     */
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= expNeeded) {
            levelUp();
        }
    }
    
    /**
     * Levels up the player, resetting experience.
     */
    private void levelUp() {
        level++;
        exp = 0;
        System.out.println("*** You leveled up! ***");
        System.out.println("Level: " + level);
    }
    
    /** Gets the player's level. @return the level */
    public int getLevel() { return level; }
    
    /** Gets the current experience. @return the experience points */
    public int getExp() { return exp; }
    
    /** Gets the experience needed for next level. @return the required XP */
    public int getExpNeeded() { return expNeeded; }
    
    /** Sets the XP needed (used when loading). @param expNeeded the required XP */
    public void setExpNeeded(int expNeeded) { this.expNeeded = expNeeded; }
    
    /** Sets the equipped weapon index (used when loading). @param index the weapon index */
    public void setEquippedWeaponIndex(int index) { this.equippedWeaponIndex = index; }
    
    /** Sets the equipped armor index (used when loading). @param index the armor index */
    public void setEquippedArmorIndex(int index) { this.equippedArmorIndex = index; }
    
    /** Sets the inventory capacity (used when loading). @param capacity the new capacity */
    public void setInventoryCapacity(int capacity) { this.inventoryCapacity = capacity; }
    
    /** Sets the entire inventory (used when loading). @param inventory the new inventory list */
    public void setInventory(List<Item> inventory) { this.inventory = inventory != null ? inventory : new ArrayList<>(); }

    /**
     * Returns a string representation of the player showing all stats.
     *
     * @return formatted player information
     */
    @Override
    public String toString() {
        String equipped = "";
        if (equippedWeaponIndex >= 0 && equippedWeaponIndex < inventory.size()) {
            equipped = " (equipped: " + inventory.get(equippedWeaponIndex).getName() + ")";
        }
        String armorInfo = "";
        if (equippedArmorIndex >= 0 && equippedArmorIndex < inventory.size()) {
            armorInfo = " [armor: " + inventory.get(equippedArmorIndex).getName() + "]";
        }
        return name + " HP:" + hp + " ATK:" + getAttack() + " LVL:" + level + " EXP:" + exp + "/" + expNeeded
            + " INV:" + inventory.size() + "/" + inventoryCapacity + equipped + armorInfo + " DEF:" + getTotalDefense();
    }
}