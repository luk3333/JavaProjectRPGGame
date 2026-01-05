
package rpg;

import java.util.ArrayList;
import java.util.List;

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

    public boolean addItem(Item item) {
        if (inventory.size() >= inventoryCapacity) return false;
        inventory.add(item);
        return true;
    }

    public boolean usePotionByIndex(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Potion)) return false;
        int heal = ((Potion) it).getHealAmount();
        heal(heal);
        removeItemAt(index);
        return true;
    }

    public int useFirstPotion() {
        for (int i = 0; i < inventory.size(); i++) {
            Item it = inventory.get(i);
            if (it instanceof Potion) {
                int heal = ((Potion) it).getHealAmount();
                heal(heal);
                removeItemAt(i);
                return heal;
            }
        }
        return 0; // no potion
    }

    public boolean removeItemAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        inventory.remove(index);
        // adjust equipped indices
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

    // Replace item at given index with newItem. Keeps inventory size the same.
    // Returns true on success, false on invalid index.
    public boolean replaceItemAt(int index, Item newItem) {
        if (index < 0 || index >= inventory.size()) return false;
        Item old = inventory.set(index, newItem);
        // adjust equipped indexes: if the slot was equipped for weapon/armor,
        // keep equipped index only if the new item is of the same kind; otherwise unequip.
        if (equippedWeaponIndex == index) {
            if (!(newItem instanceof Weapon)) equippedWeaponIndex = -1;
        }
        // if the replaced slot wasn't previously the equipped weapon, we don't auto-equip

        if (equippedArmorIndex == index) {
            if (!(newItem instanceof Armor)) equippedArmorIndex = -1;
        }
        return true;
    }

    public boolean equipWeaponAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Weapon)) return false;
        equippedWeaponIndex = index;
        return true;
    }

    public void unequipWeapon() {
        equippedWeaponIndex = -1;
    }

    public boolean equipArmorAt(int index) {
        if (index < 0 || index >= inventory.size()) return false;
        Item it = inventory.get(index);
        if (!(it instanceof Armor)) return false;
        equippedArmorIndex = index;
        return true;
    }

    public void unequipArmor() {
        equippedArmorIndex = -1;
    }

    public int getEquippedWeaponIndex() { return equippedWeaponIndex; }
    public Item getEquippedWeapon() { return (equippedWeaponIndex >= 0) ? inventory.get(equippedWeaponIndex) : null; }
    public int getEquippedArmorIndex() { return equippedArmorIndex; }
    public Item getEquippedArmor() { return (equippedArmorIndex >= 0) ? inventory.get(equippedArmorIndex) : null; }

    public List<Item> getInventory() { return inventory; }
    public int getInventoryCapacity() { return inventoryCapacity; }
    public int getInventoryCount() { return inventory.size(); }

    public void takeDamage(int d) {
        int damage = Math.max(0, d - getTotalDefense());
        hp = Math.max(0, hp - damage);
    }
    public void heal(int d) { hp = Math.min(maxHp, hp + d); }
    public int getMaxHp() { return maxHp; }
    public String getName() { return name; }
    public int getDefense() { return defense; }
    public boolean isAlive() { return hp > 0; }
    public int getBaseAttack() { return baseAttack; }
    public int getAttack() {
        int bonus = 0;
        Item w = getEquippedWeapon();
        if (w instanceof Weapon) bonus = ((Weapon) w).getAttackValue();
        return baseAttack + bonus;
    }

    public int getTotalDefense() {
        int bonus = 0;
        Item a = getEquippedArmor();
        if (a instanceof Armor) bonus = ((Armor) a).getDefenseValue();
        return defense + bonus;
    }
    
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= expNeeded) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        exp = 0;
        System.out.println("*** You leveled up! ***");
        System.out.println("Level: " + level);
    }
    
    public int getLevel() { return level; }
    public int getExp() { return exp; }

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