package hackathon;

import javax.swing.*;
import java.awt.*;

public class JavaRuntimeInvaders extends JFrame {

    public JavaRuntimeInvaders() {
        initUI();
    }

    private void initUI() {
        add(new GamePanel());
        setTitle("Java Runtime Invaders");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JavaRuntimeInvaders ex = new JavaRuntimeInvaders();
            ex.setVisible(true);
        });
    }
}
