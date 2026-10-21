/**
 * Both sides need the same basic rules for health and damage. Keeping those
 * rules here means a fix applies to every tank. The class is abstract because
 * the game needs an actual player or enemy tank with its own attack behavior.
 */
public abstract class Tank {
    // Identity and starting combat stats stay fixed for one tank; health changes
    // during a battle. Subclasses share these fields through protected access.
    protected final String name;
    protected int health;
    protected final int maxHealth;
    protected final int damage;

    protected Tank(String name, int health, int damage) {
        // Requiring starting values helps every subclass begin with usable state.
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
    }

    public void takeDamage(int amount) {
        // Put the health boundary in one place so every attacker follows the
        // same rule. A bad damage value should not heal or produce negative hull.
        health = Math.max(0, health - Math.max(0, amount));
    }

    public boolean isAlive() {
        // Ask this question wherever survival matters instead of repeating a
        // health comparison throughout the rest of the program.
        return health > 0;
    }

    // Small query methods give the menu a consistent way to inspect a tank.
    // Changes to health can then be expressed through actions such as takeDamage.
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    // Every tank promises an attack, but its subclass chooses the details.
    // Accepting Tank lets either kind of tank be an opponent without requiring
    // the attacker to know the opponent's concrete class.
    public abstract void attack(Tank target);
}
