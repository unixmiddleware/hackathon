package hackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private List<Invader> invaders;
    private List<Bullet> bullets;
    private final int DELAY = 15;
    private Image backgroundImage;

    public GamePanel() {
        initPanel();
    }

    private void initPanel() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));

        loadBackgroundImage();
        
        player = new Player();
        invaders = createInvaders();
        bullets = new ArrayList<>();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    fire();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void loadBackgroundImage() {
        ImageIcon ii = new ImageIcon("src/main/resources/background.png");
        backgroundImage = ii.getImage();
    }

    private List<Invader> createInvaders() {
        List<Invader> invaders = new ArrayList<>();
        invaders.add(new Invader("GC too slow"));
        invaders.add(new Invader("JVM memory issue"));
        invaders.add(new Invader("Disk space low"));
        invaders.add(new Invader("Invalid classpath"));
        invaders.add(new Invader("GC too fast"));
        return invaders;
    }

    private void fire() {
        bullets.add(new Bullet(player.getX() + player.getWidth() / 2, player.getY()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawObjects(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void drawObjects(Graphics g) {
        g.drawImage(player.getImage(), player.getX(), player.getY(), this);

        for (Invader invader : invaders) {
            g.drawImage(invader.getImage(), invader.getX(), invader.getY(), this);
        }

        for (Bullet bullet : bullets) {
            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateBullets();
        updateInvaders();
        player.move();
        checkCollisions();
        repaint();
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();

        while (it.hasNext()) {
            Bullet bullet = it.next();

            if (bullet.isVisible()) {
                bullet.move();
            } else {
                it.remove();
            }
        }
    }

    private void updateInvaders() {
        for (Invader invader : invaders) {
            invader.move();
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletBounds = bullet.getBounds();

            Iterator<Invader> invaderIterator = invaders.iterator();

            while (invaderIterator.hasNext()) {
                Invader invader = invaderIterator.next();
                Rectangle invaderBounds = invader.getBounds();

                if (bulletBounds.intersects(invaderBounds)) {
                    bullet.setVisible(false);
                    invader.setVisible(false);
                    bulletIterator.remove();
                    invaderIterator.remove();
                    showRuntimeIssue(invader.getIssue());
                }
            }
        }
    }

    private void showRuntimeIssue(String issue) {
        JOptionPane.showMessageDialog(this, "Runtime Issue: " + issue, "Issue Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
