package draughtscoursework;

import com.google.common.collect.ImmutableList;
import java.util.*;

public class Checker extends Piece 
{
    private final static int[] CANIDATE_MOVES = {-9, -7, 7, 9};
    
    public Checker(final Alliance pieceAlliance, final int piecePosition)
    {
        super(PieceType.CHECKER, piecePosition, pieceAlliance);
    }
    
    //Does as method name, Calculates the legal moves of the selected checker
    @Override
    public Collection<Move> calculateLegalMoves(final Board board)
    {
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int canidateCoordinateOffset: CANIDATE_MOVES)
        {
            int canidateDestinationCoordinate = this.piecePosition;
            
            while(BoardUtils.isValidCoOrd(canidateDestinationCoordinate))
            {
                if(isFirstColumnExclusion(canidateDestinationCoordinate, canidateCoordinateOffset) || isEighthColumnExclusion(canidateDestinationCoordinate, canidateCoordinateOffset))
                {
                    break;
                }
                canidateDestinationCoordinate += canidateCoordinateOffset;
                if (BoardUtils.isValidCoOrd(canidateDestinationCoordinate))
                {
                    final Tile canidateDestinationTile = board.getTile(canidateDestinationCoordinate);
                    if(!canidateDestinationTile.isTileOccupied())
                    {
                        legalMoves.add(new Move.normalMove(board, this, canidateDestinationCoordinate));
                        //break;
                    }
                    else
                    {
                        final Piece pieceAtDestination = canidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if(this.pieceAlliance != pieceAlliance)
                        {
                            legalMoves.add(new Move.attackMove(board, this, canidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }    
    return ImmutableList.copyOf(legalMoves);    
    }
    
    @Override
    public String toString()
    {
        return PieceType.CHECKER.toString();
    }
    
    //If the piece is on a square in the followung columns it limits the move directions it can make
    private static boolean isFirstColumnExclusion(final int currentCandidate, final int candidateDestinationCoordinate) 
    {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) && ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate, final int candidateDestinationCoordinate) 
    {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) && ((currentCandidate == -7) || (currentCandidate == 9));
    }

    @Override
    public Checker movePiece(Move move) 
    {
        return new Checker(move.getMovedPiece().getPieceAlliance(), move.getDesCoordinates());
    }

    @Override
    public int locationBonus() 
    {
        return this.pieceAlliance.checkerBonus(this.piecePosition);
    }
}
