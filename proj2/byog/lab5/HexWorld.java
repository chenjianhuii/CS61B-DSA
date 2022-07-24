package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static int xStart(int x, int size) {
        return x + size - 1;
    }

    private static int yStart(int y, int size) {
        return y + size << 1;
    }

    private static void addRow(int x, int y, int s, TETile t, TETile[][] world) {
        for (int i = 0; i < s; i++) {
            world[x + i][y] = t;
        }
    }

    private static void addCol(int x, int y, int s, TETile[][] world, int num) {
        for (int i = 0; i < num; i++) {
            TETile t = randomTile();
            addHexagon(s, x, y + 2 * i * s, t, world);
        }
    }

    private static TETile randomTile() {
        Random r = new Random();
        int tileNum = r.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.MOUNTAIN;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WATER;
            case 4: return Tileset.SAND;
            default: return Tileset.TREE;
        }
    }

    private static void addHexagon(int size, int x, int y, TETile t, TETile[][] world) {
        int xs = xStart(x, size);
        for (int i = 0; i < size * 2; i++) {
            if (i < size) {
                addRow(xs - i, y + i, size + 2 * i, t, world);
            } else {
                addRow(xs + i + 1 - 2 * size, y + i, 5 * size - 2 - 2 * i, t, world);
            }
        }
    }

    private static void fillAll(int width, int height, TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static void fillTesselation(int x, int y, int s, TETile[][] world) {
        int ys = yStart(y, s);
        for (int i = 0; i < 5; i++) {
            if (i < 3) {
                addCol(x + i * (2 * s - 1), ys - i * s, s, world, i + 3);
            } else {
                addCol(x + i * (2 * s - 1), ys - (4 - i) * s, s, world, 7 - i);
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT, 0, 3);
        TETile[][] hexTiles = new TETile[WIDTH][HEIGHT];
        fillAll(WIDTH, HEIGHT, hexTiles);
        fillTesselation(0, 0, Integer.parseInt(args[0]), hexTiles);
        ter.renderFrame(hexTiles);
    }
}
