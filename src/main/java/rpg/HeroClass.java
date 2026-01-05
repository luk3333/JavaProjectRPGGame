package rpg;

public enum HeroClass {
    WARRIOR("Warrior", 100, 10, 10),
    ARCHER("Archer", 80, 15, 7),
    MAGE("Mage", 50, 25, 5);

    private String name;
    private int hp;
    private int attack;
    private int inventorySlots;

    HeroClass(String name, int hp, int attack, int inventorySlots) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.inventorySlots = inventorySlots;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
    public int getInventorySlots() { return inventorySlots; }
}