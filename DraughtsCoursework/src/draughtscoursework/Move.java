package draughtscoursework;

public abstract class Move 
{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    
    public static final Move NULL_MOVE = new nullMove();
    
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;   
    }
    
    @Override 
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object other)
    {
        if(this == other)
        {
            return true;
        }
        if(!(other instanceof Move))
        {
            return false;
        }
        final Move otherMove = (Move) other;
        return getDesCoordinates() == otherMove.getDesCoordinates() && getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    public Piece getMovedPiece()
    {
        return this.movedPiece;
    }
    
    public int getDesCoordinates()
    {
        return this.destinationCoordinate;
    }
    
    public int getCurrentCoordinates()
    {
        return this.getMovedPiece().getPiecePosition();
    }
    
    public boolean isAttack()
    {
        return false;
    }
    
    public Piece getAttackedPiece()
    {
        return null;
    }
    
    //Will create a new board every time move is executed
    public Board execute() 
    {
        final Board.Builder builder = new Board.Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces())
        {
            if (!this.movedPiece.equals(piece))
            {
                builder.setPiece(piece);
            }
        }

        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
        {
            builder.setPiece(piece);
        }

        builder.setPiece(null);
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.builder();
    }
    
    //A basic move of the piece
    public static final class normalMove extends Move
    {
        normalMove(final Board board, final Piece movedPiece, final int destinationCoordinate)
        {
            super(board, movedPiece, destinationCoordinate);
        }  
    }
    
    //An attack move of a piece
    public static final class attackMove extends Move
    {
        final Piece attackedPiece;
        
        attackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece)
        {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        
        //@Override
        public int hashcode()
        {
            return this.attackedPiece.hashcode() + super.hashCode();
        }
        
        @Override
        public boolean equals(final Object other)
        {
            if(this == other)
            {
                return true;
            }
            if(!(other instanceof attackMove))
            {
                return false;
            }
            final attackMove otherAttackMove = (attackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
            
        }
        
        //Will create a new board every time move is executed
        @Override
        public Board execute() 
        {
            return null;
        }
        
        @Override
        public boolean isAttack()
        {
            return true;
        }
        
        @Override
        public Piece getAttackedPiece()
        {
            return this.attackedPiece;
        }
    }
    
    public static final class nullMove extends Move
    {
        public nullMove()
        {
            super(null, null, -1);
        }
        
        @Override
        public Board execute()
        {
            throw new RuntimeException("Cannot execute null move");
        }
    }
    
    public static class MoveFactory
    {
        private MoveFactory()
        {
            throw new RuntimeException("Not instantiable");
        }
        
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate)
        {
            for(final Move move : board.getAllLegalMoves())
            {
                if(move.getCurrentCoordinates() == currentCoordinate && move.getDesCoordinates() == destinationCoordinate)
                {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
