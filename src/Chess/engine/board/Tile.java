package Chess.engine.board;
import Chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
public abstract class Tile
{
    protected final int tileCoordinate;
    private static final Map<Integer,EmptyTile> EmptyBoard=createEmptyBoard();

    public static Tile createTile(int tileCoordinate,Piece piece)
    {
        if(piece == null)
            return new EmptyTile(tileCoordinate);
        else
            return new OccupiedTile(tileCoordinate,piece);
    }
    private Tile(final int tileCoordinate)
    {
        this.tileCoordinate=tileCoordinate;
    }
    private static Map<Integer, EmptyTile> createEmptyBoard()
{
    final Map<Integer,EmptyTile> EmptyMap=new HashMap<>();
    for(int i=0;i<Utilities.numberOfTiles;i++)
    {
        EmptyMap.put(i, new EmptyTile(i));
    }
    return ImmutableMap.copyOf(EmptyMap);
}
    public abstract boolean isOccupied();
    public abstract Piece getPiece();

    public int getCoordinate()
    {
        return this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile
    {
        EmptyTile(final int tileCoordinate)
        {
            super(tileCoordinate);
        }
        @Override
        public boolean isOccupied()
        {
            return false;
        }
        @Override
        public Piece getPiece()
        {
            return null;
        }
        @Override
        public String toString()
        {
            return "-";
        }
    }
    public static final class OccupiedTile extends Tile
    {
        int tileCoordinate;
        private final Piece pieceOnTile;
        OccupiedTile(final int tileCoordinate,final Piece piece)
        {
            super(tileCoordinate);
            this.pieceOnTile =piece;
        }
        @Override
        public boolean isOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
        @Override
        public String toString()
        {
            return getPiece().getColour().isBlack()?
                    this.pieceOnTile.toString().toLowerCase():
                    this.pieceOnTile.toString();
        }
    }
}

