package hackathon;

import javax.swing.*;
import java.awt.*;

public class Invader {
    private int x, y;
    private int dy;
    private Image image;
    private String issue;
    private boolean visible;

    public Invader(String issue) {
        this.issue = issue;
        initInvader();
    }

    private void initInvader() {
        loadImage();
        x = (int) (Math.random() * 750);
        y = (int) (Math.random() * 100);
        dy = 1;
        visible = true;
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/main/resources/alien1.png");
        image = ii.getImage();
    }

    public void move() {
        y += dy;

        if (y > 600) {
            y = -50;
            x = (int) (Math.random() * 750);
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

    public String getIssue() {
        return issue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
