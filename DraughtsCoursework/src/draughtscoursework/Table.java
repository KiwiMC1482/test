package draughtscoursework;

import draughtscoursework.Move.MoveFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public final class Table extends Observable 
{
    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private final DebugPanel debugPanel;
    private final GameSetup gameSetup;
    private final BoardPanel boardpanel;
    private Board draughtsBoard;
    private Move computerMove;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private boolean highlightLegalMoves;
    private boolean useBook;
    
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    
    private final Color lightTileColour = Color.decode("#FFFACD");
    private final Color darkTileColour = Color.decode("#593E1A");
    
    
    private static final Table INSTANCE = new Table();

    //Creates the main Jframe and populates it with the menu bar,board and pieces
    Table()
    {
        this.gameFrame = new JFrame("JChess");
        
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.draughtsBoard = Board.createStandardBoard();
        this.boardpanel = new BoardPanel();
        this.gameFrame.add(this.boardpanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameHistoryPanel = new GameHistoryPanel();
        this.debugPanel = new DebugPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardpanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(debugPanel, BorderLayout.SOUTH);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        center(this.gameFrame);
        this.gameFrame.setVisible(true);
    }
    
     public static Table get() 
     {
        return INSTANCE;
    }

    private JFrame getGameFrame() 
    {
        return this.gameFrame;
    }

    private Board getGameBoard() 
    {
        return this.draughtsBoard;
    }

    private MoveLog getMoveLog() 
    {
        return this.moveLog;
    }

    private BoardPanel getBoardPanel() 
    {
        return this.boardpanel;
    }
    
    private DebugPanel getDebugPanel() 
    {
        return this.debugPanel;
    }
    
    private GameSetup getGameSetup()
    {
        return this.gameSetup;
    }

    private GameHistoryPanel getGameHistoryPanel() 
    {
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() 
    {
        return this.takenPiecesPanel;
    }

    private boolean getHighlightLegalMoves() 
    {
        return this.highlightLegalMoves;
    }

    private boolean getUseBook() 
    {
        return this.useBook;
    }

    public void show() 
    {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(draughtsBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
        Table.get().getDebugPanel().redo();
    }

    private JMenuBar populateTableMenuBar() 
    {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;    
    }

    //Create the click down file menu
    private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private JMenu createFileMenu() 
    {
        final JMenu filesMenu = new JMenu("File");
        filesMenu.setMnemonic(KeyEvent.VK_F);
        
        final JMenuItem resetMenuItem = new JMenuItem("New Game", KeyEvent.VK_P);
        resetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                undoAllMoves();
            }

        });
        filesMenu.add(resetMenuItem);
        
        final JMenuItem undoMoveMenuItem = new JMenuItem("Undo last move", KeyEvent.VK_M);
        undoMoveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(Table.get().getMoveLog().size() > 0) {
                    undoLastMove();
                }
            }
        });
        filesMenu.add(undoMoveMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Table.get().getGameFrame().dispose();
                System.exit(0);
            }
        });
        filesMenu.add(exitMenuItem);
        
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game", KeyEvent.VK_S);
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        filesMenu.add(setupGameMenuItem);

        return filesMenu;
    }
    
    private static String playerInfo(final Player player) 
    {
        return ("Player is: " +player.getAlliance() + "\nlegal moves (" +player.getLegalMoves().size()+ ") = " +player.getLegalMoves() + "\ninCheckMate = " + player.isInCheckMate());
    }

    private void updateGameBoard(final Board board) 
    {
        this.draughtsBoard = board;
    }

    private void updateComputerMove(final Move move) 
    {
        this.computerMove = move;
    }

    private void undoAllMoves() 
    {
        for(int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) 
        {
            final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
            this.draughtsBoard = this.draughtsBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        }
        this.computerMove = null;
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(draughtsBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(draughtsBoard);
        Table.get().getDebugPanel().redo();
    }
    
    private void undoLastMove() 
    {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.draughtsBoard = this.draughtsBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        this.computerMove = null;
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().redo(draughtsBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(draughtsBoard);
        Table.get().getDebugPanel().redo();
    }

    private void moveMadeUpdate(final PlayerType playerType) 
    {
        setChanged();
        notifyObservers(playerType);
    }

    private void setupUpdate(final GameSetup gameSetup) 
    {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer 
    {
        //@Override
        public void update(final Observable o, final Object arg) 
        {

            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                !Table.get().getGameBoard().currentPlayer().isInCheckMate())
            {
                System.out.println(Table.get().getGameBoard().currentPlayer() + " is set to AI, thinking....");
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) 
            {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    enum PlayerType 
    {
        HUMAN,
        COMPUTER
    }

    private static class AIThinkTank extends SwingWorker<Move, String> 
    {

        private AIThinkTank() 
        {
        }

        @Override
        protected Move doInBackground() throws Exception 
        {
            final Move bestMove;
            final Move bookMove = Table.get().getUseBook()
                    ? SqlGamePersistence.get().getNextBestMove(Table.get().getGameBoard(),
                    Table.get().getGameBoard().currentPlayer(),
                    Table.get().getMoveLog().getMoves().toString().replaceAll("\\[", "").replaceAll("\\]", ""))
                    : MoveFactory.getNullMove();
            if (Table.get().getUseBook() && bookMove != MoveFactory.getNullMove()) {
                bestMove = bookMove;
            }
            else 
            {
                final StockAlphaBeta strategy = new StockAlphaBeta(Table.get().getGameSetup().getSearchDepth());
                strategy.addObserver(Table.get().getDebugPanel());
                bestMove = strategy.execute(
                        Table.get().getGameBoard());
            }
            return bestMove;
        }

        @Override
        public void done() 
        {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getToBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().getDebugPanel().redo();
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    private class BoardPanel extends JPanel
    {
        final List<TilePanel> boardTiles;
        
        BoardPanel()
        {
            //Builds the board of 64 tiles in a Jswing panel
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            
            for(int i = 0; i < BoardUtils.NUM_TILES; i++)
            {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4726"));
            
            validate();
        }
        
        public void drawBoard(final Board board)
        {
            removeAll();
            for(final TilePanel tilePanel : boardTiles)
            {
             tilePanel.drawTile(board);
             add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    
    //Records the moves made and displays them on screen
    public static class MoveLog 
    {

        private final List<Move> moves;

        MoveLog() 
        {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() 
        {
            return this.moves;
        }

        void addMove(final Move move) 
        {
            this.moves.add(move);
        }

        public int size() 
        {
            return this.moves.size();
        }

        void clear() 
        {
            this.moves.clear();
        }

        Move removeMove(final int index) 
        {
            return this.moves.remove(index);
        }

        boolean removeMove(final Move move) 
        {
            return this.moves.remove(move);
        }
    }
    
    private class TilePanel extends JPanel
    {
        private final int tileId;
        
        TilePanel(final BoardPanel boardPanel, final int tileId)
        {
            super(new GridBagLayout());
            
            this.tileId = tileId;
            
            setPreferredSize(TILE_PANEL_DIMENSION);
            
            assignTileColour();
            assignTileIcon(draughtsBoard);
            
            //Right mouse click cancels piece selection. Left mouse click moves piece
            addMouseListener(new MouseListener() 
            {
                @Override
                public void mouseClicked(final MouseEvent event) 
                {

                    if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) || BoardUtils.isEndGame(Table.get().getGameBoard())) 
                    {
                        return;
                    }

                    if (isRightMouseButton(event)) 
                    {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(event)) 
                    {
                        if (sourceTile == null) 
                        {
                            sourceTile = draughtsBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) 
                            {
                                sourceTile = null;
                            }
                        } 
                        else 
                        {
                            destinationTile = draughtsBoard.getTile(tileId);
                            final Move move = MoveFactory.createMove(draughtsBoard, sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = draughtsBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) 
                            {
                                draughtsBoard = transition.getToBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                    }
                    invokeLater(new Runnable()
                    {
                        public void run() 
                        {
                            gameHistoryPanel.redo(draughtsBoard, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            boardPanel.drawBoard(draughtsBoard);
                            debugPanel.redo();
                        }
                    });
                }
                
                @Override
                public void mousePressed(final MouseEvent me) 
                {
                
                }

                @Override
                public void mouseReleased(final MouseEvent me) 
                {
                
                }

                @Override
                public void mouseEntered(final MouseEvent me) 
                {
                
                }

                @Override
                public void mouseExited(final MouseEvent me) 
                {
                
                }
            });
            
            validate();
        }
        
        public void drawTile(final Board board)
        {
            assignTileColour();
            assignTileIcon(board);
            highlightLegalMoves(board);
            highlightAIMove();
            validate();
            repaint();
        }
        
        //Highlights the border surrounding the board
        private void highlightTileBorder(final Board board) 
        {
            if(humanMovedPiece != null &&
               humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance() &&
               humanMovedPiece.getPiecePosition() == this.tileId) 
            {
                setBorder(BorderFactory.createLineBorder(Color.cyan));
            } 
            else 
            {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }
        
        //Highlights the ai moves
        private void highlightAIMove() 
        {
            if(computerMove != null) 
            {
                if(this.tileId == computerMove.getCurrentCoordinates()) 
                {
                    setBackground(Color.pink);
                } 
                else if(this.tileId == computerMove.getDesCoordinates()) 
                {
                    setBackground(Color.red);
                }
            }
        }

        //Collection of the legal moves a piece can take when human
        private Collection<Move> pieceLegalMoves(final Board board) 
        {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance())
            {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        
        //Gets pieces icon and calls the assigned picture associated to the piece
        private void assignTileIcon(final Board board) 
        {
            this.removeAll();
            
            if(board.getTile(this.tileId).isTileOccupied()) 
            {
                if(board.getTile(this.tileId).getPiece().getPieceAlliance().isWhite())
                {
                    //White King
                    if(board.getTile(this.tileId).getPiece().getPieceType().isKing())
                    {
                        try
                        {
                         final BufferedImage whiteKingImage = ImageIO.read(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\DraughtsCoursework\\art\\WK.gif"));
                         add(new JLabel(new ImageIcon(whiteKingImage)));
                        }
                        catch(final IOException e) 
                        {
                        e.printStackTrace();
                        }
                    }
                    //White Checker
                    else
                    {
                        try
                        {
                         final BufferedImage whiteCheckerImage = ImageIO.read(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\DraughtsCoursework\\art\\WC.gif"));
                         add(new JLabel(new ImageIcon(whiteCheckerImage))); 
                        }
                        catch(final IOException e) 
                        {
                        e.printStackTrace();
                        }            
                    }
                }
            }
            
            if(board.getTile(this.tileId).isTileOccupied()) 
            {
                if(board.getTile(this.tileId).getPiece().getPieceAlliance().isBlack())
                {
                    //Black King
                    if(board.getTile(this.tileId).getPiece().getPieceType().isKing())
                    {
                        try
                        {
                         final BufferedImage blackKingImage = ImageIO.read(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\DraughtsCoursework\\art\\BK.gif"));
                         add(new JLabel(new ImageIcon(blackKingImage)));
                        }
                        catch(final IOException e) 
                        {
                        e.printStackTrace();
                        }
                    }
                    //Black Checker
                    else
                    {
                        try
                        {
                         final BufferedImage blackCheckerImage = ImageIO.read(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\DraughtsCoursework\\art\\BC.gif"));
                         add(new JLabel(new ImageIcon(blackCheckerImage)));
                        }
                        catch(final IOException e) 
                        {
                        e.printStackTrace();
                        }       
                    }
                }
            }
        }

        //Highlights all legal moves the player can make 
        private void highlightLegalMoves(final Board board)
        {
            if(true)
            {
                for(final Move move : pieceLegalMoves(board))
                {
                    try
                    {
                     add(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\DraughtsCoursework\\art\\green_dot.png")))));   
                    }
                    catch(Exception e)
                    {
                        
                    }
                }
            }
        }
        
        //Calculates which tiles are light and dark
        private void assignTileColour() 
        {
            if (BoardUtils.INSTANCE.FIRST_ROW.get(this.tileId) || BoardUtils.INSTANCE.THIRD_ROW.get(this.tileId) ||
                BoardUtils.INSTANCE.FIFTH_ROW.get(this.tileId) || BoardUtils.INSTANCE.SEVENTH_ROW.get(this.tileId)) 
            {
                setBackground(this.tileId % 2 == 0 ? lightTileColour : darkTileColour);
            } 
            else if(BoardUtils.INSTANCE.SECOND_ROW.get(this.tileId) || BoardUtils.INSTANCE.FOURTH_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.SIXTH_ROW.get(this.tileId)  || BoardUtils.INSTANCE.EIGHTH_ROW.get(this.tileId)) 
            {
                setBackground(this.tileId % 2 != 0 ? lightTileColour : darkTileColour);
            }          
        }
    }    
}