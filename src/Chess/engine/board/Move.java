package Chess.engine.board;
import Chess.engine.board.Board.Builder;
import Chess.engine.pieces.Pawn;
import Chess.engine.pieces.Piece;
import Chess.engine.pieces.Rook;

public abstract class Move {
    final Board board;
    final Piece pieceMoved;
    final int destinationCoordinate;
    final int sourceCoordinate;
    public static final Move NULL_MOVE = new NullMove();
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result=prime*result + this.destinationCoordinate;
        result=prime*result + this.pieceMoved.getPieceCoordinate();
        return result;
    }
    @Override
    public boolean equals(final Object other)
    {
        if(this==other)
            return true;
        if(!(other instanceof Move))
            return false;
        final Move otherMove=(Move)other;
        return  this.pieceMoved.equals(otherMove.pieceMoved) &&
                this.destinationCoordinate == otherMove.getDestinationCoordinate() &&
                this.sourceCoordinate == otherMove.getSourceCoordinate();
    }
    public boolean isAttack()
    {
        return false;
    }
    public Piece getKilledPiece()
    {
        return null;
    }
    public boolean isCastleMove()
    {
        return false;
    }
    public int getSourceCoordinate()
    {
        return this.sourceCoordinate;
    }
    private Move(final Board board, final Piece pieceMoved, final int destinationCoordinate, final int sourceCoordinate)
    {
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.destinationCoordinate = destinationCoordinate;
        this.sourceCoordinate = sourceCoordinate;
    }

    public int getDestinationCoordinate()
    {
        return this.destinationCoordinate;
    }

    public boolean isLegalMove()
    {
        for(final Move move : this.pieceMoved.allLegalMoves(this.board))
        {
            if(this.destinationCoordinate == move.getDestinationCoordinate())
                return true;
        }
        return false;
    }
    public Board execute()
    {
        final Builder builder = new Builder();
        for(final Piece piece:this.board.currentPlayer().getActivePieces())
        {
            if(!this.pieceMoved.equals(piece))
                builder.putBoardConfiguration(piece);
        }
        for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
        {
            builder.putBoardConfiguration(piece);
        }
        final Piece movedPiece=(Piece)this.pieceMoved.movePiece(this);
        builder.putBoardConfiguration(movedPiece);
        builder.nextMoveColour(this.board.currentPlayer().getColour());
        return builder.build();
    }

    public Piece getMovedPiece()
    {
        return this.pieceMoved;
    }

    public static class MajorMove extends Move
    {
        public MajorMove(final Board board, final Piece pieceMoved, final int destinationCoordinate, final int sourceCoordinate) {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate);
        }
    }
    public static class KillingMove extends Move
    {
        final Piece killedPiece;
        public KillingMove(final Board board,final Piece pieceMoved,final int destinationCoordinate,final int sourceCoordinate,final Piece killedPiece)
        {
            super(board,pieceMoved,destinationCoordinate,sourceCoordinate);
            this.killedPiece=killedPiece;
        }
        @Override
        public int hashCode()
        {
            return this.killedPiece.hashCode()+super.hashCode();
        }
        @Override
        public boolean equals(final Object other)
        {
            if(this==other)
                return true;
            if(!(other instanceof KillingMove))
                return false;
            final KillingMove otherKillingMove=(KillingMove) other;
            return  super.equals(otherKillingMove) && getKilledPiece().equals(otherKillingMove.getKilledPiece());
        }
        @Override
        public boolean isAttack()
        {
            return true;
        }
        @Override
        public Piece getKilledPiece()
        {
            return this.killedPiece;
        }
    }
    public static class PawnMove extends Move
    {
        public PawnMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate);
        }
    }
    public static class PawnKillingMove extends KillingMove
    {
        public PawnKillingMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate, Piece killedPiece)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate, killedPiece);
        }
    }
    public static final class PawnEnPassantKillingMove extends PawnKillingMove
    {
        public PawnEnPassantKillingMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate, Piece killedPiece)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate, killedPiece);
        }
    }
    public static final class PawnJump extends Move
    {
        public  PawnJump(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate);
        }
        @Override
        public Board execute()
        {
            final Builder builder = new Builder();
            for(final Piece piece:this.board.currentPlayer().getActivePieces())
            {
                if(!(pieceMoved.equals(piece)))
                    builder.putBoardConfiguration(piece);
            }
            for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
            {
                builder.putBoardConfiguration(piece);
            }
            final Pawn movedPawn=(Pawn)this.pieceMoved.movePiece(this);
            builder.putBoardConfiguration(movedPawn);
            builder.putEnPassantPiece(movedPawn);
            builder.nextMoveColour(this.board.currentPlayer().getOpponent().getColour());
            return builder.build();
        }
    }
    static abstract class CastleMove extends Move
    {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        public CastleMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate,
                          Rook castleRook,int castleRookStart,int castleRookDestination)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate);
            this.castleRook=castleRook;
            this.castleRookStart=castleRookStart;
            this.castleRookDestination=castleRookDestination;
        }
        public Rook getCastleRook()
        {
            return this.castleRook;
        }

        @Override
        public Board execute()
        {
            final Builder builder = new Builder();
            for(final Piece piece:this.board.currentPlayer().getActivePieces())
            {
                if(!(pieceMoved.equals(piece)) || !(this.castleRook.equals(piece)))
                    builder.putBoardConfiguration(piece);
            }
            for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
            {
                builder.putBoardConfiguration(piece);
            }
            builder.putBoardConfiguration(this.pieceMoved.movePiece(this));
            builder.putBoardConfiguration(new Rook(this.castleRookDestination,this.castleRook.getColour()));
            builder.nextMoveColour(this.board.currentPlayer().getOpponent().getColour());
            return builder.build();
        }
        @Override
        public boolean isCastleMove()
        {
            return true;
        }
    }
    public static final class KingSideCastleMove extends CastleMove
    {
        public KingSideCastleMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate,
                                  Rook castleRook,int castleRookStart,int castleRookDestination)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString()
        {
            return "0-0";
        }
    }
    public static final class QueenSideCastleMove extends CastleMove
    {
        public QueenSideCastleMove(Board board, Piece pieceMoved, int destinationCoordinate, int sourceCoordinate,
                                   Rook castleRook,int castleRookStart,int castleRookDestination)
        {
            super(board, pieceMoved, destinationCoordinate, sourceCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString()
        {
            return "0-0-0";
        }
    }
    public static final class NullMove extends Move
    {
        public NullMove()
        {
            super(null, null, -1, -1);
        }
        @Override
        public Board execute()
        {
            throw new RuntimeException("cannot execute the null Move!");
        }
    }
    public static class MoveFactory
    {
        private MoveFactory()
        {
            throw new RuntimeException("Class not Instantiable");
        }
        public static Move createMove(final Board board,final int sourceCoordinate,final int destinationCoordinate)
        {
            for(final Move move:board.getAllLegalMoves())
            {
                if(move.getSourceCoordinate()==sourceCoordinate
                   && move.getDestinationCoordinate()==destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
