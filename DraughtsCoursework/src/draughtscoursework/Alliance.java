package draughtscoursework;

//Is the alliance of the piece black or white
public enum Alliance 
{
    BLACK //Going down the board
    {
      @Override
      public int getDirection()
      {
          return 1;
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
      
      public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer)
      {
          return blackPlayer;
      }
    },
    WHITE //Going up the board
    {
      @Override
      public int getDirection()
      {
          return -1;
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
      
      public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer)
      {
          return whitePlayer;
      }
    };
    
    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
