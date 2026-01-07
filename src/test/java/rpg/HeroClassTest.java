package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeroClassTest {
    @Test
    void values() {
        assertEquals("Warrior", HeroClass.WARRIOR.getName());
        assertEquals(100, HeroClass.WARRIOR.getHp());
        assertEquals(10, HeroClass.WARRIOR.getAttack());
        assertEquals(10, HeroClass.WARRIOR.getInventorySlots());

        assertEquals("Archer", HeroClass.ARCHER.getName());
        assertEquals(80, HeroClass.ARCHER.getHp());

        assertEquals("Mage", HeroClass.MAGE.getName());
        assertEquals(50, HeroClass.MAGE.getHp());
    }
}
