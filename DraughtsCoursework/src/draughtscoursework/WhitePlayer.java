package draughtscoursework;

import java.util.Collection;

class WhitePlayer extends Player{

    WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) 
    {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }
    
    @Override
    public Collection<Piece> getActivePieces() 
    {
       return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() 
    {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() 
    {
        return this.board.blackPlayer();
    }
}
