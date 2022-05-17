public class Bullet extends WorldObj{

    public Bullet() {
    }

    public Bullet(int x, int y, int dx, int dy) {
        super(x, y);
        setHitBoxSize(10);
        setDx(dx);
        setDy(dy);
        setSpeed(6);
    }

    public void refreshState(int x, int y, int dx, int dy) {
        setX(x);
        setY(y);
        setDx(dx);
        setDy(dy);
    }
}
