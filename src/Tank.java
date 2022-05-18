import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Tank extends WorldObj {

    private boolean faceNorth;
    private boolean faceSouth;
    private boolean faceEast;
    private boolean faceWest;

    private int tankNum;
    private Image tankImage;

    public boolean alive;
    private boolean buffed;

    public Tank() {
    }

    public Tank(int x, int y, int tankNum, String face) {
        super(x, y);
        setHitBoxSize(25);
        this.alive = true;
        this.buffed = false;
        this.tankNum = tankNum;
        setFace(face);
    }

    public void setFace(String face) {
        if (Objects.equals(face, "north")) {
            faceNorth = true; faceSouth = false; faceEast = false; faceWest = false;
            tankImage = new ImageIcon("img/tank" + tankNum + "_up.png").getImage();
        } else if (Objects.equals(face, "south")) {
            faceNorth = false; faceSouth = true; faceEast = false; faceWest = false;
            tankImage = new ImageIcon("img/tank" + tankNum + "_down.png").getImage();
        } else if (Objects.equals(face, "east")) {
            faceNorth = false; faceSouth = false; faceEast = true; faceWest = false;
            tankImage = new ImageIcon("img/tank" + tankNum + "_right.png").getImage();
        } else if (Objects.equals(face, "west")) {
            faceNorth = false; faceSouth = false; faceEast = false; faceWest = true;
            tankImage = new ImageIcon("img/tank" + tankNum + "_left.png").getImage();
        }
    }

    public boolean isShot(Bullet bullet) {
        return getX() + getHitBoxSize() / 2 >= bullet.getX() - bullet.getHitBoxSize() / 2 &&
                getY() + getHitBoxSize() / 2 >= bullet.getY() - bullet.getHitBoxSize() / 2 &&
                getX() - getHitBoxSize() / 2 <= bullet.getX() + bullet.getHitBoxSize() / 2 &&
                getY() - getHitBoxSize() / 2 <= bullet.getY() + bullet.getHitBoxSize() / 2 &&
                (!bullet.getOwner().equals(this));
    }

    public Image getTankImage() {
        return tankImage;
    }

    public boolean isFaceEast() {
        return faceEast;
    }

    public boolean isFaceNorth() {
        return faceNorth;
    }

    public boolean isFaceSouth() {
        return faceSouth;
    }

    public boolean isFaceWest() {
        return faceWest;
    }

    public boolean isBuffed() {
        return buffed;
    }

    public void setBuffed(boolean buffed) {
        this.buffed = buffed;
    }
}