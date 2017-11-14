/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draughtscoursework;

// BoardUtils is a series of variables and methods which are used in many different places so instead of 
//constanlty typing them locally in other classes it is easier to have them in one place and call them from here
public class BoardUtils
{
    public static final boolean[] FIRST_COLUMN = inItColumn(0); 
    public static final boolean[] SECOND_COLUMN = inItColumn(1); 
    public static final boolean[] SEVENTH_COLUMN = inItColumn(6); 
    public static final boolean[] EIGHTH_COLUMN = inItColumn(7); 
    
    public static final boolean[] EIGHTH_ROW = inItRow(0);
    public static final boolean[] SEVENTH_ROW = inItRow(8);
    public static final boolean[] SIXTH_ROW = inItRow(16);
    public static final boolean[] FIFTH_ROW = inItRow(24);
    public static final boolean[] FOURTH_ROW = inItRow(32);
    public static final boolean[] THIRD_ROW = inItRow(40);
    public static final boolean[] SECOND_ROW = inItRow(48);
    public static final boolean[] FIRST_ROW = inItRow(56);
    
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
    
    private static boolean[] inItColumn(int columnNumber)
    {
        final boolean[] column = new boolean[NUM_TILES];
        
        do
        { 
         column[columnNumber] = true;
         columnNumber += NUM_TILES_PER_ROW;
        }
        while (columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] inItRow(int rowNumber)
    {
        final boolean[] row = new boolean[NUM_TILES];
        
        do
        { 
         row[rowNumber] = true;
         rowNumber ++;
        }
        while (rowNumber % NUM_TILES_PER_ROW !=0);
        return row;
    }       
}
