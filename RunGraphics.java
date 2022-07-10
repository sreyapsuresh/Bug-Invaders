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