package hackathon;

import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private int dx, dy;
    private Image image;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        loadImage();
        x = 400;
        y = 550;
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/main/resources/player.png");
        image = ii.getImage();
    }

    public void move() {
        x += dx;
        y += dy;
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

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
