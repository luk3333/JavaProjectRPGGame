package rpg;

public class Potion extends Item {
    private int healAmount;

    // Generation bounds for potions
    public static int POTION_MIN_HEAL = 15;
    public static int POTION_MAX_HEAL = 30;

    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    public int getHealAmount() { return healAmount; }

    @Override
    public String toString() {
        return name + " (POTION, heal=" + healAmount + ")";
    }
}
