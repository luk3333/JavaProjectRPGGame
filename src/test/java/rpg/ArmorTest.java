package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArmorTest {
    @Test
    void armorBasics() {
        Armor a = new Armor("Shield", 3);
        assertEquals(3, a.getDefenseValue());
        assertTrue(a.toString().contains("ARMOR"));
        assertNotNull(Armor.ArmorType.values());
    }
}
