package rpg;

public enum HeroClass {
    WARRIOR("Warrior", 100, 10),
    ARCHER("Archer", 80, 15),
    MAGE("Mage", 50, 25);

    private String name;
    private int hp;
    private int attack;

    HeroClass(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
}