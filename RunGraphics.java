private Thread animator;

int xAxis = 30;
int yAxis = 30;
Ship s;
Alien [][] a = mew Alien [3][10];
Shot sh;

public showGraphics(Dimension dimension) {
    s = new Ship(200,500, 57, 35, 5, "player.png");
    sh = new Shot(200, 500, 5, 20, 15, "shoit.png");
    int x = 10;
    int y = 10;
    for(int r = 0; r<a.length; r++){
        for (int c = 0; c<a[0].length; c++){
            a[r][c] = new Alien(x, y, 30, 20, 5, "alien.png");
            x += 35;
        }
        x=10;
        y += 25;
    }
}

//that set the alien for a coordinate and spaced them out by 35 pixels
//need to add images

public void paintComponent(Graphics g){
    Graphics2D g2 = (Graphics2D) g;

    Dimesion d= getSize();
    //background

    g2.setColor(Color.white);
    g2.fillRect(o, o, d.width, d.height);

    move Alien();
    s.move(0);
    sh.move(0);
//draw ship to the screen and then hit detection
    sh.draw(g2);
    s.draw(g2);
    hitDetect();
}


//display aliens to screen

for(int r = 0; r<a.length; r++){
    for (int c=0; c<a[0].length; c++){
        if(a[r][c].isVis)
            a[r][c].draw(g2);
    }
}
//end of the paintcomponent

//go through each alien and see if it is visible

for(int r = 0; r<a.length; r++){
    for (int c = 0; c<a[0].length; c++){
        if (a[r][c].isVis == true && sh.getX() + sh.getWidth() >= a[r][c].getX()
        sh.getX() <= a[r][c].getX() + a[r][c].getWidth() &&
        sh.getY() + sh.getHeight() >= (a[r][c].getY()) &&
        sh.getY() <= a[r][c].getY() + a[r][c].getHHeight()) {
            a[r][c].isVis=false;
            sh.x = -30;
        }
    }
}
