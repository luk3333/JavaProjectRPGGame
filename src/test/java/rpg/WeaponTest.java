package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    @Test
    void weaponBasics() {
        Weapon w = new Weapon("Blade", 5);
        assertEquals(5, w.getAttackValue());
        assertTrue(w.toString().contains("WEAPON"));
        assertNotNull(Weapon.WeaponType.values());
    }
}
