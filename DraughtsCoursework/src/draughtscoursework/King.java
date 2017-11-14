/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draughtscoursework;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author user
 */
public class King extends Piece 
{
    private final static int[] CANIDATE_MOVES = {-9, -7, 7, 9};
    
    public King(final Alliance pieceAlliance, final int piecePosition)
    {
        super(Piece.PieceType.KING, piecePosition, pieceAlliance);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board)
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
        return Piece.PieceType.KING.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int canidateOffset)
    {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (canidateOffset == -9);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int canidateOffset)
    {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (canidateOffset == -7);
    }

    @Override
    public King movePiece(Move move) 
    {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDesCoordinates());
    }
    
}
