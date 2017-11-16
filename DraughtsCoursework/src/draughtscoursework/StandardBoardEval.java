package draughtscoursework;

import com.google.common.annotations.VisibleForTesting;

public final class StandardBoardEval implements BoardEval 
{
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int MOBILITY_MULTIPLIER = 2;
    private final static int ATTACK_MULTIPLIER = 2;
    private final static int TWO_KINGS_BONUS = 50;
    private static final StandardBoardEval INSTANCE = new StandardBoardEval();

    private StandardBoardEval() 
    {
    }

    public static StandardBoardEval get() 
    {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) 
    {
        return score(board.whitePlayer(), depth) - score(board.blackPlayer(), depth);
    }

    @VisibleForTesting
    private static int score(final Player player,final int depth) 
    {
        return mobility(player) +attacks(player) + pieceEvaluations(player);
    }

    private static int attacks(final Player player) 
    {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()) 
        {
            if(move.isAttack()) 
            {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if(movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) 
                {
                    attackScore ++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceEvaluations(final Player player) 
    {
        int pieceValuationScore = 0;
        int numKings = 0;
        for (final Piece piece : player.getActivePieces()) 
        {
            pieceValuationScore += piece.getPieceValue() + piece.locationBonus();
            if(piece.getPieceType().isKing()) 
            {
                numKings++;
            }
        }
        return pieceValuationScore + (numKings == 2 ? TWO_KINGS_BONUS : 0);
    }

    private static int mobility(final Player player) 
    {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) 
    {
        return (int)((player.getLegalMoves().size() * 100.0f) / player.getOpponent().getLegalMoves().size());
    }

    private static int depthBonus(final int depth) 
    {
        return depth == 0 ? 1 : 100 * depth;
    }

    

}
