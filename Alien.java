public class Alien extends Character 
{
    boolean moveRight;
    boolean moveLeft;
    boolean isVisible; // when hit, they are no longer visible

    public Alien(int x, int y, int s)
    {
        super(x,y,s);

        moveLeft = false;
        moveRight = true; // starts the game off by moving
        isVisible = true;
    }
}
