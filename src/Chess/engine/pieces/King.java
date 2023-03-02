package Chess.engine.pieces;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Coordinate;
import Chess.engine.board.Move;
import Chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class King extends Piece
{
    private final int NUMBER_OF_OFFSETS=8;
    private final Coordinate possibleMoveOffset[]= new Coordinate[NUMBER_OF_OFFSETS];
    public King(final int pieceCoordinate, final Colour pieceColour)
    {
        super(PieceType.KING,pieceCoordinate,pieceColour);
        possibleMoveOffset[0] = new Coordinate(0,-1);
        possibleMoveOffset[1] = new Coordinate(-1,0);
        possibleMoveOffset[2] = new Coordinate(0,1);
        possibleMoveOffset[3] = new Coordinate(1,0);
        possibleMoveOffset[4] = new Coordinate(-1,-1);
        possibleMoveOffset[5] = new Coordinate(-1,1);
        possibleMoveOffset[6] = new Coordinate(-1,1);
        possibleMoveOffset[7] = new Coordinate(1,1);
    }
    @Override
    public String toString()
    {
        return PieceType.KING.toString();
    }
    @Override
    public Collection<Move> allLegalMoves(Board board)
    {
        final List<Move> legalMoves = new ArrayList<>();
        for(int i=0;i<NUMBER_OF_OFFSETS;i++)
        {
            Coordinate possibleDestinationCoordinate=Coordinate.sum(Coordinate.createCoordinate(this.pieceCoordinate),possibleMoveOffset[i]);
            final int possibleDestination = Coordinate.key(possibleDestinationCoordinate);
            if(isValidCoordinate(possibleDestinationCoordinate))
            {
                final Tile destinationTile = board.getTile(possibleDestination);
                if (!destinationTile.isOccupied())
                    legalMoves.add(new Move.MajorMove(board,this,possibleDestination,this.pieceCoordinate));
                else
                {
                    final Piece pieceOnDestination=destinationTile.getPiece();
                    if(this.pieceColour!=pieceOnDestination.getColour())
                        legalMoves.add(new Move.KillingMove(board,this,possibleDestination,this.pieceCoordinate,pieceOnDestination));
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public King movePiece(Move move)
    {
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getColour());
    }
}
