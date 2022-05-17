public abstract class WorldObj {

    private int x;
    private int y;

    private int dx;
    private int dy;

    private int speed = 3;
    private int hitBoxSize;

    public WorldObj() {
    }

    public WorldObj(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void turnNorth() {
        dx = 0;
        dy = -1;
    }

    public void turnSouth() {
        dx = 0;
        dy = 1;
    }

    public void turnWest() {
        dx = -1;
        dy = 0;
    }

    public void turnEast() {
        dx = 1;
        dy = 0;
    }

    public void move() {
        this.x += dx * speed;
        this.y += dy * speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getHitBoxSize() {
        return hitBoxSize;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHitBoxSize(int hitBoxSize) {
        this.hitBoxSize = hitBoxSize;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hitBlock(Block block) {
        return x + hitBoxSize / 2 >= block.getX() - block.getSize() / 2 &&
                y + hitBoxSize / 2 >= block.getY() - block.getSize() / 2 &&
                x - hitBoxSize / 2 <= block.getX() + block.getSize() / 2 &&
                y - hitBoxSize / 2 <= block.getY() + block.getSize() / 2;
    }
}
