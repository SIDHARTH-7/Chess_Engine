package Chess.engine.board;

public class Coordinate
{
    public int x;
    public int y;
    public Coordinate(final int x, final int y)
    {
        this.x=x;
        this.y=y;
    }
    public static Coordinate createCoordinate(int tileCoordinate)
    {
        return new Coordinate(tileCoordinate%8,tileCoordinate/8);
    }
    public static Coordinate sum(final Coordinate x,final Coordinate y)
    {
        return new Coordinate(x.x+y.x,x.y+y.y);
    }
    public static int key(Coordinate coordinate)
    {
        return (coordinate.y*8)+coordinate.x;
    }
}
