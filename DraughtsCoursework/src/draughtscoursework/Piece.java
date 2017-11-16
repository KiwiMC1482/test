package draughtscoursework;

import java.util.*;

//Where is the piece and which side is it alligned to
public abstract class Piece 
{
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final PieceType pieceType;
    protected final boolean isFirstMove;
    private final int cachedHashcode;
    
    Piece(final PieceType pieceType ,final int piecePosition, final Alliance pieceAlliance)
    {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = false;
        this.cachedHashcode = computeHashcode();
    }
    
    private int computeHashcode() 
    {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(final Object other)
    {
        if(this == other)
        {
            return true;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();    
    }
    
    //@Override
    public int hashcode()
    {
        return this.cachedHashcode;
    }
    
    public int getPiecePosition()
    {
        return this.piecePosition;
    }
    
    public Alliance getPieceAlliance()
    {
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove()
    {
        return this.isFirstMove;
    }
    
    public PieceType getPieceType()
    {
        return this.pieceType;
    }
    
    public int getPieceValue() 
    {
        return this.pieceType.getPieceValue();
    }
    
    public abstract int locationBonus();

    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType
    {
        CHECKER(350 , "C") 
        {
            //@Override
            public boolean isChecker() 
            {
                return true;
            }
            
            @Override
            public boolean isKing() 
            {
                return false;
            }
        },
        KING(2000 , "K") 
        {
            //@Override
            public boolean isChecker() 
            {
                return false;
            }
            
            @Override
            public boolean isKing() 
            {
                return true;
            }
        };
        
        private final int value;
        private final String pieceName;
        
        
        PieceType(final int val, final String pieceName) 
        {
            this.value = val;
            this.pieceName = pieceName;
        }
        
        public int getPieceValue() 
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return this.pieceName;
        }
        
        public abstract boolean isKing();
    }
}
