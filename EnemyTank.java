/**
 * These enemies differ only in their numbers, so one class can represent all
 * three. Separate ScoutTank and HeavyTank classes would become useful if their
 * behavior differed enough to justify separate implementations.
 */
public class EnemyTank extends Tank {
    // Reusing these labels reduces spelling mistakes when building encounters.
    public static final String SCOUT = "Scout Tank";
    public static final String MEDIUM = "Medium Tank";
    public static final String HEAVY = "Heavy Tank";

    private final String enemyType;

    public EnemyTank(String enemyType) {
        // Translate a type into starting stats once, when the enemy is created.
        // The battle loop can then work with ordinary Tank behavior.
        super(enemyType, healthFor(enemyType), damageFor(enemyType));
        this.enemyType = enemyType;
    }

    // These helpers are static because choosing starting stats does not require
    // an existing enemy object. They are needed during parent construction.
    private static int healthFor(String type) {
        switch (type) {
            case SCOUT:
                return 50;
            case MEDIUM:
                return 90;
            case HEAVY:
                return 150;
            default:
                // A misspelled type is a programming mistake. Make it visible
                // instead of silently creating an enemy with unexpected stats.
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    private static int damageFor(String type) {
        // Keep enemy balancing together so changing a type has a clear home.
        switch (type) {
            case SCOUT:
                return 10;
            case MEDIUM:
                return 18;
            case HEAVY:
                return 25;
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    @Override
    public void attack(Tank target) {
        // Keep enemy attacks predictable for a first game. The checks also
        // protect the rule that a destroyed tank cannot participate in combat.
        if (isAlive() && target.isAlive()) {
            target.takeDamage(damage);
            System.out.println(enemyType + " fires back for " + damage + " damage!");
        }
    }

    public String getEnemyType() {
        return enemyType;
    }
}
