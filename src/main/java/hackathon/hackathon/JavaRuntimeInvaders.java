package hackathon;

import javax.swing.*;

public class JavaRuntimeInvaders extends JFrame {

    public JavaRuntimeInvaders(String[] args) {
        initUI(args);
    }

    private void initUI(String[] args) {
        GamePanel gamePanel = new GamePanel(args);
        add(gamePanel);

        setTitle("Java Runtime Invaders");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JavaRuntimeInvaders ex = new JavaRuntimeInvaders(args);
            ex.setVisible(true);
        });
    }
}
