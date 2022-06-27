import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame  {
    int BOARD_WIDTH = 500;
    int BOARD_HEIGHT = 500;

    public SpaceInvaders() {

        initUI();
    }

    private void initUI() {

        add(new Main());

        setTitle("Space Invaders");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
