import javax.swing.JFrame;

public class Starter extends JFrame {

    public Starter()
    {
        // board
        add(new Board());
        setTitle("Space Invaders"); // the title of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new Starter();
    }
}