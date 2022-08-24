import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static class Vertex implements Comparable<Vertex> {

        long id;
        long parent;
        double dist;
        double priority;

        Vertex(long id, long parent, double dist, double priority) {
            this.id = id;
            this.parent = parent;
            this.dist = dist;
            this.priority = priority;
        }

        @Override
        public int compareTo(Vertex o) {
            double cmp = priority - o.priority;
            if (cmp + 1e-6 < 0) {
                return -1;
            }
            if (cmp - 1e-6 > 0) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        PriorityQueue<Vertex> fringe = new PriorityQueue<>();
        Map<Long, Double> best = new HashMap<>();
        Map<Long, Long> parent = new HashMap<>();
        for (long v: g.vertices()) {
            best.put(v, Double.MAX_VALUE);
            parent.put(v, -1L);
        }
        long start = g.closest(stlon, stlat);
        long target = g.closest(destlon, destlat);
        fringe.add(new Vertex(start, 0, 0.0, 0.0));
        while (!fringe.isEmpty()) {
            Vertex u = fringe.poll();
            if (parent.get(u.id) == -1) {
                parent.put(u.id, u.parent);
                best.put(u.id, u.dist);
                if (u.id == target) {
                    break;
                }
                for (long v: g.adjacent(u.id)) {
                    if (parent.get(v) == -1) {
                        double newDist = u.dist + g.distance(u.id, v);
                        if (newDist < best.get(v)) {
                            best.put(v, newDist);
                            fringe.add(new Vertex(v, u.id, newDist, newDist + g.distance(v, target)));
                        }
                    }
                }
            }
        }
        LinkedList<Long> l = new LinkedList<>();
        long t = target;
        while (t != start) {
            l.addFirst(t);
            t = parent.get(t);
        }
        l.addFirst(start);
        return l;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> ln = new ArrayList<>();
        double totalDist = 0.0;
        NavigationDirection nd = new NavigationDirection();
        nd.direction = NavigationDirection.START;
        GraphDB.Edge first = g.getEdge(route.get(0), route.get(1));
        nd.way = first.name == null ? NavigationDirection.UNKNOWN_ROAD : first.name;
        totalDist = first.dist;
        for (int i = 1; i < route.size() - 1; i++) {
            GraphDB.Edge newEdge = g.getEdge(route.get(i), route.get(i + 1));
            String newWay = newEdge.name == null ? NavigationDirection.UNKNOWN_ROAD : newEdge.name;
            if (newWay.equals(nd.way)) {
                totalDist += newEdge.dist;
            } else {
                nd.distance = totalDist;
                ln.add(nd);
                nd = new NavigationDirection();
                double prevBearing = g.bearing(route.get(i - 1), route.get(i));
                double curBearing = g.bearing(route.get(i), route.get(i + 1));
                double relBearing = curBearing - prevBearing;
                if (Math.abs(relBearing) < 15) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (relBearing < -15 && relBearing > -30) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (relBearing < 30 && relBearing > 15) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (relBearing < -30 && relBearing > -100) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (relBearing < 100 && relBearing > 30) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (relBearing < -100) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else if (relBearing > 100) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                }
                nd.way = newWay;
                totalDist = newEdge.dist;
            }
        }
        nd.distance = totalDist;
        ln.add(nd);
        return ln;
    }



    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
