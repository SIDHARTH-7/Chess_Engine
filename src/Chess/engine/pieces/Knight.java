package Chess.engine.pieces;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Coordinate;
import Chess.engine.board.Move;
import Chess.engine.board.Move.*;
import Chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public final class Knight extends Piece
{
    final int NUMBER_OF_OFFSETS=8;
    private final Coordinate possibleMoveOffset[]= new Coordinate[NUMBER_OF_OFFSETS];
    public Knight(final int pieceCoordinate, final Colour pieceColour)
    {
        super(PieceType.KNIGHT,pieceCoordinate,pieceColour);
        possibleMoveOffset[0] = new Coordinate(-2,-1);
        possibleMoveOffset[1] = new Coordinate(-1,-2);
        possibleMoveOffset[2] = new Coordinate(-2,1);
        possibleMoveOffset[3] = new Coordinate(-1,2);
        possibleMoveOffset[4] = new Coordinate(1,2);
        possibleMoveOffset[5] = new Coordinate(2,1);
        possibleMoveOffset[6] = new Coordinate(1,-2);
        possibleMoveOffset[7] = new Coordinate(2,-1);
    }
    @Override
    public String toString()
    {
        return PieceType.KNIGHT.toString();
    }
    @Override
    public Collection<Move> allLegalMoves(Board board)
    {
        final List<Move> legalMoves= new ArrayList<>();
        for(int i=0;i<8;i++)
        {
            final Coordinate possibleDestinationCoordinate=Coordinate.sum(Coordinate.createCoordinate(this.pieceCoordinate),possibleMoveOffset[i]);
            final int possibleDestination = Coordinate.key(possibleDestinationCoordinate);
            if(isValidCoordinate(possibleDestinationCoordinate))
            {
                final Tile destinationTile = board.getTile(possibleDestination);
                if (!destinationTile.isOccupied())
                    legalMoves.add(new MajorMove(board,this,possibleDestination,this.pieceCoordinate));
                else
                {
                    final Piece pieceOnDestination=destinationTile.getPiece();
                    if(this.pieceColour!=pieceOnDestination.pieceColour)
                        legalMoves.add(new KillingMove(board,this,possibleDestination,this.pieceCoordinate,pieceOnDestination));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Knight movePiece(Move move)
    {
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getColour());
    }
}
