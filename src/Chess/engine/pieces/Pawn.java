package Chess.engine.pieces;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Coordinate;
import Chess.engine.board.Move;
import Chess.engine.board.Move.MajorMove;
import Chess.engine.board.Move.KillingMove;
import Chess.engine.board.Move.PawnJump;
import Chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class Pawn extends Piece
{
    private final int NUMBER_OF_OFFSETS=4;
    private final Coordinate possibleMoveOffset[]= new Coordinate[NUMBER_OF_OFFSETS];
    public Pawn(final int pieceCoordinate, final Colour pieceColour)
    {
        super(PieceType.PAWN,pieceCoordinate,pieceColour);
        if(this.pieceColour.isBlack())
        {
            possibleMoveOffset[0] = new Coordinate(0,1);
            possibleMoveOffset[1] = new Coordinate(0,2);
            possibleMoveOffset[2] = new Coordinate(-1,1);
            possibleMoveOffset[3] = new Coordinate(1,1);
        }
        else
        {
            possibleMoveOffset[0] = new Coordinate(0,-1);
            possibleMoveOffset[1] = new Coordinate(0,-2);
            possibleMoveOffset[2] = new Coordinate(-1,-1);
            possibleMoveOffset[3] = new Coordinate(1,-1);
        }
    }
    public boolean isBridgingTileOccupied(Board board)
    {
        final Coordinate bridgingTileCoordinate = Coordinate.sum(Coordinate.createCoordinate(this.pieceCoordinate),possibleMoveOffset[0]);
        final int bridgingTileLocation = Coordinate.key(bridgingTileCoordinate);
        final Tile bridgingTile = board.getTile(bridgingTileLocation);
        if(bridgingTile.isOccupied()) {
            System.out.println("1");
            return true;
        }
        else {
            System.out.println("0");
            return false;
        }
    }
    public boolean isPieceOnDestinationOpposite(final int destinationCoordinate,Board board)
    {
        final Tile destinationTile = board.getTile(destinationCoordinate);
        final Colour pieceOnDestinationColour = destinationTile.getPiece().pieceColour;
        if(pieceColour == pieceOnDestinationColour)
            return false;
        else
            return true;
    }
    @Override
    public String toString()
    {
        return PieceType.PAWN.toString();
    }
    @Override
    public Collection<Move> allLegalMoves(Board board)
    {
        final List<Move> legalMoves = new ArrayList<>();
        for(int i=0;i<NUMBER_OF_OFFSETS;i++)
        {
            final Coordinate possibleDestinationCoordinate=Coordinate.sum(Coordinate.createCoordinate(this.pieceCoordinate),possibleMoveOffset[i]);
            final int possibleDestination = Coordinate.key(possibleDestinationCoordinate);
            if (isValidCoordinate(possibleDestinationCoordinate)) {
                final Tile destinationTile = board.getTile(possibleDestination);
                if (i==0 && !destinationTile.isOccupied())
                    legalMoves.add(new MajorMove(board, this, possibleDestination, this.pieceCoordinate));//work required here----------
                else if(i==1 && this.isFirstMove() &&
                        (this.pieceCoordinate>7 && this.pieceCoordinate<16 && this.pieceColour.isWhite() ||
                        this.pieceCoordinate>47 && this.pieceCoordinate<56 && this.pieceColour.isBlack()) &&
                        (!destinationTile.isOccupied() && !isBridgingTileOccupied(board)))
                {
                    legalMoves.add(new PawnJump(board, this, possibleDestination, this.pieceCoordinate));
                }
                else if((i==2 || i==3) && destinationTile.isOccupied() && isPieceOnDestinationOpposite(possibleDestination,board))
                {
                    legalMoves.add(new KillingMove(board,this,possibleDestination,this.pieceCoordinate,destinationTile.getPiece()));
                }
            }
            // Code required here _________________________________________

        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Pawn movePiece(Move move)
    {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getColour());
    }
}
