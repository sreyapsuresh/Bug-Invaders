public class Alien extends Character 
{
    boolean moveRight;
    boolean moveLeft;
    boolean isVisible;

    public Alien(int x, int y, int s)
    {
        super(x,y,s);

        moveLeft = false;
        moveRight = true; // starts off moving right
        isVisible = true;
    }
}
