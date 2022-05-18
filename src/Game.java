import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Game extends JFrame implements Observer {

    private int size = 500;
    private int worldSize = 20;
    private World world;
    private boolean singlePlayerMode;

    private Renderer renderer;
    private MenuUI menuUI;

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
        requestFocus();
    }

    public void start() {
        setVisible(true);
        startMenu();
    }

    public void startMenu() {
        menuUI = new MenuUI();
        add(menuUI);
    }

    public void messageEndGame() {
        String message, title;
        if (singlePlayerMode) {
            if (world.getTank2().alive) {
                message = "You lose to AI";
                title = "You lose!";
            } else {
                message = "You win against AI";
                title = "You win!";
            }
        } else {
            int playerWin = 1;
            if (world.getTank2().alive) {
                playerWin = 2;
            }
            message = "Congratulation to Player " + playerWin;
            title = "Player" + playerWin + "Win!";
        }
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    @Override
    public void update(Observable o, Object arg) {
        renderer.repaint();
        if (!world.getTank1().alive || !world.getTank2().alive) {
            messageEndGame();
            System.exit(0);
        }
    }

    class MenuUI extends JPanel {
        private JButton singlePlayerButton;
        private JButton multiPlayerButton;
        private JLabel menuLabel;

        public MenuUI() {
            setPreferredSize(new Dimension(size, size));
            setLayout(new FlowLayout());
            menuLabel = new JLabel("Select Player Mode");
            add(menuLabel);
            singlePlayerButton = new JButton("Single-Player");
            singlePlayerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    singlePlayerButton.setVisible(false);
                    multiPlayerButton.setVisible(false);
                    world.startSinglePlayer(new RandomRunAndGunStrategy());
                    singlePlayerMode = true;
                    Game.this.requestFocus();
                }
            });
            add(singlePlayerButton);
            multiPlayerButton = new JButton("Multi-Player");
            multiPlayerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    singlePlayerButton.setVisible(false);
                    multiPlayerButton.setVisible(false);
                    world.startMultiPlayer();
                    singlePlayerMode = false;
                    Game.this.requestFocus();
                }
            });
            add(multiPlayerButton);
            pack();
        }
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
//            paintGrids(g);
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
                g.drawOval(bullet.getX() - (bullet.getHitBoxSize() / 2),
                        bullet.getY() - (bullet.getHitBoxSize() / 2),
                        bullet.getHitBoxSize() / 2,
                        bullet.getHitBoxSize() / 2);
                g.fillOval(bullet.getX() - (bullet.getHitBoxSize() / 2),
                        bullet.getY() - (bullet.getHitBoxSize() / 2),
                        bullet.getHitBoxSize(),
                        bullet.getHitBoxSize());
            }
        }

        private void paintBlocks(Graphics g) {
            Image image;
            for(Block block : world.getBlocks()) {
                if (block instanceof BlockBuff) {
                    g.setColor(Color.black);
                    g.fillOval(block.getX() - (block.getSize() / 2),
                            block.getY() - (block.getSize() / 2),
                            block.getSize() / 2,
                            block.getSize() / 2);
                    g.setColor(Color.red);
                    g.drawOval(block.getX() - (block.getSize() / 2),
                            block.getY() - (block.getSize() / 2),
                            block.getSize() / 2,
                            block.getSize() / 2);
                } else {
                    image = brickImage;
                    if (block instanceof BlockSteel) {
                        image = steelImage;
                    } else if (block instanceof BlockTree) {
                        image = treeImage;
                    }
                    g.drawImage(image, block.getX() - (block.getSize() / 2),
                            block.getY() - (block.getSize() / 2),
                            block.getSize(), block.getSize(),
                            null, null);
                }
            }
        }
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!singlePlayerMode) {
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
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    Command c = new CommandShoot(world.getTank2(), world);
                    c.execute();
                }
            }
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
            if (e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                Command c = new CommandShoot(world.getTank1(), world);
                c.execute();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (!singlePlayerMode) {
                if ((e.getKeyCode() == KeyEvent.VK_W) && world.getTank2().isFaceNorth() ||
                        (e.getKeyCode() == KeyEvent.VK_S) && world.getTank2().isFaceSouth() ||
                        (e.getKeyCode() == KeyEvent.VK_A) && world.getTank2().isFaceWest() ||
                        (e.getKeyCode() == KeyEvent.VK_D) && world.getTank2().isFaceEast()) {
                    Command c = new CommandStopMoving(world.getTank2());
                    c.execute();
                }
            }
            if ((e.getKeyCode() == KeyEvent.VK_UP) && world.getTank1().isFaceNorth() ||
                    (e.getKeyCode() == KeyEvent.VK_DOWN) && world.getTank1().isFaceSouth() ||
                    (e.getKeyCode() == KeyEvent.VK_LEFT) && world.getTank1().isFaceWest() ||
                    (e.getKeyCode() == KeyEvent.VK_RIGHT) && world.getTank1().isFaceEast() ) {
                Command c = new CommandStopMoving(world.getTank1());
                c.execute();
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}