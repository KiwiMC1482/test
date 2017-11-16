package draughtscoursework;

//Is the alliance of the piece black or white
//Depending on the alliance the piece can only move in a certain direction
public enum Alliance 
{
    BLACK //Going down the board
    {
        @Override
        public int getDirection()
        {
          return DOWN;
        }

        @Override
        public int getOppositeDirection()
        {
          return UP;
        }

        @Override
        public boolean isWhite()        
        {
          return false;
        }

        @Override 
        public boolean isBlack()
        {
          return true;
        }

        //@Override
        public boolean isPawnPromotionSquare(final int position) 
        {
            return BoardUtils.INSTANCE.FIRST_ROW.get(position);
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) 
        {
            return blackPlayer;
        }

        @Override
        public String toString() 
        {
            return "Black";
        }

        @Override
        public int checkerBonus(final int position) 
        {
            return BLACK_CHECKER_PREFERRED_COORDINATES[position];
        }

        @Override
        public int kingBonus(final int position) 
        {
            return BLACK_KING_PREFERRED_COORDINATES[position];
        }
    },
    WHITE //Going up the board
    {
        @Override
        public int getDirection()
        {
            return UP;
        }
      
        @Override
        public int getOppositeDirection()
        {
            return DOWN;
        }
      
        @Override
        public boolean isWhite()        
        {
            return true;
        }
      
        @Override 
        public boolean isBlack()
        {
            return false;
        }
      
        //@Override
        public boolean isPawnPromotionSquare(final int position) 
        {
            return BoardUtils.INSTANCE.FIRST_ROW.get(position);
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) 
        {
            return whitePlayer;
        }

        @Override
        public String toString() 
        {
            return "White";
        }

        @Override
        public int checkerBonus(final int position) 
        {
            return WHITE_CHECKER_PREFERRED_COORDINATES[position];
        }

        @Override
        public int kingBonus(final int position) 
        {
            return WHITE_KING_PREFERRED_COORDINATES[position];
        }
};
    
public abstract int getDirection();
public abstract int getOppositeDirection();
public abstract boolean isWhite();
public abstract boolean isBlack();
public abstract int kingBonus(int position);
public abstract int checkerBonus(int position);
public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);

private final static int[] WHITE_CHECKER_PREFERRED_COORDINATES = 
{
    -20,-10,-10,-10,-10,-10,-10,-20,
    -10,  0,  0,  0,  0,  0,  0,-10,
    -10,  0,  5, 10, 10,  5,  0,-10,
    -10,  5,  5, 10, 10,  5,  5,-10,
    -10,  0, 10, 10, 10, 10,  0,-10,
    -10, 10, 10, 10, 10, 10, 10,-10,
    -10,  5,  0,  0,  0,  0,  5,-10,
    -20,-10,-10,-10,-10,-10,-10,-20
};

private final static int[] BLACK_CHECKER_PREFERRED_COORDINATES = 
{
    -20,-10,-10,-10,-10,-10,-10,-20,
    -10,  5,  0,  0,  0,  0,  5,-10,
    -10, 10, 10, 10, 10, 10, 10,-10,
    -10,  0, 10, 10, 10, 10,  0,-10,
    -10,  5,  5, 10, 10,  5,  5,-10,
    -10,  0,  5, 10, 10,  5,  0,-10,
    -10,  0,  0,  0,  0,  0,  0,-10,
    -20,-10,-10,-10,-10,-10,-10,-20,
};

private final static int[] WHITE_KING_PREFERRED_COORDINATES = 
{
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -20,-30,-30,-40,-40,-30,-30,-20,
    -10,-20,-20,-20,-20,-20,-20,-10,
     20, 20,  0,  0,  0,  0, 20, 20,
     20, 30, 10,  0,  0, 10, 30, 20
};

private final static int[] BLACK_KING_PREFERRED_COORDINATES = 
{
     20, 30, 10,  0,  0, 10, 30, 20,
     20, 20,  0,  0,  0,  0, 20, 20,
    -10,-20,-20,-20,-20,-20,-20,-10,
    -20,-30,-30,-40,-40,-30,-30,-20,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30
};

private static final int UP = -1;

private static final int DOWN = 1;
}
