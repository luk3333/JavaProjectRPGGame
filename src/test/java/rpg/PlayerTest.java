package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void inventoryAndEquipAndPotion() {
        Player p = new Player("Hero", 100, 10, 3);
        assertEquals(0, p.getInventoryCount());

        Potion pot = new Potion("Small", 20);
        Weapon w = new Weapon("TestSword", 5);
        Armor a = new Armor("Vest", 2);

        assertTrue(p.addItem(pot));
        assertTrue(p.addItem(w));
        assertTrue(p.addItem(a));
        assertFalse(p.addItem(new Potion("Extra", 5))); // capacity 3

        assertEquals(3, p.getInventoryCount());

        // equip weapon and armor
        assertTrue(p.equipWeaponAt(1));
        assertEquals(1, p.getEquippedWeaponIndex());
        assertTrue(p.equipArmorAt(2));
        assertEquals(2, p.getEquippedArmorIndex());

        // attack includes weapon
        assertEquals(15, p.getAttack()); // base 10 + weapon 5
        // defense includes armor
        assertEquals(2 + p.getDefense(), p.getTotalDefense());

        // use potion by index
        int beforeHp = p.getHp();
        assertTrue(p.usePotionByIndex(0));
        assertTrue(p.getHp() > beforeHp);

        // replace item
        assertTrue(p.replaceItemAt(0, new Potion("New", 10)));

        // remove item
        assertTrue(p.removeItemAt(0));
    }

    @Test
    void expAndLevelUp() {
        Player p = new Player("Hero", 50, 5, 5);
        int initialLevel = p.getLevel();
        p.gainExp(200); // should level up at least once (expNeeded default 100)
        assertTrue(p.getLevel() >= initialLevel + 1);
    }
}
