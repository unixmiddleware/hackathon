package hackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private Invader[] invaders;

    public GamePanel() {
        initPanel();
    }

    private void initPanel() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));

        player = new Player();
        invaders = createInvaders();

        timer = new Timer(15, this);
        timer.start();
    }

    private Invader[] createInvaders() {
        Invader[] invaders = new Invader[5];  // Example with 5 invaders
        invaders[0] = new Invader("GC too slow");
        invaders[1] = new Invader("JVM memory issue");
        invaders[2] = new Invader("Disk space low");
        invaders[3] = new Invader("Invalid classpath");
        invaders[4] = new Invader("GC too fast");
        return invaders;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
    }

    private void drawObjects(Graphics g) {
        g.drawImage(player.getImage(), player.getX(), player.getY(), this);

        for (Invader invader : invaders) {
            g.drawImage(invader.getImage(), invader.getX(), invader.getY(), this);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();
        repaint();
    }
}
