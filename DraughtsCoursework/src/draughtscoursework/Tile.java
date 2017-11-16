package draughtscoursework;

import draughtscoursework.Alliance;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile 
{
    //When constructing a new instance of a tile, it will be assigned a tile coordinate 
    //which is equal to whatever is passed into the constructor
    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
    private static final Table<Integer, Piece, OccupiedTile> OCCUPIED_TILES = createAllPossibleOccupiedTiles();
    
    private Tile(final int coordinate) {
        this.tileCoordinate = coordinate;
    }

    //The next few couple of variables and method will tell whther a tile is occupied or empty
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    //Creates tiles
    static Tile createTile(final int coordinate, final Piece piece) 
    {

        if(piece == null) 
        {
            return EMPTY_TILES.get(coordinate);
        }

        final OccupiedTile cachedOccupiedTile = OCCUPIED_TILES.get(coordinate, piece);

        if(cachedOccupiedTile != null) 
        {
            return cachedOccupiedTile;
        }

        return new OccupiedTile(coordinate, piece);
    }
    
    public int getTileCoordinate()
    {
        return this.tileCoordinate;
    }
    
    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() 
    {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for(int i = 0; i < 64; i++) 
        {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    
    private static Table<Integer, Piece, OccupiedTile> createAllPossibleOccupiedTiles()
    {
        final Table<Integer, Piece, OccupiedTile> occupiedTileTable = HashBasedTable.create();

        for (final Alliance alliance : Alliance.values()) 
        {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++)
            {
                final Checker whiteCheckerFirstMove = new Checker(alliance, i);
                final Checker whiteCheckerMoved = new Checker(alliance, i);
                occupiedTileTable.put(i, whiteCheckerFirstMove, new OccupiedTile(i, whiteCheckerFirstMove));
                occupiedTileTable.put(i, whiteCheckerMoved, new OccupiedTile(i, whiteCheckerMoved));
            }
            
            for (int i = 0; i < BoardUtils.NUM_TILES; i++)
            {
                final King whiteKingFirstMove = new King(alliance,i);
                final King whiteKingMoved = new King(alliance, i);
                occupiedTileTable.put(i, whiteKingFirstMove, new OccupiedTile(i, whiteKingFirstMove));
                occupiedTileTable.put(i, whiteKingMoved, new OccupiedTile(i, whiteKingMoved));
            }
        }
        return ImmutableTable.copyOf(occupiedTileTable);
    }
    
    public static final class EmptyTile extends Tile //Empty Tile Method
    {
        private EmptyTile(final int coordinate) 
        {
            super(coordinate);
        }
        
        @Override
        public String toString()
        {
           return "-"; 
        }

        @Override
        public boolean isTileOccupied() 
        {
            return false;
        }

        public Piece getPiece() 
        {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile //Occupied Tile Method
    {
        private final Piece pieceOnTile;

        private OccupiedTile(int tileCoordinate,final Piece pieceOnTile) 
        {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        
        @Override
        public String toString()
        {
           return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString(); 
        }
        
        
        @Override
        public boolean isTileOccupied() 
        {
            return true;
        }

        public Piece getPiece() 
        {
            return this.pieceOnTile;
        }   
    }
}
