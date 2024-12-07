package hackathon;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int dy;
    private Image image;
    private boolean visible;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        initBullet();
    }

    private void initBullet() {
        loadImage();
        dy = -2;
        visible = true;
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/main/resources/bullet.png");
        image = ii.getImage();
    }

    public void move() {
        y += dy;

        if (y < 0) {
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
