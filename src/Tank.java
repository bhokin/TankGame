import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Tank extends WorldObj {

    private boolean faceNorth;
    private boolean faceSouth;
    private boolean faceEast;
    private boolean faceWest;

    private Image tankImage;

    public Tank() {
    }

    public Tank(int x, int y, String face) {
        super(x, y);
        setHitBoxSize(20);
        setFace(face);
    }

    public void setFace(String face) {
        if (Objects.equals(face, "north")) {
            faceNorth = true; faceSouth = false; faceEast = false; faceWest = false;
            tankImage = new ImageIcon("img/tank1_up.png").getImage();
        } else if (Objects.equals(face, "south")) {
            faceNorth = true; faceSouth = true; faceEast = false; faceWest = false;
            tankImage = new ImageIcon("img/tank1_down.png").getImage();
        } else if (Objects.equals(face, "east")) {
            faceNorth = true; faceSouth = false; faceEast = true; faceWest = false;
            tankImage = new ImageIcon("img/tank1_right.png").getImage();
        } else if (Objects.equals(face, "west")) {
            faceNorth = true; faceSouth = false; faceEast = false; faceWest = true;
            tankImage = new ImageIcon("img/tank1_left.png").getImage();
        }
    }

    public Image getTankImage() {
        return tankImage;
    }
}