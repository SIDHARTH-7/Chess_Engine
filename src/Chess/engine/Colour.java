package Chess.engine;
import Chess.engine.player.BlackPlayer;
import Chess.engine.player.Player;
import Chess.engine.player.WhitePlayer;

public enum Colour
{
    WHITE
            {
                @Override
                public boolean isWhite()
                {return true;}
                public boolean isBlack()
                {return false;}
                public Player choosePlayer(final WhitePlayer WhitePlayer, final BlackPlayer BlackPlayer)
                {return WhitePlayer;}
            },
    BLACK
            {
                @Override
                public boolean isWhite()
                {return false;}
                public boolean isBlack()
                {return true;}
                public Player choosePlayer(final WhitePlayer WhitePlayer, final BlackPlayer BlackPlayer)
                {return BlackPlayer;}
            };
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Player choosePlayer(final WhitePlayer WhitePlayer, final BlackPlayer BlackPlayer);
}
