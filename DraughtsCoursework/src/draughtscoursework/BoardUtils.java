package draughtscoursework;

// BoardUtils is a series of variables and methods which are used in many different places so instead of 

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import draughtscoursework.Move.MoveFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//constanlty typing them locally in other classes it is easier to have them in one place and call them from here
public enum BoardUtils
{
    INSTANCE;
    
    public final List<Boolean> FIRST_COLUMN = inItColumn(0);
    public final List<Boolean> SECOND_COLUMN = inItColumn(1);
    public final List<Boolean> THIRD_COLUMN = inItColumn(2);
    public final List<Boolean> FOURTH_COLUMN = inItColumn(3);
    public final List<Boolean> FIFTH_COLUMN = inItColumn(4);
    public final List<Boolean> SIXTH_COLUMN = inItColumn(5);
    public final List<Boolean> SEVENTH_COLUMN = inItColumn(6);
    public final List<Boolean> EIGHTH_COLUMN = inItColumn(7);
    
    public final List<Boolean> FIRST_ROW = inItRow(0);
    public final List<Boolean> SECOND_ROW = inItRow(8);
    public final List<Boolean> THIRD_ROW = inItRow(16);
    public final List<Boolean> FOURTH_ROW = inItRow(24);
    public final List<Boolean> FIFTH_ROW = inItRow(32);
    public final List<Boolean> SIXTH_ROW = inItRow(40);
    public final List<Boolean> SEVENTH_ROW = inItRow(48);
    public final List<Boolean> EIGHTH_ROW = inItRow(56);
    
    public final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    
    public static final int START_TILE_INDEX = 0;    
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    
    private BoardUtils()
    {
        throw new RuntimeException("You cannot instantiate me");
    }
    
    public static boolean isValidCoOrd(int coordinate)
    {
        return coordinate >= 0 && coordinate < 64;
    }

    private static List<Boolean> inItColumn(int columnNumber) 
    {
        final Boolean[] column = new Boolean[NUM_TILES];
        for(int i = 0; i < column.length; i++) 
        {
            column[i] = false;
        }
        do 
        {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } 
        while(columnNumber < NUM_TILES);
        return ImmutableList.copyOf(column);
    }

     private static List<Boolean> inItRow(int rowNumber) 
     {
        final Boolean[] row = new Boolean[NUM_TILES];
        for(int i = 0; i < row.length; i++) 
        {
            row[i] = false;
        }
        do 
        {
            row[rowNumber] = true;
            rowNumber++;
        } 
        while(rowNumber % NUM_TILES_PER_ROW != 0);
        return ImmutableList.copyOf(row);
    } 
    
    private Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return ImmutableList.copyOf(new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        });
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= START_TILE_INDEX && coordinate < NUM_TILES;
    }

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
    
    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while(currentMove != MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return ImmutableList.copyOf(moveHistory);
    }
    
    public static boolean isEndGame(final Board board) 
    {
        return board.currentPlayer().isInCheckMate();
    }   
}
