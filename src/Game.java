import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Game extends JFrame implements Observer {

    private int size = 500;
    private int worldSize = 20;
    private World world;

    private Renderer renderer;

    public Game() {
        super();
        addKeyListener(new Controller());
        setLayout(new BorderLayout());
        renderer = new Renderer();
        add(renderer, BorderLayout.CENTER);
        world = new World(worldSize);
        world.addObserver(this);
        setSize(size + 20, size + 40);
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
        private Image brickImage = new ImageIcon("img/brick_wall.png").getImage();
        private Image steelImage = new ImageIcon("img/steel_wall.png").getImage();
        private Image treeImage = new ImageIcon("img/tree.png").getImage();

        public Renderer() {
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintGrids(g);
            paintTank(g);
            paintBullets(g);
            paintBlocks(g);
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
            int xTank1 = world.getTank1().getX();
            int yTank1 = world.getTank1().getY();
            int xTank2 = world.getTank2().getX();
            int yTank2 = world.getTank2().getY();
            int hitBoxTank1 = world.getTank1().getHitBoxSize();
            int hitBoxTank2 = world.getTank2().getHitBoxSize();
            g.drawImage(world.getTank1().getTankImage(),
                    xTank1 - (hitBoxTank1 / 2), yTank1 - (hitBoxTank1 / 2),
                    hitBoxTank1, hitBoxTank1, null, null);
            g.drawImage(world.getTank2().getTankImage(),
                    xTank2 - (hitBoxTank2 / 2), yTank2 - (hitBoxTank2 / 2),
                    hitBoxTank2, hitBoxTank2, null, null);
            g.setColor(Color.red);
            g.drawRect(xTank1 - (hitBoxTank1 / 2), yTank1 - (hitBoxTank1 / 2), hitBoxTank1, hitBoxTank1);
            g.drawRect(xTank2 - (hitBoxTank2 / 2), yTank2 - (hitBoxTank2 / 2), hitBoxTank2, hitBoxTank2);
        }

        private void paintBullets(Graphics g) {
            g.setColor(Color.green);
            for(Bullet bullet : world.getBullets()) {
                g.fillOval(bullet.getX() - (bullet.getHitBoxSize() / 2),
                        bullet.getY() - (bullet.getHitBoxSize() / 2),
                        bullet.getHitBoxSize(),
                        bullet.getHitBoxSize());
            }
        }

        private void paintBlocks(Graphics g) {
            for(Block block : world.getBlocks()) {
                Image image = brickImage;
                if (block instanceof BlockSteel) {
                    image = steelImage;
                } else if (block instanceof BlockTree) {
                    image = treeImage;
                }
                g.drawImage(image, block.getX() - (block.getSize() / 2),
                        block.getY() - (block.getSize() / 2),
                        block.getSize(), block.getSize(),
                        null, null);
                g.setColor(Color.black);
                g.drawRect(block.getX() - (block.getSize() / 2),
                        block.getY() - (block.getSize() / 2),
                        block.getSize(),
                        block.getSize());
            }
        }
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                Command c = new CommandTurnNorth(world.getTank1());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                Command c = new CommandTurnSouth(world.getTank1());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                Command c = new CommandTurnWest(world.getTank1());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                Command c = new CommandTurnEast(world.getTank1());
                c.execute();
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                Command c = new CommandTurnNorth(world.getTank2());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                Command c = new CommandTurnSouth(world.getTank2());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_A) {
                Command c = new CommandTurnWest(world.getTank2());
                c.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                Command c = new CommandTurnEast(world.getTank2());
                c.execute();
            }
            if (e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                world.burstBullets(world.getTank1());
            }
            if (e.getKeyCode() == KeyEvent.VK_G) {
                world.burstBullets(world.getTank2());
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
