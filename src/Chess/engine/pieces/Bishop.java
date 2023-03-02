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
public final class Bishop extends Piece
{
    private final int NUMBER_OF_OFFSETS=4;
    private final Coordinate possibleMoveOffsetVector[]= new Coordinate[NUMBER_OF_OFFSETS];
    public Bishop(final int pieceCoordinate, final Colour pieceColour)
    {
        super(PieceType.BISHOP,pieceCoordinate,pieceColour);
        possibleMoveOffsetVector[0] = new Coordinate(-1,-1);
        possibleMoveOffsetVector[1] = new Coordinate(-1,1);
        possibleMoveOffsetVector[2] = new Coordinate(-1,1);
        possibleMoveOffsetVector[3] = new Coordinate(1,1);
    }
    @Override
    public String toString()
    {
        return PieceType.BISHOP.toString();
    }
    @Override
    public Collection<Move> allLegalMoves(Board board)
    {
        final List<Move> legalMoves = new ArrayList<>();
        for(int i=0;i<NUMBER_OF_OFFSETS;i++)
        {
            Coordinate possibleDestinationCoordinate=Coordinate.sum(Coordinate.createCoordinate(this.pieceCoordinate),possibleMoveOffsetVector[i]);
            final int possibleDestination = Coordinate.key(possibleDestinationCoordinate);
            while(isValidCoordinate(possibleDestinationCoordinate))
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
                possibleDestinationCoordinate=Coordinate.sum(possibleDestinationCoordinate,possibleMoveOffsetVector[i]);
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Bishop movePiece(Move move)
    {
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getColour());
    }
}
