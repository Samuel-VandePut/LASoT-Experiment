package be.unamur.game2048.helpers;

import be.unamur.game2048.models.Grid;
import be.unamur.game2048.models.Tile;

import java.util.Arrays;


/**
 * /!\ NE PAS TESTER /!\
 * Cette classe peut être utile pour avoir un code de test plus clair.
 */
public class GridHelper {

    /**
     * La tuile à cette position est-elle égale à une certaine valeur ?
     */
    public static boolean tileEqual(Grid grid, int row, int col, Integer val) {
        return grid.getTile(row, col).equals(new Tile(val));
    }

    /**
     * La ligne (horizontale) est-elle égale à cette série de valeurs ?
     */
    public static boolean rowEqual(Grid grid, int row, Integer[] expectedValues) {
        Tile[] rowTiles = grid.getRow(row);
        return GridHelper.lineEqual(expectedValues, rowTiles);
    }

    /**
     * La colonne (verticale) est-elle égale à cette série de valeurs ?
     */
    public static boolean colEqual(Grid grid, int col, Integer[] expectedValues) {
        Tile[] colTiles = grid.getCol(col);
        return GridHelper.lineEqual(expectedValues, colTiles);
    }

    private static boolean lineEqual(Integer[] expectedValues, Tile[] lineTiles) {
        Object[] expectedTiles = Arrays.stream(expectedValues).map(value -> {
            if (value == null)
                return null;
            return new Tile(value);
        }).toArray();
        return Arrays.equals(expectedTiles, lineTiles);
    }
}
