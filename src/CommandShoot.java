public class CommandShoot extends Command{

    private World world;

    public CommandShoot(Tank tank, World world) {
        super(tank);
        this.world = world;
    }
    @Override
    public void execute() {
        world.burstBullets(getTank());
    }
}