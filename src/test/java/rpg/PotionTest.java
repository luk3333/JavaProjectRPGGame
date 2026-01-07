package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PotionTest {
    @Test
    void potionBasics() {
        Potion p = new Potion("Test Potion", 20);
        assertEquals(20, p.getHealAmount());
        assertTrue(p.toString().contains("POTION"));
    }
}
