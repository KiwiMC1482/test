package draughtscoursework;

import draughtscoursework.Board.Builder;

public abstract class Move 
{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    
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
    
    //Following methods are getters for the current pieces coordinates, if it's attacking and where the attacked piece is
    public Board getBoard() 
    {
        return this.board;
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

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        builder.setMoveTransition(this);
        
        return builder.builder();
    }
    
    public Board undo() 
    {
        final Board.Builder builder = new Builder();
        for (final Piece piece : this.board.getAllPieces()) 
        {
            builder.setPiece(piece);
        }
        builder.setMoveMaker(this.board.currentPlayer().getAlliance());
        return builder.builder();
    }

    public String disambiguationFile() 
    {
        for(final Move move : this.board.currentPlayer().getLegalMoves()) 
        {
            if(move.getDesCoordinates() == this.destinationCoordinate && !this.equals(move) &&
               this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) 
            {
                return BoardUtils.INSTANCE.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }
    
    public enum MoveStatus {

        DONE {
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE {
            @Override
            public boolean isDone() {
                return false;
            }
        
        };

        public abstract boolean isDone();
    }

    public static class CheckerPromotion extends Move
    {

        final Move decoratedMove;
        final Checker promotedChecker;
        final Piece promotionPiece;

        public CheckerPromotion(final Move decoratedMove, final Piece promotionPiece) 
        {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDesCoordinates());
            this.decoratedMove = decoratedMove;
            this.promotedChecker = (Checker) decoratedMove.getMovedPiece();
            this.promotionPiece = promotionPiece;
        }
    }
    
    //A basic move of the piece
    public static class normalMove extends Move
    {
        normalMove(final Board board, final Piece movedPiece, final int destinationCoordinate)
        {
            super(board, movedPiece, destinationCoordinate);
        } 
        
        @Override
        public boolean equals(final Object other) 
        {
            return this == other || other instanceof normalMove && super.equals(other);
        }
        
        @Override
        public String toString() 
        {
            return movedPiece.getPieceType().toString() + disambiguationFile() + BoardUtils.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
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
    
   private static class nullMove extends Move 
   {

        private nullMove() 
        {
            super(null, null ,-1);
        }

        //@Override
        public int getCurrentCoordinate() 
        {
            return -1;
        }

        //@Override
        public int getDestinationCoordinate() 
        {
            return -1;
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }
    
    public static class MoveFactory
    {
        public static final Move NULL_MOVE = new nullMove();
        
        private MoveFactory() 
        {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Move getNullMove() 
        {
            return NULL_MOVE;
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) 
        {
            for (final Move move : board.getAllLegalMoves()) 
            {
                if (move.getCurrentCoordinates() == currentCoordinate && move.getDesCoordinates() == destinationCoordinate) 
                {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}