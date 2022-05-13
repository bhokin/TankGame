public abstract class WorldObj {

    private int x;
    private int y;

    private int dx;
    private int dy;

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
        this.x += dx;
        this.y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hit(WorldObj wObj) {
        return x == wObj.x && y == wObj.y;
    }
}
