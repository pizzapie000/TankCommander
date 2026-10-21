import java.util.Random;

/**
 * Supply management belongs to the player's tank because it determines which
 * actions that tank can perform. Enemy tanks can stay simpler while the player
 * has decisions to make about ammunition and repairs.
 */
public class PlayerTank extends Tank {
    // Named constants make the rules easy to tune without hunting for numbers
    // scattered through several methods.
    public static final int MAX_AMMO = 10;
    public static final int MAX_REPAIR_KITS = 3;
    public static final int REPAIR_AMOUNT = 40;

    // Keeping supplies private puts the rules for spending them in this class.
    private int ammo;
    private int repairKits;
    private final Random random;

    public PlayerTank(String name) {
        // Let the parent establish shared tank state before adding player rules.
        super(name, 100, 30);
        ammo = MAX_AMMO;
        repairKits = MAX_REPAIR_KITS;
        // Reuse one generator across shots rather than create one per attack.
        random = new Random();
    }

    @Override
    public void attack(Tank target) {
        // Preserve the common attack entry point promised by Tank. fireShell
        // holds the player-specific rules and can also report success or failure.
        fireShell(target);
    }

    public boolean fireShell(Tank target) {
        // Refuse an impossible action before spending supplies. Returning a
        // boolean gives callers a way to distinguish refusal from success.
        if (!isAlive() || !target.isAlive()) {
            return false;
        }
        if (ammo <= 0) {
            System.out.println("No shells remaining! You must retreat.");
            return false;
        }

        ammo--;
        // Use a temporary value so a critical hit cannot permanently increase
        // the tank's normal damage on later turns.
        int shellDamage = damage;
        // Twenty of the one hundred possible results qualify. This keeps the
        // probability rule visible and easy to experiment with while learning.
        if (random.nextInt(100) < 20) {
            shellDamage *= 2;
            System.out.println("CRITICAL HIT! Your shell strikes a weak spot!");
        }

        target.takeDamage(shellDamage);
        System.out.println("You fire the main cannon for " + shellDamage + " damage!");
        return true;
    }

    public boolean repair() {
        // The game uses this result to decide whether the enemy gets a turn.
        // Check every reason to refuse before using a kit, so a harmless menu
        // mistake does not cost the player a resource or a turn.
        if (!isAlive()) {
            return false;
        }
        if (repairKits <= 0) {
            System.out.println("No repair kits remaining.");
            return false;
        }
        if (health == maxHealth) {
            System.out.println("Your hull is already at full integrity.");
            return false;
        }

        // Keep the previous value so feedback reports the actual repair. Near
        // full health, the tank may benefit from fewer than forty points.
        int previousHealth = health;
        health = Math.min(maxHealth, health + REPAIR_AMOUNT);
        repairKits--;
        System.out.println("Field repairs restore " + (health - previousHealth)
                + " hull integrity.");
        return true;
    }

    public void resupply() {
        // Partial resupply rewards a victory while preserving decisions about
        // supplies. Capacity limits keep later encounters from accumulating
        // unlimited ammunition and kits.
        int previousAmmo = ammo;
        int previousKits = repairKits;
        ammo = Math.min(MAX_AMMO, ammo + 3);
        repairKits = Math.min(MAX_REPAIR_KITS, repairKits + 1);
        System.out.println("Supply truck: +" + (ammo - previousAmmo) + " shells, +"
                + (repairKits - previousKits) + " repair kit(s).");
    }

    public int getAmmo() {
        return ammo;
    }

    public int getRepairKits() {
        return repairKits;
    }
}
