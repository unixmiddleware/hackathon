package hackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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

    // Features to simulate issues
    private boolean memoryLeakEnabled;
    private boolean deadlockEnabled;
    private boolean highCpuUsageEnabled;
    private boolean resourceLeakEnabled;
    private boolean infiniteRecursionEnabled;
    private boolean excessiveLoggingEnabled;

    // Memory leak - A list that will grow indefinitely
    private List<byte[]> memoryLeak;
    private List<Object> resourceLeak;
    private Logger logger = Logger.getLogger(GamePanel.class.getName());

    public GamePanel(String[] args) {
        parseArgs(args);
        initPanel();
    }

    private void parseArgs(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "memoryLeak":
                    memoryLeakEnabled = true;
                    memoryLeak = new LinkedList<>();
                    break;
                case "deadlock":
                    deadlockEnabled = true;
                    simulateDeadlock();
                    break;
                case "highCpuUsage":
                    highCpuUsageEnabled = true;
                    break;
                case "resourceLeak":
                    resourceLeakEnabled = true;
                    resourceLeak = new ArrayList<>();
                    break;
                case "infiniteRecursion":
                    infiniteRecursionEnabled = true;
                    simulateInfiniteRecursion();
                    break;
                case "excessiveLogging":
                    excessiveLoggingEnabled = true;
                    break;
                default:
                    break;
            }
        }
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
            simulateIssues();
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

    // Simulate various issues based on command-line arguments
    private void simulateIssues() {
        if (memoryLeakEnabled) {
            simulateMemoryLeak();
        }
        if (highCpuUsageEnabled) {
            simulateHighCpuUsage();
        }
        if (resourceLeakEnabled) {
            simulateResourceLeak();
        }
        if (excessiveLoggingEnabled) {
            simulateExcessiveLogging();
        }
    }

    // Simulate a memory leak by continuously adding larger elements to the list
    private void simulateMemoryLeak() {
        for (int i = 0; i < 10; i++) {
            memoryLeak.add(new byte[10 * 1024 * 1024]); // Add 10MB byte arrays to the list
        }
    }

    // Simulate deadlock by creating two threads that hold locks on each other
    private void simulateDeadlock() {
        final Object resource1 = new Object();
        final Object resource2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");

                try { Thread.sleep(50); } catch (InterruptedException e) {}

                synchronized (resource2) {
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked resource 2");

                try { Thread.sleep(50); } catch (InterruptedException e) {}

                synchronized (resource1) {
                    System.out.println("Thread 2: Locked resource 1");
                }
            }
        });

        t1.start();
        t2.start();
    }
    // Simulate high CPU usage 
    private void simulateHighCpuUsage() { 
        Thread cpuThread = new Thread(() -> { 
            while (true) { 
                double value = Math.random() * Math.random(); 
            } 
        }); 
        cpuThread.start(); 
    }

    private void simulateResourceLeak() {
        Thread resourceLeakThread = new Thread(() -> {
            try {
                while (true) {
                    FileInputStream fis = new FileInputStream(new File("somefile.txt"));
                    resourceLeak.add(fis);  // Intentionally not closing the FileInputStream
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        resourceLeakThread.start();
    }

    // Simulate infinite recursion
    private void simulateInfiniteRecursion() {
        SwingUtilities.invokeLater(this::recursiveMethod);
    }

    private void recursiveMethod() {
        recursiveMethod();
    }

    // Simulate excessive logging
    private void simulateExcessiveLogging() {
        Thread loggingThread = new Thread(() -> {
            while (true) {
                logger.info("Logging excessively...");
            }
        });
        loggingThread.start();
    }
}

