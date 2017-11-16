package draughtscoursework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.*;

public class Board 
{
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Move transitionMove;
    
    private static final Board STANDARD_BOARD = createStandardBoardIm();

    private Board(final Builder builder)
    {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : Move.MoveFactory.getNullMove();
    }
    
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < BoardUtils.NUM_TILES; i++)
        {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) %  BoardUtils.NUM_TILES_PER_ROW == 0)
            {
                builder.append("\n");
            }    
        }
        return builder.toString();
    }
    
    public Player whitePlayer()
    {
        return this.whitePlayer;
    }
    
    public Player blackPlayer()
    {
        return this.blackPlayer;
    }
    
    public Player currentPlayer()
    {
        return this.currentPlayer;
    }
    
    public Collection<Piece> getBlackPieces()
    {
        return this.blackPieces;
    }
    
    public Collection<Piece> getWhitePieces()
    {
        return this.whitePieces;
    }
    
    public Iterable<Piece> getAllPieces() 
    {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePieces, this.blackPieces));
    }
    
    public Iterable<Move> getAllLegalMoves() 
    {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }
    
    public Tile getTile(final int coordinate) 
    {
        return this.gameBoard.get(coordinate);
    }

    public List<Tile> getGameBoard() 
    {
        return this.gameBoard;
    }

    public Move getTransitionMove() 
    {
        return this.transitionMove;
    }

    //This calculates all the legal moves that can be played for all pieces on each side.
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) 
    {
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final Piece piece : pieces)
        {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    //This calculates which pieces are still in play on the board.
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) 
    {
        final List<Piece> activePieces = new ArrayList<>();
        
        for(final Tile tile : gameBoard)
        {
            if(tile.isTileOccupied())
            {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance)
                {
                    activePieces.add(piece);
                }
            }    
        }
        return ImmutableList.copyOf(activePieces);
    }
    
    //Populates a list of tiles numbered 0-63 which will represent game board
    private static List<Tile> createGameBoard(final Builder boardBuilder) 
    {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) 
        {
//            tiles[i] = Tile.createTile(i, boardBuilder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    
    public static Board createStandardBoard() 
    {
        return STANDARD_BOARD;
    }
    
    //Sets the pieces starting positions on the board so that when the board comes around to see
    //which tiles are occupied or not (done in Tile class) they are set as occupied tiles.
    public static Board createStandardBoardIm()
    {
        final Builder builder = new Builder();
        
        //Black piece layout
        builder.setPiece(new Checker(Alliance.BLACK,1));
        builder.setPiece(new Checker(Alliance.BLACK,3));
        builder.setPiece(new Checker(Alliance.BLACK,5));
        builder.setPiece(new Checker(Alliance.BLACK,7));
        builder.setPiece(new Checker(Alliance.BLACK,8));
        builder.setPiece(new Checker(Alliance.BLACK,10));
        builder.setPiece(new Checker(Alliance.BLACK,12));
        builder.setPiece(new Checker(Alliance.BLACK,14));
        builder.setPiece(new Checker(Alliance.BLACK,17));
        builder.setPiece(new Checker(Alliance.BLACK,19));
        builder.setPiece(new Checker(Alliance.BLACK,21));
        builder.setPiece(new Checker(Alliance.BLACK,23));
        
        //White piece layout
        builder.setPiece(new Checker(Alliance.WHITE,40));
        builder.setPiece(new Checker(Alliance.WHITE,42));
        builder.setPiece(new Checker(Alliance.WHITE,44));
        builder.setPiece(new Checker(Alliance.WHITE,46));
        builder.setPiece(new Checker(Alliance.WHITE,49));
        builder.setPiece(new Checker(Alliance.WHITE,51));
        builder.setPiece(new Checker(Alliance.WHITE,53));
        builder.setPiece(new Checker(Alliance.WHITE,55));
        builder.setPiece(new Checker(Alliance.WHITE,56));
        builder.setPiece(new Checker(Alliance.WHITE,58));
        builder.setPiece(new Checker(Alliance.WHITE,60));
        builder.setPiece(new Checker(Alliance.WHITE,62));
        
        //White to move first
        builder.setMoveMaker(Alliance.WHITE);
        
        return builder.builder();
    }
    
    public static class Builder
    {
        Map<Integer,Piece> boardConfig;
        Alliance nextMoveMaker;
        Move transitionMove;
        
        public Builder()
        {
            this.boardConfig = new HashMap<>(33, 1.0f);
        }
        
        public Builder setPiece(final Piece piece)
        {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        public Builder setMoveMaker(final Alliance nextMoveMaker)
        {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        
        public Builder setMoveTransition(final Move transitionMove) 
        {
            this.transitionMove = transitionMove;
            return this;
        }
        
        public Board builder()
        {
            return new Board(this);
        }   
    }    
}
