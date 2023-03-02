package Chess.engine.board;
import Chess.engine.Colour;
import Chess.engine.pieces.*;
import Chess.engine.player.BlackPlayer;
import Chess.engine.player.Player;
import Chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.BufferedWriter;
import java.util.*;
import static Chess.engine.Colour.BLACK;
import static Chess.engine.Colour.WHITE;
public class Board
{
    private final List<Tile> gameBoard;
    private final Collection<Piece> whiteActivePieces;
    private final Collection<Piece> blackActivePieces;
    private final WhitePlayer whiteplayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
     private Board(Builder builder)
    {
        this.gameBoard=createGameBoard(builder);
        this.whiteActivePieces=getActivePieces(this.gameBoard,WHITE);
        this.blackActivePieces=getActivePieces(this.gameBoard,BLACK);
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(whiteActivePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(blackActivePieces);
        this.whiteplayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,blackStandardLegalMoves,whiteStandardLegalMoves);
        this.currentPlayer = builder.nextMoveColour.choosePlayer(this.whiteplayer,this.blackPlayer);
    }
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces)
    {
        final Collection<Move> legalMoves = new ArrayList<>();
        for(Piece piece: pieces)
        {
            legalMoves.addAll(piece.allLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<Utilities.numberOfTiles;i++)
        {
            final String tileInfo= this.gameBoard.get(i).toString();
            stringBuilder.append(String.format("%3s",tileInfo));
            if((i+1)%Utilities.numberOfColumnsOfTiles==0)
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public static Collection<Piece> getActivePieces(List<Tile> gameBoard, Colour colour)
    {
        final List<Piece> activePieces = new ArrayList<>();
        for(Tile tile:gameBoard)
        {
            if(tile.isOccupied())
            {
                if(tile.getPiece().getColour()==colour)
                    activePieces.add(tile.getPiece());
            }
        }
        return ImmutableList.copyOf(activePieces);
    }
    public final Tile getTile(final int tileCoordinate)
    {
        return gameBoard.get(tileCoordinate);
    }
    private List<Tile> createGameBoard(Builder builder)
    {
        final Tile tiles[] = new Tile[Utilities.numberOfTiles];int num=0;
        for(int i=0;i<Utilities.numberOfTiles;i++)
        {
                tiles[i]=Tile.createTile(i,builder.boardConfiguration.get(i));
        }
        System.out.println(num);
        return ImmutableList.copyOf(tiles);
    }
    public static Board createStandardGameBoard()
    {
        final Builder builder = new Builder();
        builder.putBoardConfiguration(new Rook(0, BLACK));
        builder.putBoardConfiguration(new Knight(1, BLACK));
        builder.putBoardConfiguration(new Bishop(2, BLACK));
        builder.putBoardConfiguration(new Queen(3,BLACK));
        builder.putBoardConfiguration(new King(4,BLACK));
        builder.putBoardConfiguration(new Bishop(5, BLACK));
        builder.putBoardConfiguration(new Knight(6, BLACK));
        builder.putBoardConfiguration(new Rook(7, BLACK));
        for(int i=8;i<16;i++)
        {
            builder.putBoardConfiguration(new Pawn(i,BLACK));
        }
        for(int i=48;i<56;i++)
        {
            builder.putBoardConfiguration(new Pawn(i,WHITE));
        }
        builder.putBoardConfiguration(new Rook(56 ,WHITE));
        builder.putBoardConfiguration(new Knight(57, WHITE));
        builder.putBoardConfiguration(new Bishop(58, WHITE));
        builder.putBoardConfiguration(new Queen(59,WHITE));
        builder.putBoardConfiguration(new King(60,WHITE));
        builder.putBoardConfiguration(new Bishop(61, WHITE));
        builder.putBoardConfiguration(new Knight(62, WHITE));
        builder.putBoardConfiguration(new Rook(63, WHITE));
        builder.nextMoveColour(WHITE);
        return builder.build();
    }

    public Collection<Piece> getWhiteActivePieces()
    {
        return this.whiteActivePieces;
    }

    public Collection<Piece> getBlackActivePieces()
    {
        return this.blackActivePieces;
    }

    public Player getWhitePlayer() {
        return this.whiteplayer;
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }
    public Player currentPlayer()
    {
        return this.currentPlayer;
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whiteplayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
    }

    public boolean areBridgingTilesOccupied(int tile1, int tile2)
    {
        if(tile1<=tile2) {
            for (int i = tile1; i <= tile2; i++) {
                if (this.getTile(i).isOccupied())
                    return true;
            }
        }
        else
        {
            for (int i = tile1; i >= tile2; i--) {
                if (this.getTile(i).isOccupied())
                    return true;
            }
        }
        return false;
    }

    public static class Builder
    {
        Map<Integer,Piece> boardConfiguration;
        Colour nextMoveColour;
        public Builder()
        {
            this.boardConfiguration=new HashMap<>();
        }
        public Builder putBoardConfiguration(final Piece piece)
        {
            this.boardConfiguration.put(piece.getPieceCoordinate(),piece);
            return this;
        }
        public Builder nextMoveColour(final Colour nextMoveColour)
        {
            this.nextMoveColour=nextMoveColour;
            return this;
        }
        public Board build()
        {
            return new Board(this);
        }

        public void putEnPassantPiece(Pawn movedPawn)
        {

        }
    }

}
