import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        int level = getLevel(params);
        double latPerTile = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / (1 << level);
        double lonPerTile = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (1 << level);
        int xMin = findLeftBound(params.get("ullon"), MapServer.ROOT_ULLON, MapServer.ROOT_LRLON, level);
        int yMin = findLeftBound(params.get("ullat"), MapServer.ROOT_ULLAT, MapServer.ROOT_LRLAT, level);
        int xMax = findLeftBound(params.get("lrlon"), MapServer.ROOT_ULLON, MapServer.ROOT_LRLON, level);
        int yMax = findLeftBound(params.get("lrlat"), MapServer.ROOT_ULLAT, MapServer.ROOT_LRLAT, level);
        String[][] renderGrid = new String[yMax - yMin + 1][xMax - xMin + 1];
        boolean success = false;
        for (int i = 0; i < renderGrid.length; i++) {
            for (int j = 0; j < renderGrid[0].length; j++) {
                success = true;
                renderGrid[i][j] = String.format("d%d_x%d_y%d.png", level, j + xMin, i + yMin);
            }
        }
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + xMin * lonPerTile);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - yMin * latPerTile);
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (xMax + 1) * lonPerTile);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - (yMax + 1) * latPerTile);
        results.put("depth", level);
        results.put("query_success", success);
        return results;
    }


    private int getLevel(Map<String, Double> params) {
        double targetLonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        double rootLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        int level = 0;
        while (level < 7 && rootLonDPP / (1 << level) - targetLonDPP > 1e-10) {
            level++;
        }
        return level;
    }

    private int findLeftBound(double ul, double rootL, double rootR, int level) {
        int tileNum = 1 << level;
        double fraction = (ul - rootL) / (rootR - rootL);
        if (fraction < 0) {
            return 0;
        }
        if (fraction > 1) {
            return tileNum - 1;
        }
        return (int) (tileNum * fraction);
    }
}
