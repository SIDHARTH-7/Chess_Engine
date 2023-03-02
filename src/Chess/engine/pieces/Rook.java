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
public class Rook extends Piece
{
    private final int NUMBER_OF_OFFSETS=4;
    private final Coordinate possibleMoveOffsetVector[]= new Coordinate[NUMBER_OF_OFFSETS];
    public Rook(final int pieceCoordinate, final Colour pieceColour)
    {
        super(PieceType.ROOK,pieceCoordinate,pieceColour);
        possibleMoveOffsetVector[0] = new Coordinate(0,-1);
        possibleMoveOffsetVector[1] = new Coordinate(-1,0);
        possibleMoveOffsetVector[2] = new Coordinate(0,1);
        possibleMoveOffsetVector[3] = new Coordinate(1,0);
    }
    @Override
    public String toString()
    {
        return PieceType.ROOK.toString();
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
    public Rook movePiece(Move move)
    {
        return new Rook(move.getDestinationCoordinate(),move.getMovedPiece().getColour());
    }
}
