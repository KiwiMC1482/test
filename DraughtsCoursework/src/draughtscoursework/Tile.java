package draughtscoursework;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile 
{
    //When constructing a new instance of a tile, it will be assigned a tile coordinate 
    //which is equal to whatever is passed into the constructor
    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    
    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() 
    {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for(int i = 0; i < 64; i++) 
        {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    
    //Creates tiles
    public static Tile createTile(final int tileCoordinates, final Piece piece)
    {
        return piece != null ? new OccupiedTile(tileCoordinates,piece) : EMPTY_TILES_CACHE.get(tileCoordinates);
    }
   
    private Tile(final int tileCoordinate) 
    {
        this.tileCoordinate = tileCoordinate;
    }
    
    //The next few couple of variables and method will tell whther a tile is occupied or empty
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();
    
    public int getTileCoordinate()
    {
        return this.tileCoordinate;
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
