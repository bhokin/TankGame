import java.util.Random;

public class RandomRunAndGunStrategy implements AIStrategy {

    private Random random = new Random();
    private int faceDirection;
    private long lastCommand;
    private long lastShoot;

    public Command getNextMoveCommand(World world, Tank myTank) {
        if (world == null) {
            return null;
        }
        Command command;
        long diff = System.currentTimeMillis() - lastCommand;
        long diffShoot = System.currentTimeMillis() - lastShoot;
        if (diff < 1000) {
            if (diffShoot > 500) {
                command = new CommandShoot(myTank, world);
                lastShoot = System.currentTimeMillis();
                return command;
            }
            return null;
        }
        lastCommand = System.currentTimeMillis();
        faceDirection = random.nextInt(4);

        if (faceDirection == 0) {
            command = new CommandTurnNorth(myTank);
        } else if (faceDirection == 1) {
            command = new CommandTurnSouth(myTank);
        } else if (faceDirection == 2) {
            command = new CommandTurnWest(myTank);
        } else {
            command = new CommandTurnEast(myTank);
        }
        return command;
    }
}
