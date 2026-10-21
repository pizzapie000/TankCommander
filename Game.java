import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tank classes decide what their actions mean; this class decides when those
 * actions happen. Keeping the mission sequence here makes turn order easier to
 * understand without mixing it into each tank's resource rules.
 */
public class Game {
    // Keep one input reader and one encounter collection for the mission.
    // Creating a fresh tank object for each list entry gives each enemy its own health, even when two enemies have the same type.
    private final Scanner input = new Scanner(System.in);
    private final ArrayList<EnemyTank> enemies = new ArrayList<>();
    private PlayerTank player;
    private String commanderName;
    private int enemiesDestroyed;
    // Health alone cannot explain every ending: retreat and closed input can
    // end a mission while the player's tank is still alive.
    private boolean retreated;
    private boolean inputClosed;

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }

    public void run() {
        // Keep setup, the encounter sequence, and the final report in one place so you can read the overall mission before looking at each turns.
        System.out.println("Welcome to TANK COMMANDER");
        System.out.print("Commander name: ");
        if (!input.hasNextLine()) {
            System.out.println("\nNo input available. Mission cancelled.");
            return;
        }

        commanderName = input.nextLine().trim();
        // A default name keeps an empty response from producing awkward output.
        if (commanderName.isEmpty()) {
            commanderName = "Commander";
        }
        player = new PlayerTank("Ironclad");
        spawnEnemies();

        System.out.println("\nLead your platoon through " + enemies.size() + " encounters.");
        System.out.println("You control the platoon's command tank, " + player.getName() + ".");
        System.out.println("Fire: 30 damage, with a 20% chance to deal 60 damage.");
        System.out.println("Repair: restores up to 40 hull integrity and uses one kit.");
        System.out.println("Firing or repairing lets a surviving enemy fire back.");
        System.out.println("Scanning is free. Invalid choices do not use a turn.");
        System.out.println("Between encounters: up to 3 shells and 1 repair kit are added.");

        // The outer loop continues the mission. battle has its own inner loop because each encounter might need a different number of turns.
        for (int i = 0; i < enemies.size() && player.isAlive(); i++) {
            EnemyTank enemy = enemies.get(i);
            System.out.println("\n--- ENCOUNTER " + (i + 1) + " OF " + enemies.size() + " ---");
            System.out.println("Enemy " + enemy.getEnemyType() + " appears!");
            battle(enemy);

            if (retreated || inputClosed) {
                break;
            }
            if (!enemy.isAlive()) {
                enemiesDestroyed++;
                System.out.println("Enemy destroyed!");
                if (enemiesDestroyed < enemies.size()) {
                    player.resupply();
                }
            }
        }

        printResults();
    }

    private void spawnEnemies() {
        // A fixed order makes early experiments easier to compare and I could add random encounters once the basic rules are familiar like in Pokemon.
        enemies.add(new EnemyTank(EnemyTank.SCOUT));
        enemies.add(new EnemyTank(EnemyTank.MEDIUM));
        enemies.add(new EnemyTank(EnemyTank.SCOUT));
        enemies.add(new EnemyTank(EnemyTank.MEDIUM));
        enemies.add(new EnemyTank(EnemyTank.HEAVY));
    }

    private void battle(EnemyTank enemy) {
        while (player.isAlive() && enemy.isAlive()) {
            printMenu(enemy);
            int choice = readChoice();
            boolean usedTurn = false;

            switch (choice) {
                case 1:
                    // Tank's common attack method returns void, so this menu checks ammunition before marking the action as a turn.
                    if (player.getAmmo() > 0) {
                        player.attack(enemy);
                        usedTurn = true;
                    } else {
                        System.out.println("No shells remaining! Choose Retreat to return to base.");
                    }
                    break;
                case 2:
                    // repair reports whether it really used a kit. The menu needs that answer to keep turn costs fair.
                    usedTurn = player.repair();
                    break;
                case 3:
                    scanEnemy(enemy);
                    break;
                case 4:
                    retreated = true;
                    System.out.println("You order the platoon to retreat to base.");
                    return;
                case -1:
                    inputClosed = true;
                    System.out.println("\nInput closed. Mission ended.");
                    return;
                default:
                    System.out.println("Please enter a number from 1 to 4.");
            }

            // Resolve retaliation after the player's action so a finishing shot prevents a return shot. Keeping this in one place avoids accidentally giving different menu actions different turn order.
            if (usedTurn && enemy.isAlive()) {
                enemy.attack(player);
            }
        }
    }

    private void printMenu(EnemyTank enemy) {
        System.out.println("\nCommander: " + commanderName);
        System.out.println("Tank: " + player.getName());
        System.out.println("Hull Integrity: " + player.getHealth() + "/" + player.getMaxHealth());
        System.out.println("Ammo: " + player.getAmmo() + "/" + PlayerTank.MAX_AMMO);
        System.out.println("Repair Kits: " + player.getRepairKits() + "/" + PlayerTank.MAX_REPAIR_KITS);
        System.out.println("Enemies Destroyed: " + enemiesDestroyed + "/" + enemies.size());
        System.out.println("Enemy: " + enemy.getEnemyType());
        System.out.println("\n1. Fire Cannon");
        System.out.println("2. Repair");
        System.out.println("3. Scan Enemy");
        System.out.println("4. Retreat");
        System.out.print("Choice: ");
    }

    private int readChoice() {
        // Read a whole line before converting it. This handles bad input in one place and avoids mixing Scanner's numeric and line reading methods.
        if (!input.hasNextLine()) {
            return -1;
        }
        String line = input.nextLine().trim();
        try {
            int choice = Integer.parseInt(line);
            // Keep closed input distinct from a bad choice: -1 ends the mission,
            // while 0 lets the menu explain the mistake and ask again.
            return choice >= 1 && choice <= 4 ? choice : 0;
        } catch (NumberFormatException exception) {
            // Invalid input is an expected user mistake, so recover locally
            // rather than let it end the whole game.
            return 0;
        }
    }

    private void scanEnemy(EnemyTank enemy) {
        // Free information encourages the player to plan. Turn costs are figured out in battle, so this method only presents what the tank actually knows.
        System.out.println("\n--- ENEMY SCAN ---");
        System.out.println("Type: " + enemy.getEnemyType());
        System.out.println("Enemy Hull: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
        System.out.println("Cannon Damage: " + enemy.getDamage());
        System.out.println("Scanning uses no ammunition and gives the enemy no turn.");
    }

    private void printResults() {
        // One results method keeps the score consistent across victory, defeat, retreat, and an interrupted input stream.
        System.out.println("\n=== MISSION RESULTS ===");
        if (enemiesDestroyed == enemies.size()) {
            System.out.println("MISSION COMPLETE");
        } else if (!player.isAlive()) {
            System.out.println("GAME OVER - Your command tank was destroyed.");
        } else if (retreated) {
            System.out.println("MISSION ABORTED - Your platoon returned to base.");
        } else {
            System.out.println("MISSION ENDED - Input was closed.");
        }
        System.out.println("Commander: " + commanderName);
        System.out.println("Enemies Destroyed: " + enemiesDestroyed + "/" + enemies.size());
        System.out.println("Hull Remaining: " + player.getHealth() + "/" + player.getMaxHealth());
        System.out.println("Ammo Remaining: " + player.getAmmo());
        System.out.println("Repair Kits Remaining: " + player.getRepairKits());
    }
}
