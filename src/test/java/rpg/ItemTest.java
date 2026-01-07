package rpg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class ItemTest {
    @Test
    void createRandomProducesItemWithinBounds() {
        Random rnd = new Random(123);
        for (int i = 0; i < 20; i++) {
            Item it = Item.createRandom(rnd);
            assertNotNull(it);
            if (it instanceof Potion) {
                Potion p = (Potion) it;
                assertTrue(p.getHealAmount() >= Potion.POTION_MIN_HEAL && p.getHealAmount() <= Potion.POTION_MAX_HEAL);
            } else if (it instanceof Weapon) {
                Weapon w = (Weapon) it;
                assertTrue(w.getAttackValue() >= Weapon.WEAPON_MIN_ATTACK && w.getAttackValue() <= Weapon.WEAPON_MAX_ATTACK);
            } else if (it instanceof Armor) {
                Armor a = (Armor) it;
                assertTrue(a.getDefenseValue() >= Armor.ARMOR_MIN_DEFENSE && a.getDefenseValue() <= Armor.ARMOR_MAX_DEFENSE);
            }
        }
    }
}
