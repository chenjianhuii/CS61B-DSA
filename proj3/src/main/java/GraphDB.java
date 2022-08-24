import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    public static class Node {
        long id;
        double lat, lon;
        String name;
        List<Edge> adj;
        Node(long id, double lon, double lat) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = "";
            adj = new ArrayList<>();
        }

        @Override
        public int hashCode() {
            return (int) (id & 0x7fffffff);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            }
            Node other = (Node) o;
            return this.id == other.id;
        }

        Node(long id, double lon, double lat, String name) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = name;
            adj = new ArrayList<>();
        }
    }

    public static class Edge {
        double dist;
        long u, v;
        String name;
        Edge(double dist, long u, long v) {
            this.dist = dist;
            this.u = u;
            this.v = v;
            this.name = null;
        }
        Edge(double dist, long u, long v, String name) {
            this.dist = dist;
            this.u = u;
            this.v = v;
            this.name = name;
        }
    }

    private Map<Long, Node> vertices = new ConcurrentHashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (Node n: vertices.values()) {
            if (n.adj.isEmpty()) {
                removeNode(n.id);
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        List<Long> adjs = new ArrayList<>();
        for (Edge e: vertices.get(v).adj) {
            if (e.u != v) {
                adjs.add(e.u);
            }
            if (e.v != v) {
                adjs.add(e.v);
            }
        }
        return adjs;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestNode = -1;
        double closestDist = Double.MAX_VALUE;
        for (Node n: vertices.values()) {
            if (closestNode == -1) {
                closestNode = n.id;
            } else {
                double d = distance(lon(n.id), lat(n.id), lon, lat);
                if (d < closestDist) {
                    closestNode = n.id;
                    closestDist = d;
                }
            }
        }
        return closestNode;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertices.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertices.get(v).lat;
    }

    void addNode(Node v) {
        if (!vertices.containsKey(v.id)) {
            vertices.put(v.id, v);
        }
    }

    void addEdge(long u, long v){
        if (vertices.containsKey(u) && vertices.containsKey(v)) {
            Edge e = new Edge(distance(u, v), u, v);
            vertices.get(u).adj.add(e);
            vertices.get(v).adj.add(e);
        }
    }
    void addEdge(long u, long v, String name){
        if (vertices.containsKey(u) && vertices.containsKey(v)) {
            Edge e = new Edge(distance(u, v), u, v, name);
            vertices.get(u).adj.add(e);
            vertices.get(v).adj.add(e);
        }
    }

    void removeNode(long v) {
        for (long u: adjacent(v)) {
            for (Edge e: vertices.get(u).adj) {
                if (e.u == v || e.v == v) {
                    vertices.get(u).adj.remove(e);
                    break;
                }
            }
        }
        vertices.remove(v);
    }

    Edge getEdge(long u, long v) {
        for (Edge e: vertices.get(u).adj) {
            if (e.u == v || e.v == v) {
                return e;
            }
        }
        return null;
    }
}
