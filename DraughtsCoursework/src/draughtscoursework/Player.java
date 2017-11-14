package draughtscoursework;

import com.google.common.collect.ImmutableList;
import java.util.*;

public abstract class Player 
{
    protected final Board board;
    protected final Collection<Move> legalMoves;
    protected final King playerKing;
//    protected final Checker playerChecker;
//    private final boolean isInStaleMate;

    public Collection<Move> getLegalMoves()
    {
        return this.legalMoves;
    }
    
    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves)
    {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
//        this.playerChecker = establishChecker();
//        this.isInStaleMate = !Player.calculateAttacksOnTile(this.playerChecker.getPiecePosition(), opponentMoves).isEmpty();
    }
    
    //Is the checker on the tile being attack
//    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves)
//    {
//        final List<Move> attackMoves = new ArrayList<>();
//        for(final Move move : moves)
//        {
//            if(piecePosition == move.getDesCoordinates())
//            {
//                attackMoves.add(move);
//            }
//        }
//        return ImmutableList.copyOf(attackMoves);
//    }
    
    //Is the piece a king on the board
    private King establishKing()
    {
        for(final Piece piece: getActivePieces())
        {
            if(piece.getPieceType().isKing())
            {
                return (King) piece;
            }
        }
        return null;
    }
    
    //Is there normal pieces
    private Checker establishChecker()
    {
        for(final Piece piece: getActivePieces())
        {
            if(piece.getPieceType().isKing())
            {
                return (Checker) piece;
            }
        }
        return null;
        
    }
    
    public boolean isMoveLegal(final Move move)
    {
        return this.legalMoves.contains(move);
    }
    
    public boolean isInCheckMate()
    {
        for (final Move move : this.legalMoves)
        {
            if (this.getOpponent().getActivePieces() == null)
            {
            return true;
            }
        }
        return false;
    }
    
//    public boolean isInStaleMate()
//    {
//        return this.isInStaleMate;
//    }
    
    //When a move is made. MoveTransition is returned which will wrap the board 
    //that is being transitioned to if move was valid
    public MoveTransition makeMove(final Move move)
    {
        if(!isMoveLegal(move))
        {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL);
        }
        
        return null;
    }
    
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
