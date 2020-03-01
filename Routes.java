import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

abstract class Routes {

    /**
     * Removes Route with largest dist if there are duplicates in t
     * Removes Route in t if that Route is in p
     * @param t First Route array (tentative)
     * @param p Second Route array (permanent)
     */
    static void removeDups(ArrayList<Route> t, ArrayList<Route> p) {
        for (int i = 0; i < t.size(); i++) {
            for (int j = i+1; j < t.size(); j++) {
                if (t.get(i).port == t.get(j).port) {
                    t.remove(maxDst(t.get(i),t.get(j)));
                }
            }
        }

        //TODO might not need to do this if getting more than just neighbours info?
        //TODO careful...
        for (int i = 0; i < p.size(); i++) {
            for (int j = 0; j < t.size(); j++) {
                if (p.get(i).port == t.get(j).port) {
                    t.remove(j);
                    j--;
                }
            }
        }
    }

    /**
     * Returns the Route of two Route's with the greatest distance
     * @param r1 First Route
     * @param r2 Second Route
     * @return Route with the greatest dist
     */
    static Route maxDst(Route r1, Route r2) {
        return r1.dist > r2.dist ? r1 : r2;
    }

    /**
     * Returns the route with the shortest distance
     * @param routes Array of Route's
     * @return Route with the smallest dist
     */
    static Route getShortest(ArrayList<Route> routes) {
        Route shortest = routes.get(0);//TODO might return this when don't want it to?
        for (Route route : routes) {
            if (route.dist < shortest.dist) {
                shortest = route;
            }
        }
        return shortest;
    }

    /**
     * Converts an array of Route's to a HashMap (i.e. a routing table)
     * @param arr Array of Route's
     * @return HashMap of dstPort : dist,route
     */
    //TODO
    static ConcurrentHashMap<Integer, Pair<Integer, ArrayList<Integer>>> toMap(ArrayList<Route> arr, int srcPort) {
        ConcurrentHashMap<Integer, Pair<Integer, ArrayList<Integer>>> result = new ConcurrentHashMap<>();
        for (Route r : arr) {
            ArrayList<Integer> neighbours = new ArrayList<>();
            Route newNeighbour = r;
            while (newNeighbour.port != srcPort) {
                neighbours.add(newNeighbour.port);
                newNeighbour = get(arr, newNeighbour.neighbour);
            }
            Collections.reverse(neighbours);
            result.put(r.port, new Pair<>(r.dist, neighbours));//TODO want to put endpoints associated with r.port, not r.port
        }
        return result;
    }

    /**
     * Returns the Route for a given port from a Route array
     * @param arr Route array
     * @param port Port of route
     * @return Route with port in arr, null if not present
     */
    static Route get(ArrayList<Route> arr, int port) {
        for (Route r: arr) {
            if (r.port == port) {
                return r;
            }
        }
        return null;
    }
}


