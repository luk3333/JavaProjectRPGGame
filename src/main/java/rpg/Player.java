
package rpg;

public class Player {
    private String name;
    private int hp;
    private int attack;
    private int exp;
    private int level;
    private int expNeeded;

    public Player(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.exp = 0;
        this.level = 1;
        this.expNeeded = 100; // Experience needed to level up
    }

    public void takeDamage(int d) { hp = Math.max(0, hp - d); }
    public void heal(int d) { hp += d; }
    public boolean isAlive() { return hp > 0; }
    public int getAttack() { return attack; }
    
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= expNeeded) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        exp = 0;
        System.out.println("*** You leveled up! ***");
        System.out.println("Level: " + level);
    }
    
    public int getLevel() { return level; }
    public int getExp() { return exp; }

    @Override
    public String toString() {
        return name + " HP:" + hp + " ATK:" + attack + " LVL:" + level + " EXP:" + exp + "/" + expNeeded;
    }
}