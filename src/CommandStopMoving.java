public class CommandStopMoving extends Command{

    public CommandStopMoving(Tank tank) {
        super(tank);
    }

    @Override
    public void execute() {
        getTank().stopMoving();
    }
}