package rpg;

public class Weapon extends Item {
    private int attackValue;

    // Generation bounds for weapons
    public static int WEAPON_MIN_ATTACK = 1;
    public static int WEAPON_MAX_ATTACK = 6;

    public Weapon(String name, int attackValue) {
        super(name);
        this.attackValue = attackValue;
    }

    public int getAttackValue() { return attackValue; }

    @Override
    public String toString() {
        return name + " (WEAPON, atk=" + attackValue + ")";
    }

    public enum WeaponType {
        RUSTY_SWORD("Rusty Sword"),
        SHORT_BOW("Short Bow"),
        WOODEN_STAFF("Wooden Staff");

        private final String displayName;
        WeaponType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
