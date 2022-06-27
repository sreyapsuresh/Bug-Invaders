import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

public class Agent extends Sprite {

    private int width;
    int BOARD_HEIGHT = 500;
    int BOARD_WIDTH = 500;

    public Agent() {

        initPlayer();
    }

    private void initPlayer() {

        var playerImg = "Sprites/AgentOne/AgentOne_Shock.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = 270;
        setX(START_X);

        int START_Y = 280;
        setY(START_Y);
    }

    public void act() {

        x += dx;

        if (x <= 2) {

            x = 2;
        }

        if (x >= BOARD_WIDTH - 2 * width) {

            x = BOARD_WIDTH - 2 * width;
        }
    }

    
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            System.out.print("left");
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {

            System.out.print("rgth");
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}