package Chess.engine.player;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Move;
import Chess.engine.pieces.King;
import Chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public abstract class Player
{
    protected final Board board;
    protected final King king;
    protected final Collection<Move> nextLegalMove;
    protected boolean isInCheck;
    Player(final Board board,final Collection<Move> nextLegalMove,final Collection<Move> opponentNextLegalMove)
    {
        this.board = board;
        this.king = establishKing();
        this.nextLegalMove = ImmutableList.copyOf(Iterables.concat(nextLegalMove,calculateCastleMoves(nextLegalMove,opponentNextLegalMove)));
        this.isInCheck = !Player.attacksOnTile(this.king.getPieceCoordinate(),opponentNextLegalMove).isEmpty();
    }
    protected static Collection<Move> attacksOnTile(int pieceCoordinate, Collection<Move> opponentNextLegalMove)
    {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move:opponentNextLegalMove)
        {
            if(pieceCoordinate == move.getDestinationCoordinate())
                attackMoves.add(move);
        }
        return ImmutableList.copyOf(attackMoves);
    }
    protected static boolean isAttackOnTilePossible(int tileCoordinate1,int tileCoordinate2,Collection<Move> opponentNextLegalMove)
    {
        if(tileCoordinate1<=tileCoordinate2)
        {
            for(int i=tileCoordinate1;i<=tileCoordinate2;i++)
            {
                if(!attacksOnTile(i,opponentNextLegalMove).isEmpty())
                    return true;
            }
        }
        else
        {
            for(int i=tileCoordinate1;i>=tileCoordinate2;i--)
            {
                if(!attacksOnTile(i,opponentNextLegalMove).isEmpty())
                    return true;
            }
        }
        return false;
    }
    private King establishKing()
    {
        for(final Piece piece : getActivePieces())
        {
            if(piece.getPieceType().isKing())
                return  (King) piece;
        }
        throw new RuntimeException("This Board is Invalid!");
    }
    public boolean isMoveLegal(final Move move)
    {
        return this.nextLegalMove.contains(move);
    }

    public boolean isInCheck()
    {
        return this.isInCheck;
    }
    public boolean isInCheckMate()
    {
        return this.isInCheck && hasEscapeMoves();
    }

    private boolean hasEscapeMoves()
    {
        for(final Move move: this.nextLegalMove)
        {
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone())
                return true;
        }
        return false;
    }

    public boolean isInStaleMate()
    {
        return !this.isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled()
    {
        return false;
    }
    public MoveTransition makeMove(final Move move)
    {
        if(!move.isLegalMove())
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.attacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPieceCoordinate(),
                transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty())
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }
     public Collection<Move> getLegalMoves()
    {
        return this.nextLegalMove;
    }
    private Piece getPlayerKing()
    {
        return this.king;
    }
    public abstract Collection<Piece> getActivePieces();
    public abstract Colour getColour();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateCastleMoves(Collection<Move> playerLegalMoves,Collection<Move> opponentLegalMoves);
}
