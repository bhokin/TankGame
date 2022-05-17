import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class World extends Observable {

    private int blockSize;
    private Tank tank;
    private List<Bullet> bullets;
    private BulletPool bulletPool;
    private List<Block> blocks;

    private Thread mainThread;
    private long delayed = 10;

    public World(int size) {
        this.blockSize = size;
        tank = new Tank(0, 0, "east");
        bullets = new ArrayList<Bullet>();
        blocks = new ArrayList<Block>();
        bulletPool = new BulletPool();
    }

    public void start() {
        tank.setPosition(blockSize, 250);
        generateBlocks();
        mainThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    tank.move();
                    moveBullets();
                    cleanupBullets();
                    checkTankCollision();
                    setChanged();
                    notifyObservers();
                    try {
                        Thread.sleep(delayed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mainThread.start();
    }

    private void moveBullets() {
        for(Bullet bullet : bullets) {
            bullet.move();
        }
    }

    private void cleanupBullets() {
        List<Bullet> toRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets) {
            if(bullet.getX() <= 0 ||
                    bullet.getX() >= 500 ||
                    bullet.getY() <= 0 ||
                    bullet.getY() >= 500) {
                toRemove.add(bullet);
            }
        }
        for(Bullet bullet : toRemove) {
            bullets.remove(bullet);
            bulletPool.releaseBullet(bullet);
        }
    }

    public void burstBullets() {
        bullets.add(bulletPool.requestBullet(tank.getX(), tank.getY(), tank.getDx(), tank.getDy()));
    }

    private void generateBlocks() {
        for(int i = 0; i < blockSize / 2; i++) {
            blocks.add(new BlockBrick(50, i * 25, 25));
            blocks.add(new BlockSteel(150, i * 25, 25));
            blocks.add(new BlockTree(250, i * 25, 25));
        }
    }

    public void checkTankCollision() {
        for(Block block : blocks) {
            if (tank.hitBlock(block)) {
                if (block instanceof BlockBrick || block instanceof BlockSteel) {
                    tank.setDx(0);
                    tank.setDy(0);
                    System.out.println("Hit!");
                    break;
                }
            }
        }
    }

    public Tank getTank() {
        return tank;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}