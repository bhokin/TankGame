public class Bullet extends WorldObj{

    private Tank owner;

    public Bullet() {
    }

    public Bullet(int x, int y, int dx, int dy, Tank owner) {
        super(x, y);
        setSpeed(6);
        setHitBoxSize(10);
        setDx(dx);
        setDy(dy);
        this.owner = owner;
    }

    public void refreshState(int x, int y, int dx, int dy, Tank owner) {
        setX(x);
        setY(y);
        setDx(dx);
        setDy(dy);
        this.owner = owner;
    }

    public Tank getOwner() {
        return owner;
    }
}
