import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class World extends Observable {

    private int blockSize;
    private Tank tank1, tank2;
    private List<Bullet> bullets;
    private BulletPool bulletPool;
    private List<Block> blocks;

    private Thread mainThread;
    private long delayed = 10;

    public World(int size) {
        this.blockSize = size;
        tank1 = new Tank(0, 0, 1, "east");
        tank2 = new Tank(0, 0, 2, "west");
        bullets = new ArrayList<Bullet>();
        blocks = new ArrayList<Block>();
        bulletPool = new BulletPool();
    }

    public void start() {
        tank1.setPosition(blockSize, 250);
        tank2.setPosition(500 - blockSize, 250);
        generateBlocks();
        mainThread = new Thread() {
            @Override
            public void run() {
                while(tank1.alive && tank2.alive) {
                    tank1.move();
                    tank2.move();
                    moveBullets();
                    cleanupBullets();
                    checkTankCollision(tank1);
                    checkTankCollision(tank2);
                    checkTankShot(tank1);
                    checkTankShot(tank2);
                    checkBulletHit();
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

    public void burstBullets(Tank tank) {
        int dx = 0, dy = 0;
        if (tank.isFaceEast()) {
            dx = 1;
        } else if (tank.isFaceNorth()) {
            dy = -1;
        } else if (tank.isFaceSouth()) {
            dy = 1;
        } else if (tank.isFaceWest()) {
            dx = -1;
        }
        bullets.add(bulletPool.requestBullet(tank.getX(), tank.getY(), dx, dy, tank));
    }

    private void generateBlocks() {
        for(int i = 0; i < blockSize / 2; i++) {
            blocks.add(new BlockBrick(50, i * 25, 25));
            blocks.add(new BlockSteel(150, i * 25, 25));
            blocks.add(new BlockTree(250, i * 25, 25));
        }
    }

    public void checkTankCollision(Tank tank) {
        for(Block block : blocks) {
            if (tank.hitBlock(block)) {
                if (block instanceof BlockBrick || block instanceof BlockSteel) {
                    tank.setPosition(tank.getX() - (tank.getDx() * tank.getSpeed()),
                            tank.getY() - (tank.getDy() * tank.getSpeed()));
                    System.out.println("Hit!");
                    break;
                }
            }
        }
    }

    public void checkBulletHit() {
        boolean isBreak = false;
        for(Block block : blocks) {
            for(Bullet bullet : bullets) {
                if (bullet.hitBlock(block)) {
                    if (!(block instanceof BlockTree)) {
                        bullets.remove(bullet);
                        bulletPool.releaseBullet(bullet);
                        if (block instanceof BlockBrick) {
                            blocks.remove(block);
                            isBreak = true;
                        }
                        System.out.println("Bullet Hit!");
                        break;
                    }
                }
            }
            if (isBreak) {
                isBreak = false;
                break;
            }
        }
    }

    public void checkTankShot(Tank tank) {
        for(Bullet bullet : bullets) {
            if (tank.isShot(bullet)) {
                tank.alive = false;
            }
        }
    }

    public Tank getTank1() {
        return tank1;
    }

    public Tank getTank2() {
        return tank2;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
