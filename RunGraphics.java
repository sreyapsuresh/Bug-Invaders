import java.awt.*;  
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.sound.sampled.*;

/*
*  GAME CONTROLS:
* 
*  Player 1 controls Agent Zero, and moves using the A & D keys. Use W to shoot.
*  Player 2 controls Agent One, and moves using the <- & -> keys. Use Spacebar to shoot.
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
        frame.add(new showGraphics(frame.getSize())); // Setting up the DrawBars public class function (getting bars and putting it in this frame)
        frame.pack();
        frame.setVisible(true); 
    }

    // Main method
    public static void main(String[] args) 
    {
        new RunGraphics();

        // Music player
        MusicPlayer.RunMusic("bugDestroyers.wav");
    }
    public static class showGraphics extends JPanel implements Runnable, MouseListener, KeyListener 
    {
        private Thread animator;

        // Initialization
        int xAxis;
        int yAxis;
        Boolean gameOn = false;

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
            agentOne = new Ship(100,500,57,35,5,"player.png");                                                             // **CHANGE SPRITE**
            agentZero = new Ship(300,500,57,35,5,"player.png");
            
            // Shots
            oneShot = new Shot(100,500,5,20,15,"shot.png");
            zeroShot = new Shot(300,500,5,20,15,"shot.png");                                                               // **CHANGE SPRITE**

            // Alien
            a = new Alien[3][10];

            int x = 10;
            int y = 70;

            for(int r = 0; r<a.length; r++)
            {
                for (int c = 0; c<a[0].length; c++)
                {
                    a[r][c] = new Alien(x,y,30,20,5,"alien.png");
                    x += 35;
                }
                x=10;
                y += 25;
            }      
        }

        public void paintComponent(Graphics g) 
        {
            // Different "layers" of the game
            Graphics2D gBackground = (Graphics2D) g; // Background color
            Graphics2D gScore = (Graphics2D) g; // Score text
            Graphics2D gSprite = (Graphics2D) g; // Sprites
            Graphics2D gMenu = (Graphics2D) g; // Menu background
            Graphics2D gFont = (Graphics2D) g; // Game font

            // Needed to draw things
            Dimension d = getSize();

            // Draws background
            gBackground.setColor(Color.black);
            gBackground.fillRect(0, 0, d.width, d.height);

            // If game has started, entities can start moving
            if (gameOn == true)
            {
                moveAlien();

                agentZero.move(0);
                zeroShot.move(0);

                agentOne.move(0);
                oneShot.move(0);

                oneSprite(gSprite);
                updateScore(g, gScore);
            } 
            else
            {
                startScreen(g, gMenu);
            }

            // Draw sprites
            zeroShot.draw(gSprite);
            agentZero.draw(gSprite);

            oneShot.draw(gSprite);
            agentOne.draw(gSprite);

            hitDetect();

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
            g.drawImage(oneSpr, 0, 0,this);
        }

        // Menu screen
        public void startScreen(Graphics g, Graphics2D g2) {
            g2.setColor(Color.white);
            g.drawString("Press S to start", 10, 200);
        }

        // Update score
        public void updateScore(Graphics g, Graphics2D g2) {
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
                    if(a[r][c].getX()>600)
                    {
                        moveLeftRight(1);
                        break;
                    }

                    if(a[r][c].getX()<0)
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
                        a[r][c].moveLeft=true;
                        a[r][c].moveRight=false;
                    }
                    else
                    {
                        a[r][c].moveLeft=false;
                        a[r][c].moveRight=true;
                    }

                    a[r][c].setY(a[r][c].getY()+10);

                    if(a[r][c].getY() > 500) // The bottom of the screen
                    {
                        gameOn = false;
                    }

                }
            }
        }

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void keyTyped ( KeyEvent e ){  

        }  

        public void keyPressed ( KeyEvent e){  
            int k = e.getKeyCode();

            agentZero.setLeftRight(k,37, 39); // <- and ->
            agentOne.setLeftRight(k, 65, 68); // A and D

            if(k == 83) // S
            {
                gameSetup();
                gameOn = true;
            }

            if(k == 32) // Spacebar
            {
                zeroShot.goUp=true;
                zeroShot.setX(agentZero.getX() + (agentZero.getWidth()/2));
                zeroShot.setY(agentZero.getY() );
            }
            
            if(k == 86) // V
            {
                oneShot.goUp=true;
                oneShot.setX(agentOne.getX() + (agentOne.getWidth()/2));
                oneShot.setY(agentOne.getY() );
            }
        }  

        public void keyReleased ( KeyEvent e ){  
            int k = e.getKeyCode();

            agentZero.stop();
            agentOne.stop();
        } 

        public void run() 
        {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 50;
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
                }   // End catch
            }   // End while loop
        }   // End of run
    }
}