package be.unamur.game2048;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import be.unamur.game2048.controllers.*;
import be.unamur.game2048.helpers.*;
import be.unamur.game2048.models.*;

public class Test2048 {

    /* TEST CONTROLLER */

    @Test
    public void testStartGameFirstTileNotNull(){
        // --- Initialize game
        GameController controller = new GameController();
        controller.startGame();
        Grid grid = controller.getGrid();

        Assert.assertNotNull(grid.getTile(0));
    }
    
    @Test
    public void testStartGameNumberOfTileAddedEqualsNbStartTileFilledGameParam(){
        // --- Initialize game 
        GameController controller = new GameController();
        controller.startGame();
        Grid grid = controller.getGrid();
        int count = 0;
        for(int row = 0; row < GameParams.sideLength; row++){
            for(int col = 0; col < GameParams.sideLength; col++){
                if(grid.getTile(row,col) != null){
                    count++;
                }
            }
        }

        // --- Assert
        Assert.assertEquals(GameParams.nbStartTileFilled, count);//Peut essayer un assertTrue(count <= GameParams.nbStartTileFilled)
    }
    
    @Test
    public void testFillFirstEmptyTileFalseOnFullGrid(){
        // --- Initialize full grid 
        Tile[][] tiles = new Tile[GameParams.sideLength][GameParams.sideLength];
        for(int pos = 0; pos < GameParams.sideLength * GameParams.sideLength; pos++){
            int row = pos / GameParams.sideLength;
            int col = pos % GameParams.sideLength;
            tiles[row][col] = new Tile(2);
        }
        GameController controller = new GameController();
        
        // --- Start game
        controller.startGame(tiles);

        // --- Assert controller cannot add more tile
        Assert.assertFalse(controller.fillFirstEmptyTile());
    }

    
    @Test
    public void testNoMoveAvailableOnFullGridWithNoMergingPossibility(){
        // --- Initialize full grid 
        Tile[][] tiles = {{new Tile(2),new Tile(4),new Tile(8),new Tile(16)},
                          {new Tile(32),new Tile(64),new Tile(128),new Tile(256)},
                          {new Tile(2),new Tile(4),new Tile(8),new Tile(16)},
                          {new Tile(32),new Tile(64),new Tile(128),new Tile(256)},};
        GameController controller = new GameController();
        
        // --- Start game
        controller.startGame(tiles);

        // --- Assert no move available
        Assert.assertFalse(controller.moveUp(true) 
                        || controller.moveRight(true) 
                        || controller.moveDown(true) 
                        || controller.moveLeft(true));
    }
    
    @Test
    public void testMoveAvailableOnFullGridWithMergingPossibility(){
        // --- Initialize full grid 
        Tile[][] tiles = {{new Tile(2),new Tile(4),new Tile(8),new Tile(16)},
                          {new Tile(32),new Tile(64),new Tile(64),new Tile(256)},
                          {new Tile(4),new Tile(4),new Tile(128),new Tile(16)},
                          {new Tile(32),new Tile(64),new Tile(128),new Tile(256)}};
        GameController controller = new GameController();
        
        // --- Start game
        controller.startGame(tiles);

        // --- Assert move available
        Assert.assertTrue(controller.moveUp(true) 
                        || controller.moveRight(true) 
                        || controller.moveDown(true) 
                        || controller.moveLeft(true));
    }
    
    @Test
    public void testMoveAvailableOnNonFullGrid(){
        // --- Initialize full grid 
        Tile[][] tiles = {{new Tile(2),new Tile(4),new Tile(8),new Tile(16)},
                          {new Tile(32),new Tile(64),null,new Tile(256)},
                          {null,new Tile(4),new Tile(8),new Tile(16)},
                          {new Tile(32),new Tile(64),new Tile(128),new Tile(256)}};
                          
        GameController controller = new GameController();
        
        // --- Start game
        controller.startGame(tiles);

        // --- Assert move available
        Assert.assertTrue(controller.moveUp(true) 
                        || controller.moveRight(true)
                        || controller.moveDown(true)
                        || controller.moveLeft(true));
    }

    /* TEST TILE */

    @Test 
    public void testCanMergeTileWithTwoSameTileValue() {
        // --- Initialize
        Tile tile1 = new Tile(2);
        Tile tile2 = new Tile(2);

        // --- Assert 
        Assert.assertTrue(tile1.canMergeWith(tile2));
    }

    @Test 
    public void testMergeWith() {
        // --- Initialize tiles
        Tile tile1 = new Tile(2);
        Tile tile2 = new Tile(2);

        // --- Act
        int result = tile1.mergeWith(tile2);

        // --- Assert
        Assert.assertTrue(result > 0);
    }

    @Test 
    public void testMergeWithResultOnImpossibleMerging() {
        // --- Initialize tiles
        Tile tile1 = new Tile(2);
        Tile tile2 = new Tile(4);

        // --- Act
        int result = tile1.mergeWith(tile2);
        
        // --- Assert
        Assert.assertTrue(result >= -1);
    }

    /* TEST GRID */

    @Test 
    public void testGetTileWithPosition() {
        // --- Initialize one tile grid 
        Tile tile = new Tile(256);
        Tile[][] tiles = {{null,null,null,null},
                          {null,null,null,tile},
                          {null,null,null,null},
                          {null,null,null,null}};
        
        // --- Act
        Grid grid = new Grid(tiles);
        Tile t = grid.getTile(7);

        // --- Assert
        Assert.assertEquals(tile,t);
    }

    @Test 
    public void testGetTileWithRowAndCol() {
        // --- Initialize one tile grid 
        Tile tile = new Tile(256);
        Tile[][] tiles = {{null,null,null,null},
                          {null,null,null,tile},
                          {null,null,null,null},
                          {null,null,null,null}};
        
        // --- Act
        Grid grid = new Grid(tiles);
        Tile t = grid.getTile(1,3);

        // --- Assert
        Assert.assertEquals(tile, t);
    }
    
    @Test 
    public void testSetTile() {
        // --- Initialize one tile grid 
        Tile tile = new Tile(256);
        Tile[][] tiles = {{null,null,null,null},
                          {null,null,null,null},
                          {null,null,null,null},
                          {null,null,null,null}};
        
        // --- Act
        Grid grid = new Grid(tiles);
        grid.setTile(7,tile);

        // --- Assert
        Assert.assertEquals(tile, grid.getTile(7));
    }

    @Test 
    public void testGetCol() {
        // --- Initialize one tile grid 
        Tile[] tileCol = {new Tile(2), new Tile(4), new Tile(8), new Tile(16)};
        Tile[][] tiles = {{tileCol[0],null,null,null},
                          {tileCol[1],null,null,null},
                          {tileCol[2],null,null,null},
                          {tileCol[3],null,null,null}};
        
        // --- Act
        Grid grid = new Grid(tiles);
        Tile[] col = grid.getCol(0);

        // --- Assert
        Assert.assertNotNull(col);
    }

    @Test 
    public void testTileEquals() {
        // --- Initialize one tile grid 
        Tile tile1 = new Tile(256);
        Tile tile2 = new Tile(256);

        // --- Assert
        Assert.assertEquals(tile1, tile2);
    }
}
