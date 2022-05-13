import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Game extends JFrame implements Observer {

    private int size = 500;
    private int worldSize = 25;
    private World world;

    private Renderer renderer;

    public Game() {
        super();
        addKeyListener(new Controller());
        setLayout(new BorderLayout());
        renderer = new Renderer();
        add(renderer, BorderLayout.CENTER);
        world = new World(25);
        world.addObserver(this);
        setSize(size, size);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
        world.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        renderer.repaint();
    }

    class Renderer extends JPanel {
        private final int perCell = size/worldSize;

        public Renderer() {
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintGrids(g);
            paintTank(g);
        }

        private void paintGrids(Graphics g) {
            // Background
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, size, size);

            // Lines
            g.setColor(Color.black);
            for(int i = 0; i < worldSize; i++) {
                g.drawLine(i * perCell, 0, i * perCell, size);
                g.drawLine(0, i * perCell, size, i * perCell);
            }
        }

        private void paintTank(Graphics g) {
            int x = world.getTank().getX();
            int y = world.getTank().getY();
            g.setColor(Color.green);
            g.fillRect(x, y, perCell, perCell);
        }
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                Command c = new CommandTurnNorth(world.getTank());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                Command c = new CommandTurnSouth(world.getTank());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                Command c = new CommandTurnWest(world.getTank());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                Command c = new CommandTurnEast(world.getTank());
                c.execute();
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
