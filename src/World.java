import javax.swing.*;
import java.util.Observable;

public class World extends Observable {

    private int worldSize;
    private Tank tank;

    private Thread mainThread;
    private long delayed = 10;

    public World(int size) {
        this.worldSize = size;
        tank = new Tank(0, 0);
    }

    public void start() {
        tank.setPosition(worldSize /2, worldSize /2);
        mainThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    tank.move();
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

    public Tank getTank() {
        return tank;
    }
}
