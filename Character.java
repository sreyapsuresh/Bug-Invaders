public class Character 
{
    int x;
    int y;
    int speed;

    public Character()
    {
        // default constructor
    } 
    
    public Character(int x, int y, int speed)
    { 
        this.x = x;
        this.y = y;
        this.speed = speed;
    }
}

abstract class Character {

    int x;
    int y;
    int w;
    int h;
    private int speed;
    private Image image;
    boolean moveLeft = false;
    boolean moveRight = false;
}

public Character {
    x = 0;
    y = 0;
}

public Character(int x, int y, int w, int h, int s, String U)
{
    this.x = x;
    this,y = y;
    this. w = w;
    this.h = h;

    speed=s;
}
try

{
    URL url = getClass().getResource(u);
    image = ImageIO.read(url);
}

public abstact void move(int direction);

public abstract void draw(Graphics window);

public String toString()

{
    return getX() + "" + getY() + "" + getWidth() + "" + getHeight() 
}

public void setPos( int x, int y)
{
    x = getX();
    y = getY();
}

public void setX(int x)
{
    this.x = x;
}

public void setY(int y)
{
    this.y = y;
}

public int getX()
{
    return x;
}

public void setWidth(int w)
{
    this. w = w;
}

public void setHeight(int h)
{
    this.h = h;
}

public int getWidth()
{
    return w;
}

public int getHeight()
{
    return h;
}

public int getSpeed(){
    return speed;
}

public Image getImage(){
    return Image;
}