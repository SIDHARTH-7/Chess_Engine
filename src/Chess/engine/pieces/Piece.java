package Chess.engine.pieces;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Coordinate;
import Chess.engine.board.Move;
import Chess.engine.board.Utilities;
import java.util.Collection;
public abstract class Piece
{
    protected final PieceType pieceType;
    protected final int pieceCoordinate;
    protected final Colour pieceColour;
    private final int cachedHashCode;
    Piece(final PieceType pieceType,final int pieceCoordinate,final Colour pieceColour)
    {
        this.pieceType = pieceType;
        this.pieceCoordinate =pieceCoordinate;
        this.pieceColour = pieceColour;
        this.cachedHashCode = calculateHashCode();
    }

    private int calculateHashCode()
    {
        int result = pieceType.hashCode();
        result=31*result+pieceColour.hashCode();
        result=31*result+pieceCoordinate;
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if(this==object)
            return true;
        if(!(object instanceof Piece))
            return false;
        final Piece otherObject = (Piece) object;
        if(otherObject.pieceType==this.pieceType && otherObject.pieceColour == this.pieceColour
        && otherObject.pieceCoordinate == this.pieceCoordinate)
            return true;
        return false;
    }
    @Override
    public int hashCode()
    {
        return this.cachedHashCode;
    }
    public final Colour getColour()
    {return this.pieceColour;}
    public boolean isValidCoordinate(final Coordinate destinationCoordinate)
    {
            if(destinationCoordinate.x<0 || destinationCoordinate.x>7 || destinationCoordinate.y<0 ||  destinationCoordinate.y>7)
                return false;
            else
                return true;
    }
    public abstract Collection<Move> allLegalMoves(final Board board);

    public int getPieceCoordinate()
    {
        return this.pieceCoordinate;
    }

    public boolean isFirstMove() {
        return true;
    }

    public PieceType getPieceType()
    {
        return this.pieceType;
    }
    public abstract Piece movePiece(final Move move);
    public enum PieceType
    {
        PAWN("P")
                {
                    @Override
                    public boolean isKing() {return false;}
                },
        ROOK("R")
                {
                    @Override
                    public boolean isKing(){return false;}
                },
        BISHOP("B")
                {
                    @Override
                    public boolean isKing(){return false;}
                },
        QUEEN("Q")
                {
                    @Override
                    public boolean isKing(){return false;}
                },
        KNIGHT("N")
                {
                    @Override
                    public boolean isKing(){return false;}
                },
        KING("K")
                {
                    @Override
                    public boolean isKing(){return true;}
                };
        private String pieceName;
        PieceType(final String pieceName)
        {
            this.pieceName=pieceName;
        }
        public abstract boolean isKing();
        @Override
        public String toString()
        {
            return this.pieceName;
        }
    }
}
