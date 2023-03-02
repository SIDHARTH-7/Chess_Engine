package Chess.engine.player;

import Chess.engine.board.Board;
import Chess.engine.board.Move;

import java.util.concurrent.Future;

public class MoveTransition
{
    private final Board board;
    private final Move move;
    private final MoveStatus moveStatus;
    public MoveTransition(final Board board,final Move move,final MoveStatus moveStatus)
    {
        this.move = move;
        this.board = board;
        this.moveStatus = moveStatus;
    }
    public MoveStatus getMoveStatus()
    {
        return this.moveStatus;
    }

    public Board getBoard()
    {
        return this.board;
    }
}
