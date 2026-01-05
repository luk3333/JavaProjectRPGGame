
package rpg;

public abstract class Item {
    protected String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public abstract String toString();

    public static Item createRandom(java.util.Random random) {
        int t = random.nextInt(3); // 0 = potion, 1 = weapon, 2 = armor
        switch (t) {
            case 0: {
                int heal = random.nextInt(Potion.POTION_MAX_HEAL - Potion.POTION_MIN_HEAL + 1) + Potion.POTION_MIN_HEAL;
                String pname = "HP Potion (" + heal + ")";
                return new Potion(pname, heal);
            }
            case 1: {
                Weapon.WeaponType[] types = Weapon.WeaponType.values();
                Weapon.WeaponType choice = types[random.nextInt(types.length)];
                int atk = random.nextInt(Weapon.WEAPON_MAX_ATTACK - Weapon.WEAPON_MIN_ATTACK + 1) + Weapon.WEAPON_MIN_ATTACK;
                return new Weapon(choice.getDisplayName(), atk);
            }
            default: {
                Armor.ArmorType[] types = Armor.ArmorType.values();
                Armor.ArmorType choice = types[random.nextInt(types.length)];
                int def = random.nextInt(Armor.ARMOR_MAX_DEFENSE - Armor.ARMOR_MIN_DEFENSE + 1) + Armor.ARMOR_MIN_DEFENSE;
                return new Armor(choice.getDisplayName(), def);
            }
        }
    }
}