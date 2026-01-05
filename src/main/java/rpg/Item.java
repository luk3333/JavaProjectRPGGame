
package rpg;

public class Item {
    private String name;
    private int healAmount;

    public Item(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    public String getName() { return name; }
    public int getHealAmount() { return healAmount; }
}