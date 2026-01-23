
package rpg;

import java.util.Random;

/**
 * Represents an enemy that the player can encounter and fight.
 * Enemies have health, attack power, and are generated with random stats based on their type.
 */
public class Enemy {
    private String name;
    private int hp;
    private int attack;
    private static final Random random = new Random();

    /**
     * Constructs an enemy with the specified name, HP, and attack.
     *
     * @param name the name of the enemy
     * @param hp the health points of the enemy
     * @param attack the attack power of the enemy
     */
    public Enemy(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    /**
     * Reduces the enemy's HP by the specified damage amount.
     *
     * @param d the amount of damage to deal
     */
    public void takeDamage(int d) { hp = Math.max(0, hp - d); }
    
    /**
     * Checks if the enemy is still alive.
     *
     * @return true if HP is greater than 0, false otherwise
     */
    public boolean isAlive() { return hp > 0; }
    
    /**
     * Gets the attack power of this enemy.
     *
     * @return the attack value
     */
    public int getAttack() { return attack; }
    
    /**
     * Gets the name of this enemy.
     *
     * @return the enemy's name
     */
    public String getName() { return name; }
    
    /**
     * Gets the current HP of this enemy.
     *
     * @return the current health points
     */
    public int getHp() { return hp; }
    
    /**
     * Sets the name of this enemy.
     *
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Sets the HP of this enemy.
     *
     * @param hp the new health points (cannot be negative)
     */
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    
    /**
     * Sets the attack power of this enemy.
     *
     * @param attack the new attack value
     */
    public void setAttack(int attack) { this.attack = attack; }

    /**
     * Generates a random enemy with stats based on a random EnemyType.
     *
     * @return a newly created random enemy
     */
    public static Enemy generateRandomEnemy() {
        EnemyType[] enemies = EnemyType.values();
        EnemyType type = enemies[random.nextInt(enemies.length)];
        
        int hp = random.nextInt(type.getMaxHp() - type.getMinHp() + 1) + type.getMinHp();
        int attack = random.nextInt(type.getMaxAttack() - type.getMinAttack() + 1) + type.getMinAttack();
        
        return new Enemy(type.getName(), hp, attack);
    }

    /**
     * Returns a string representation of this enemy.
     *
     * @return a string showing the enemy's name, HP, and attack
     */
    @Override
    public String toString() {
        return name + " HP:" + hp + " ATK:" + attack;
    }
}