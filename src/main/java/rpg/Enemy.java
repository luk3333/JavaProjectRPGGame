
package rpg;

import java.util.Random;

public class Enemy {
    private String name;
    private int hp;
    private int attack;
    private static final Random random = new Random();

    public Enemy(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public void takeDamage(int d) { hp = Math.max(0, hp - d); }
    public boolean isAlive() { return hp > 0; }
    public int getAttack() { return attack; }
    public String getName() { return name; }

    public static Enemy generateRandomEnemy() {
        EnemyType[] enemies = EnemyType.values();
        EnemyType type = enemies[random.nextInt(enemies.length)];
        
        int hp = random.nextInt(type.getMaxHp() - type.getMinHp() + 1) + type.getMinHp();
        int attack = random.nextInt(type.getMaxAttack() - type.getMinAttack() + 1) + type.getMinAttack();
        
        return new Enemy(type.getName(), hp, attack);
    }

    @Override
    public String toString() {
        return name + " HP:" + hp + " ATK:" + attack;
    }
}