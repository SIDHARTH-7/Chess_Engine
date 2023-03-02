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

public class WhitePlayer extends Player{
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves)
    {
        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }
    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getWhiteActivePieces();
    }

    @Override
    public Colour getColour() {
        return Colour.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateCastleMoves(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves)
    {
        final Collection<Move> kingCastles = new ArrayList<>();
        if(this.king.isFirstMove() && !this.isInCheck())
        {
            if(!this.board.areBridgingTilesOccupied(61,62) &&
                    !isAttackOnTilePossible(61,62,this.getOpponent().getLegalMoves()) &&
                    this.board.getTile(63).getPiece().getPieceType() == ROOK)
            {
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new Move.KingSideCastleMove(this.board,this.king,62,60,(Rook)rookTile.getPiece(),63,61));
            }
            if(!this.board.areBridgingTilesOccupied(59,57) &&
                    !isAttackOnTilePossible(59,57,this.getOpponent().getLegalMoves()) &&
                    this.board.getTile(56).getPiece().getPieceType() == ROOK)
            {
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.king,58,60,(Rook)rookTile.getPiece(),56,59));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
