/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draughtscoursework;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

enum PieceUtils 
{

    INSTANCE;

    private final Table<Alliance, Integer, Checker > ALL_POSSIBLE_CHECKER = PieceUtils.createAllPossibleMovedChecker();
    
    
    Checker getMovedBishop(final Alliance alliance, final int destinationCoordinate) 
    {
        return ALL_POSSIBLE_CHECKER.get(alliance, destinationCoordinate);
    }

    private static Table<Alliance, Integer, Checker > createAllPossibleMovedChecker() {
        final ImmutableTable.Builder<Alliance, Integer, Checker > pieces = ImmutableTable.builder();
        for(final Alliance alliance : Alliance.values()) 
        {
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) 
            {
                pieces.put(alliance, i, new Checker(alliance, i));
            }
        }
        return pieces.build();
    }
}
