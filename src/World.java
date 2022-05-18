import javax.swing.*;
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
        tank1 = new Tank(-25, -25, 1, "east");
        tank2 = new Tank(-25, -25, 2, "west");
        bullets = new ArrayList<Bullet>();
        blocks = new ArrayList<Block>();
        bulletPool = new BulletPool();
    }

    private void gameThreadLogic() {
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

    public void startSinglePlayer(AIStrategy aIStrategy) {
        tank1.setPosition(blockSize*2, 250);
        tank2.setPosition(500 - blockSize*2, 250);
        generateBlocks();
        generateBuffs();
        mainThread = new Thread() {
            @Override
            public void run() {
                while (tank1.alive && tank2.alive) {
                    gameThreadLogic();
                    Command command = aIStrategy.getNextMoveCommand(World.this, tank2);
                    if (command != null) {
                        command.execute();
                }
            }
            }
        };
        mainThread.start();
    }

    public void startMultiPlayer() {
        tank1.setPosition(blockSize*2, 250);
        tank2.setPosition(500 - blockSize*2, 250);
        generateBlocks();
        generateBuffs();
        mainThread = new Thread() {
            @Override
            public void run() {
                while (tank1.alive && tank2.alive) {
                    gameThreadLogic();
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
        if (tank.isBuffed()) {
            if (tank.isFaceNorth() || tank.isFaceSouth()) {
                bullets.add(bulletPool.requestBullet(tank.getX() - 15, tank.getY(), dx, dy, tank));
                bullets.add(bulletPool.requestBullet(tank.getX() + 15, tank.getY(), dx, dy, tank));
            } else if (tank.isFaceEast() || tank.isFaceWest()) {
                bullets.add(bulletPool.requestBullet(tank.getX(), tank.getY() - 15, dx, dy, tank));
                bullets.add(bulletPool.requestBullet(tank.getX(), tank.getY() + 15, dx, dy, tank));
            }
        }
    }

    private void generateBlocks() {
        for(int i = 0; i < blockSize; i++) {
            blocks.add(new BlockSteel(12, (i * 25) + 12, 25));
            blocks.add(new BlockSteel(487, (i * 25) + 12, 25));
            blocks.add(new BlockSteel((i * 25) + 12, 487, 25));
            blocks.add(new BlockSteel((i * 25) + 12, 12, 25));
        }
        for(int i = 0; i < 6; i++) {
            blocks.add(new BlockBrick(212, (i * 25) + 37, 25));
            blocks.add(new BlockBrick(287, (i * 25) + 37, 25));
            blocks.add(new BlockSteel(212, (i * 25) + 187, 25));
            blocks.add(new BlockSteel(287, (i * 25) + 187, 25));
            blocks.add(new BlockBrick(212, (i * 25) + 339, 25));
            blocks.add(new BlockBrick(287, (i * 25) + 339, 25));
            blocks.add(new BlockSteel(112, (i * 25) + 187, 25));
            blocks.add(new BlockSteel(387, (i * 25) + 187, 25));
        }
        for(int i = 0; i < 18; i++) {
            blocks.add(new BlockTree(162, (i * 25) + 37, 25));
            blocks.add(new BlockTree(237, (i * 25) + 37, 25));
            blocks.add(new BlockTree(263, (i * 25) + 37, 25));
            blocks.add(new BlockTree(337, (i * 25) + 37, 25));
        }
        for(int i = 0; i < 3; i++) {
            blocks.add(new BlockSteel(187, (i * 25) + 37, 25));
            blocks.add(new BlockSteel(312, (i * 25) + 37, 25));
            blocks.add(new BlockSteel(187, (i * 25) + 412, 25));
            blocks.add(new BlockSteel(312, (i * 25) + 412, 25));
            blocks.add(new BlockBrick((i * 25) + 37, 212, 25));
            blocks.add(new BlockBrick((i * 25) + 37, 287, 25));
            blocks.add(new BlockBrick((i * 25) + 412, 212, 25));
            blocks.add(new BlockBrick((i * 25) + 412, 287, 25));
        }
        for(int i = 0; i < 2; i++) {
            blocks.add(new BlockBrick(87, (i * 25) + 237, 25));
            blocks.add(new BlockBrick(412, (i * 25) + 237, 25));
        }
    }

    private void generateBuffs() {
        blocks.add(new BlockBuff(255, (9 * 25) + 37, 20));
    }

    public void checkTankCollision(Tank tank) {
        for(Block block : blocks) {
            if (tank.hitBlock(block)) {
                if (!block.isPassable()) {
                    tank.setPosition(tank.getX() - (tank.getDx() * tank.getSpeed()),
                            tank.getY() - (tank.getDy() * tank.getSpeed()));
                    break;
                }
                if (block.isBuff()) {
                    blocks.remove(block);
                    tank.setBuffed(true);
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
                    if (!block.isPassable()) {
                        bullets.remove(bullet);
                        bulletPool.releaseBullet(bullet);
                        if (block.isBreakable()) {
                            blocks.remove(block);
                            isBreak = true;
                        }
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
