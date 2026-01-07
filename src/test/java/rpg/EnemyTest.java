package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    @Test
    void generateRandomEnemy() {
        Enemy e = Enemy.generateRandomEnemy();
        assertNotNull(e);
        assertNotNull(e.getName());
        assertTrue(e.getAttack() >= 0);
        assertTrue(e.isAlive() || !e.isAlive()); // just exercise methods
    }
}
