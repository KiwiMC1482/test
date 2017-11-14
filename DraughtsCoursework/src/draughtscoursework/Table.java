package draughtscoursework;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Table 
{
    private final JFrame gameFrame;
    private final BoardPanel boardpanel;
    private final Board draughtsBoard;
    
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    //private static String pieceIconPath = "/draughtscoursework/BC.gif";
    
    
    private final Color lightTileColour = Color.decode("#FFFACD");
    private final Color darkTileColour = Color.decode("#593E1A");
    
    public Table()
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
    }

    private JMenuBar populateTableMenuBar() 
    {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;    
    }

    private JMenu createFileMenu() 
    {
        final JMenu fileMenu = new JMenu("File");
        
        final JMenu exitMenuItem = new JMenu("Exit");
        exitMenuItem.addActionListener((ActionEvent e) -> 
        {
            System.exit(0);
        });
        fileMenu.add(exitMenuItem);
        
        return fileMenu;
    }
    
    private class BoardPanel extends JPanel
    {
        final List<TilePanel> boardTiles;
        
        BoardPanel()
        {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            
            for(int i = 0; i < BoardUtils.NUM_TILES; i++)
            {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
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
            validate();
        }
        
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
                
        private void assignTileColour() 
        {
            if (BoardUtils.EIGHTH_ROW[this.tileId] || BoardUtils.SIXTH_ROW[this.tileId] ||
                BoardUtils.FOURTH_ROW[this.tileId] || BoardUtils.SECOND_ROW[this.tileId])
            {
                setBackground(this.tileId % 2 == 0 ? lightTileColour : darkTileColour);
            }
            else if(BoardUtils.SEVENTH_ROW[this.tileId] || BoardUtils.FIFTH_ROW[this.tileId] ||
                BoardUtils.THIRD_ROW[this.tileId] || BoardUtils.FIRST_ROW[this.tileId])
            {
                 setBackground(this.tileId % 2 != 0 ? lightTileColour : darkTileColour);       
            }            
        }
    }    
}
