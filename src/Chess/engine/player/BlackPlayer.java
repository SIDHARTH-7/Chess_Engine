package Chess.engine.player;
import Chess.engine.Colour;
import Chess.engine.board.Board;
import Chess.engine.board.Move;
import Chess.engine.board.Tile;
import Chess.engine.pieces.Piece;
import Chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import static Chess.engine.pieces.Piece.PieceType.ROOK;
public class BlackPlayer extends Player
{
    public BlackPlayer(Board board, Collection<Move> blackStandardLegalMoves, Collection<Move> whiteStandardLegalMoves)
    {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }
    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getBlackActivePieces();
    }

    @Override
    public Colour getColour() {
        return Colour.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateCastleMoves(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves)
    {
        final Collection<Move> kingCastles = new ArrayList<>();
        if(this.king.isFirstMove() && !this.isInCheck())
        {
            if(!this.board.areBridgingTilesOccupied(5,6) &&
                    !isAttackOnTilePossible(5,6,this.getOpponent().getLegalMoves()) &&
                    this.board.getTile(7).getPiece().getPieceType() == ROOK)
            {
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new Move.KingSideCastleMove(this.board,this.king,6,4,(Rook)rookTile.getPiece(),7,5));
            }
            if(!this.board.areBridgingTilesOccupied(1,3) &&
                    !isAttackOnTilePossible(1,3,this.getOpponent().getLegalMoves()) &&
                    this.board.getTile(0).getPiece().getPieceType() == ROOK)
            {
                final Tile rookTile = this.board.getTile(0  );
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.king,2,4,(Rook)rookTile.getPiece(),0,3));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
