import java.awt.*;  
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.*;

/*
*  GAME CONTROLS:
* 
*  Player 1 controls Agent Zero, and moves using the <- & -> keys. Use Up to shoot.
*  Player 2 controls Agent One, and moves using the A & D keys. Use W to shoot.
* 
*/

public class RunGraphics 
{
    private JFrame frame;
    int fW = 600;
    int fH = 600;

    // Window set-up
    public RunGraphics() 
    {
        frame = new JFrame("Bug Invaders");
        frame.setSize(fW, fH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new showGraphics(frame.getSize()));
        frame.pack();
        frame.setVisible(true); 
    }

    // Main method
    public static void main(String[] args) 
    {
        // Music player
        MusicPlayer.RunMusic("bugDestroyers.wav");
        new RunGraphics();
    }
    public static class showGraphics extends JPanel implements Runnable, MouseListener, KeyListener 
    {
        private Thread animator;

        // Initialization
        int xAxis;
        int yAxis;
        Boolean gameOn = false;
        Boolean controls = false;
        Boolean gameOver = false;

        String emotionZero = "Neutral"; // Expression of Agent Zero
        String emotionOne = "Neutral"; // Agent One
        
        // Ship entities
        Ship agentZero;
        Ship agentOne;

        // Shot entities
        Shot zeroShot;
        Shot oneShot;
        
        // Aliens
        Alien[][] a = new Alien[3][10];

        // Scoring
        int zeroPts;
        int onePts;

        public showGraphics(Dimension dimension) 
        {
            gameSetup();

            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener(this) ; 
            setFocusable(true);
            if (animator == null) 
            {
                animator = new Thread(this);
                animator.start();
            }
            setDoubleBuffered(true);
        }

        public void gameSetup() // Sets up entities
        {
            // Resets variables 
            yAxis = 30;
            xAxis = 30;
            zeroPts = 0;
            onePts = 0;
            gameOn = false;
            
            // Ships
            agentZero = new Ship(100,500,32,32,5,"zeroShip");
            agentOne = new Ship(300,500,32,32,5,"oneShip");                                                             
            
            // Shots
            zeroShot = new Shot(100,500,5,20,15,"zeroShot");
            oneShot = new Shot(300,500,5,20,15,"oneShot");                                                          

            // Alien
            a = new Alien[3][10];

            int x = 10;
            int y = 70;

            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    int num = c%5;
                    switch (num) 
                    {
                        case 0:
                            a[r][c] = new Alien(x,y,25,25,5, "Aliens/alienOne");
                            break;

                        case 1:
                            a[r][c] = new Alien(x,y,25,25,5, "Aliens/alienTwo");
                            break;

                        case 2:
                            a[r][c] = new Alien(x,y,25,25,5, "Aliens/alienThree"); 
                            break;

                        case 3:
                            a[r][c] = new Alien(x,y,25,25,5, "Aliens/alienFour");
                            break;

                        case 4:
                            a[r][c] = new Alien(x,y,25,25,5, "Aliens/alienFive");
                            break;
                        
                        default:
                            break;
                    }

                    x += 35;
                }

                x = 10;
                y += 30;
            }      
        }

        public void paintComponent(Graphics g) 
        {
            // Different "layers" of the game
            Graphics2D gBackground = (Graphics2D) g; // Background color
            Graphics2D gScore = (Graphics2D) g; // Score text
            Graphics2D gSprite = (Graphics2D) g; // Sprites
            Graphics2D gMenu = (Graphics2D) g; // Menu background

            // Needed to draw things
            Dimension d = getSize();

            // Draws background
            gBackground.setColor(Color.black);
            gBackground.fillRect(0, 0, d.width, d.height);

            // If game has started, entities can start moving
            if (gameOn == true)
            {
                // Draw sprites
                zeroShot.draw(gSprite);
                agentZero.draw(gSprite);

                oneShot.draw(gSprite);
                agentOne.draw(gSprite);

                for(int r = 0; r<a.length; r++)
                {
                    for (int c = 0; c<a[0].length; c++)
                    {
                        if(a[r][c].isVis)
                        {
                            a[r][c].draw(gSprite);
                        }
                    }
                }
            
                // Entity movement
                moveAlien();

                agentZero.move(0);
                zeroShot.move(0);

                agentOne.move(0);
                oneShot.move(0);

                oneSprite(gSprite);
                // zeroSprite(gSprite);

                updateScore(g, gScore);
            } 
            else
            {
                startScreen(g, gMenu);
            }

            hitDetect();
        }

        // Agent Zero Sprite
        public void zeroSprite(Graphics g)
        {
            Toolkit t = Toolkit.getDefaultToolkit();
            Image zeroSpr = t.getImage("zero"+ emotionZero + ".png");
            g.drawImage(zeroSpr, 500, 0,this);
        }

        // Agent One Sprite
        public void oneSprite(Graphics g) 
        {
            Toolkit t = Toolkit.getDefaultToolkit();  
            Image oneSpr = t.getImage("one"+ emotionOne + ".png");  
            g.drawImage(oneSpr, 5, 5,this);
        }

        // Menu screen
        public void startScreen(Graphics g, Graphics2D g2) {
            g2.setColor(Color.white);
            if (!controls)
            {
                if (gameOver)
                {
                    g.drawString("GAME OVER", 250, 200);
                    if (zeroPts < onePts)
                    {
                        g.drawString("Player 2 WINS!", 245, 250);
                    }
                    else if (zeroPts > onePts)
                    {
                        g.drawString("Player 1 WINS!", 245, 250);
                    }
                    else
                    {
                        g.drawString("IT'S A TIE!", 255, 250);
                    }
                    
                    g.drawString("Press ENTER to play again", 215, 350);
                }
                else
                {
                    g.drawString("BUG INVADERS", 250, 200);
                    g.drawString("Press ENTER to start", 235, 350);
                    g.drawString("Press C for controls", 235, 400);
                }
            }
            else if (controls)
            {
                g.drawString("Player 1 controls Agent Zero, and moves using the A & D keys. Use W to shoot.",
                30, 200);
                g.drawString("Player 2 controls Agent One, and moves using the Left & Right arrow keys. Use Up to shoot.", 
                10, 250);

                g.drawString("Press ENTER to start", 240, 350);
            }
        }

        // Update score
        public void updateScore(Graphics g, Graphics2D g2) 
        {
            g2.setColor(Color.white);
            g.drawString("P1 Score: " + zeroPts, 500, 25);
            g.drawString("P2 Score: " + onePts, 500, 75);
        }

        // Hit detection
        public void hitDetect()
        {
            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    if (a[r][c].isVis == true && zeroShot.getX() + zeroShot.getWidth() >= a[r][c].getX() && 
                    zeroShot.getX() <= a[r][c].getX() + a[r][c].getWidth() && 
                    zeroShot.getY() + zeroShot.getHeight() >= (a[r][c].getY()) && 
                    zeroShot.getY() <= a[r][c].getY() + a[r][c].getHeight()) 
                    {
                        a[r][c].isVis=false; 
                        
                        zeroShot.x = -30;
                        zeroPts++;
                        emotionZero = "Smile";

                        updateScore(null, null);
                    }

                    if (a[r][c].isVis == true && 
                    oneShot.getX() + oneShot.getWidth() >= a[r][c].getX() && 
                    oneShot.getX() <= a[r][c].getX() + a[r][c].getWidth() && 
                    oneShot.getY() + oneShot.getHeight() >= (a[r][c].getY()) && 
                    oneShot.getY() <= a[r][c].getY() + a[r][c].getHeight()) 
                    {
                        a[r][c].isVis=false; 

                        oneShot.x = -30;
                        onePts++;
                        emotionOne = "Smile"; 

                        updateScore(null, null);
                    } 
                }
            }
        }

        public void moveAlien()
        {
            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    if(a[r][c].moveLeft)
                    {
                        a[r][c].setX(a[r][c].getX()-a[r][c].getSpeed());
                    }

                    if(a[r][c].moveRight)
                    {
                        a[r][c].setX(a[r][c].getX()+a[r][c].getSpeed());
                    }
                }
            }

            // Check if we need to switch directions
            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    if(a[r][c].getX() > 600)
                    {
                        moveLeftRight(1);
                        break;
                    }

                    if(a[r][c].getX() < 0)
                    {
                        moveLeftRight(2);
                        break;
                    }
                }
            }
        }

        public void moveLeftRight(int d)
        {
            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    if(d==1)
                    {
                        a[r][c].moveLeft = true;
                        a[r][c].moveRight = false;
                    }
                    else
                    {
                        a[r][c].moveLeft = false;
                        a[r][c].moveRight = true;
                    }

                    a[r][c].setY(a[r][c].getY()+10);

                    if(a[r][c].getY() > 450 || zeroPts + onePts == 30) // The bottom of the screen
                    {
                        gameOn = false;
                        controls = false;
                        gameOver = true;
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e){}

        public void mouseReleased(MouseEvent e){}

        public void mouseEntered(MouseEvent e){}

        public void mouseExited(MouseEvent e){}

        public void mouseClicked(MouseEvent e){}

        public void keyTyped ( KeyEvent e ){}  

        public void keyPressed ( KeyEvent e)
        {  
            int k = e.getKeyCode();

            agentZero.setLeftRight(k,65, 68); // A and D
            agentOne.setLeftRight(k, 37, 39); // <- and ->

            switch (k) 
            {
                case 10: // Return or Enter
                    gameSetup();
                    gameOn = true;
                    break;

                case 87: // W
                    zeroShot.goUp=true;
                    zeroShot.setX(agentZero.getX() + (agentZero.getWidth()/2));
                    zeroShot.setY(agentZero.getY());
                    break;

                case 38: // Up
                    oneShot.goUp=true;
                    oneShot.setX(agentOne.getX() + (agentOne.getWidth()/2));
                    oneShot.setY(agentOne.getY());
                    break;

                case 67: // C
                    controls = true;
                    break;

                case 75: // K
                    controls = false;
                    break;

                default:
                    break;
            }
        }  

        public void keyReleased ( KeyEvent e )
        {  
            agentZero.stop();
            agentOne.stop();
        } 

        public void run() 
        {
            int animationDelay = 25;
            long time = System.currentTimeMillis();

            while (true) // Infinite loop
            {
                repaint();
                try 
                {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } 
                catch (InterruptedException e) 
                {
                    System.out.println(e);
                }   
            }   
        }
    }
}