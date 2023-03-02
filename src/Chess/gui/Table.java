package Chess.gui;
import Chess.engine.board.*;
import Chess.engine.pieces.Piece;
import Chess.engine.player.MoveTransition;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
public class Table
{
    private final JFrame gameArea;

    private Board chessBoard = Board.createStandardGameBoard();
    private final BoardPanel boardPanel = new BoardPanel();
    private final static String pieceIconPath = "Chess_icons/pieces/";
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension outerAreaDimension =new Dimension(600,600);
    private final static Dimension boardPanelDimension = new Dimension(400,350);
    private final static Dimension tilePanelDimension = new Dimension(10,10);
    public Table()
    {
        final JMenuBar tableMenuBar= createTableMenuBar();
        this.gameArea =new JFrame("Chess");
        this.gameArea.setLayout(new BorderLayout());
        this.gameArea.setJMenuBar(tableMenuBar);
        this.gameArea.setSize(outerAreaDimension);
        this.gameArea.add(this.boardPanel,BorderLayout.CENTER);
        this.gameArea.setVisible(true);
    }
    private JMenuBar createTableMenuBar()
    {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenuItem());
        return tableMenuBar;
    }
    private JMenu createFileMenuItem()
    {
        final JMenu files=new JMenu("File");
        final JMenuItem openPGN=new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Open png file");
            }
        });
        files.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        files.add(exitMenuItem);
        return files;
    }
    private class BoardPanel extends JPanel
    {
        final List<TilePanel> boardTiles;
        BoardPanel()
        {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i=0;i< Utilities.numberOfTiles;i++)
            {
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(boardPanelDimension);
            validate();
        }

        public void drawBoard(final Board chessBoard)
        {
            removeAll();
            for(final TilePanel tilePanel : boardTiles)
            {
                tilePanel.drawTile(chessBoard);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    private class TilePanel extends JPanel
    {
        private final int tileID;
        TilePanel(final BoardPanel boardPanel,final int tileID)
        {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(tilePanelDimension);
            assignTileColour();
            assignTileIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isLeftMouseButton(e))
                    {
                        if(sourceTile == null)
                        {
                             sourceTile = chessBoard.getTile(tileID);
                             humanMovedPiece = sourceTile.getPiece();
                             if(humanMovedPiece == null)
                                 sourceTile = null;
                        }
                        else
                        {
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = Move.MoveFactory.createMove(chessBoard,
                                                                          sourceTile.getCoordinate(),
                                                                          destinationTile.getCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone())
                            {
                                chessBoard = transition.getBoard();
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                    else if(isRightMouseButton(e))
                    {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }
        public void drawTile(final Board board)
        {
            assignTileColour();
            assignTileIcon(board);
            validate();
            repaint();
        }
        private void assignTileIcon(final Board board)
        {
            this.removeAll();
            if (board.getTile(this.tileID).isOccupied())
            {
                try
                {
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath
                            + board.getTile(this.tileID).getPiece().getColour().toString().substring(0, 1)
                            + board.getTile(this.tileID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        private void assignTileColour()
        {
            final Color lightColour = new Color(192,192,192);
            final Color darkColour = new Color(100,100,100);
            final Coordinate tileIDCoordinate = Coordinate.createCoordinate(this.tileID);
            if((tileIDCoordinate.x + tileIDCoordinate.y)%2==0)
                setBackground(lightColour);
            else
                setBackground(darkColour);
        }
    }
}
