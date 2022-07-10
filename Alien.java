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

public void mover(){

}
public void move(int direction){
    if(moveLeft==true)
        setX(getX()-getSpeed());

    if(moveRight==true)
        setX(getX()+getSpeed());
}

public void draw(Graphics window){
    window.drawImage(getImage(), getX(), getY(), getWidth(), getHeight(), null)
}


