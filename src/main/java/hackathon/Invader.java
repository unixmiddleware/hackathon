package hackathon;

import javax.swing.*;
import java.awt.*;

public class Invader {
    private int x, y;
    private Image image;
    private String issue;

    public Invader(String issue) {
        this.issue = issue;
        initInvader();
    }

    private void initInvader() {
        loadImage();
        x = (int) (Math.random() * 750);
        y = (int) (Math.random() * 100);
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/main/resources/invader.png");
        image = ii.getImage();
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

    public String getIssue() {
        return issue;
    }
}
