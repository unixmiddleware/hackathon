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

    private SoundEffect shootSound;
    private SoundEffect hitSound;
    private BackgroundMusic backgroundMusic;

    private boolean inGame;
    private JButton closeButton;

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

        shootSound = new SoundEffect("shoot.wav");
        hitSound = new SoundEffect("hit.wav");
        backgroundMusic = new BackgroundMusic("background_music.wav");

        inGame = true;

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

        backgroundMusic.play();
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
        shootSound.play();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            drawBackground(g);
            drawObjects(g);
        } else {
            drawGameOver(g);
        }
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

    private void drawGameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(message, (800 - metrics.stringWidth(message)) / 2, 300);

        if (closeButton == null) {
            closeButton = new JButton("Close Game");
            closeButton.setBounds(350, 350, 150, 50);
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    backgroundMusic.stop();
                    System.exit(0);
                }
            });
            setLayout(null);
            add(closeButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            updateBullets();
            updateInvaders();
            player.move();
            checkCollisions();
            checkGameOver();
            repaint();
        }
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
            if (invader.getY() > 600) {
                inGame = false;
            }
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
                    hitSound.play();
                    showRuntimeIssue(invader.getIssue());
                }
            }
        }
    }

    private void checkGameOver() {
        if (invaders.isEmpty()) {
            inGame = false;
        }
    }

    private void showRuntimeIssue(String issue) {
        JOptionPane.showMessageDialog(this, "Runtime Issue: " + issue, "Issue Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
