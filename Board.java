import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;


public class Board extends JPanel implements Runnable, MouseListener
{
  boolean ingame = true;
  private Dimension d;
  int BOARD_WIDTH=500;
  int BOARD_HEIGHT=500;
  int x = 0;
  BufferedImage img;
  String message = "Click Board to Start";
  private Thread animator;

  // entities
  Player p;
  Alien[] aliens = new Alien[10];
 
  public Board()
  {
    addKeyListener(new TAdapter());
    addMouseListener(this);
    setFocusable(true);
    d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);

    // entity generation
    p = new Player(BOARD_WIDTH/2, BOARD_HEIGHT-60, 5);

    int alienX = 10; // appears towards the top
    int alienY = 10; // appears towards the left

    for(int i = 0; i < aliens.length; i++)
    {
      aliens[i] = new Alien(alienX, alienY, 2);
      alienX += 40;

      if (i == 4) // splits in two rows
      {
        alienX = 10;
        alienY += 40;
      }
    }

    setBackground(Color.black);

    if (animator == null || !ingame) 
    {
      animator = new Thread(this);
      animator.start();
    }
                    
    setDoubleBuffered(true);
  }
    
  public void paint(Graphics g)
  {
    super.paint(g);
    
    g.setColor(Color.white);
    g.fillRect(0, 0, d.width, d.height);

    // player
    g.setColor(Color.red);
    g.fillRect(p.x, p.y, 20, 20);

    // player movement
    if(p.moveRight == true) 
    {
      p.x += p.speed;
    }
    
    if (p.moveLeft == true)
    { 
      p.x -= p.speed;
    }    

    // alien
    for(int i = 0; i < aliens.length; i++)
    {
      g.fillRect(aliens[i].x, aliens[i].y, 30, 30);
    }

    // alien movement
    moveAliens();

    // text
    Font small = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics metr = this.getFontMetrics(small);
    g.setColor(Color.black);
    g.setFont(small);
    g.drawString(message, 10, d.height-60);
    
    if (ingame) 
    {

    }
    
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }

  public void moveAliens() 
  {
    for(int i = 0; i < aliens.length; i++)
    {
      if(aliens[i].moveLeft == true)
      {
        aliens[i].x -= aliens[i].speed;
      }

      if(aliens[i].moveRight == true)
      {
        aliens[i].x += aliens[i].speed;
      }
    }

    for(int i = 0; i < aliens.length; i++)
    {
      if(aliens[i].x > BOARD_WIDTH) // if they go over the right screen boundary...
      {
        for(int z = 0; z < aliens.length; z++)
        {
          aliens[z].moveLeft = true; // the aliens move back left
          aliens[z].moveRight = false;

          aliens[z].y += 5; // and move down when it hits the border
        }
      }

      if(aliens[i].x < 0) // if they go over the left screen boundary...
      {
        for(int z = 0; z < aliens.length; z++)
        {
          aliens[z].moveRight = true; // the aliens move back right
          aliens[z].moveLeft = false;

          aliens[z].y += 5; // and move down when it hits the border
        }
      }
    }
  }

  private class TAdapter extends KeyAdapter 
  {
    public void keyReleased(KeyEvent e) 
    {
      int key = e.getKeyCode();
      p.moveRight = false;
      p.moveLeft = false;
    }
    
    public void keyPressed(KeyEvent e) 
    {
      // System.out.println( e.getKeyCode());
      // message = "Key Pressed: " + e.getKeyCode();

      int key = e.getKeyCode();

      if(key==39) // right arrow
      {
        p.moveRight = true; // moves right
      }
      if(key==37) // left arrow
      {
        p.moveLeft = true; // moves left
      }
    }
  }

  public void mousePressed(MouseEvent e) 
  {
    int x = e.getX();
    int y = e.getY();
  }

  public void mouseReleased(MouseEvent e) 
  {

  }

  public void mouseEntered(MouseEvent e) 
  {

  }

  public void mouseExited(MouseEvent e) 
  {

  }
  
  public void mouseClicked(MouseEvent e) 
  {

  }
  
  public void run() 
  {
    long beforeTime, timeDiff, sleep;
    
    beforeTime = System.currentTimeMillis();
    int animationDelay = 10;
    long time = System.currentTimeMillis();
    
    while (true) 
    {
      // infinite loop
      // spriteManager.update();
      repaint();
      try 
      {
        time += animationDelay;
        Thread.sleep(Math.max(0,time - System.currentTimeMillis()));
      }
      catch (InterruptedException e) 
      {
        System.out.println(e);
      } // catch
    } // while loop
  } // run
} // class