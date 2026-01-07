package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTypeTest {
    @Test
    void ranges() {
        for (EnemyType t : EnemyType.values()) {
            assertTrue(t.getMinHp() <= t.getMaxHp());
            assertTrue(t.getMinAttack() <= t.getMaxAttack());
            assertNotNull(t.getName());
        }
    }
}
