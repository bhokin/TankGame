import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class World extends Observable {

    private int worldSize;
    private Tank tank;
    private List<Bullet> bullets;
    private BulletPool bulletPool;

    private Thread mainThread;
    private long delayed = 10;

    public World(int size) {
        this.worldSize = size;
        tank = new Tank(0, 0);
        bullets = new ArrayList<Bullet>();
        bulletPool = new BulletPool();
    }

    public void start() {
        tank.setPosition(worldSize, worldSize);
        mainThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    tank.move();
                    moveBullets();
//                    cleanupBullets();
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
                    bullet.getX() >= worldSize ||
                    bullet.getY() <= 0 ||
                    bullet.getY() >= worldSize) {
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

    public Tank getTank() {
        return tank;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
