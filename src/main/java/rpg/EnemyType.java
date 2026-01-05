package rpg;

public enum EnemyType {
    GOBLIN("Goblin", 10, 25, 10, 15),
    TROLL("Troll", 40, 50, 15, 25),
    WOLF("Wolf", 20, 35, 10, 20);

    private String name;
    private int minHp;
    private int maxHp;
    private int minAttack;
    private int maxAttack;

    EnemyType(String name, int minHp, int maxHp, int minAttack, int maxAttack) {
        this.name = name;
        this.minHp = minHp;
        this.maxHp = maxHp;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
    }

    public String getName() { return name; }
    public int getMinHp() { return minHp; }
    public int getMaxHp() { return maxHp; }
    public int getMinAttack() { return minAttack; }
    public int getMaxAttack() { return maxAttack; }
}
