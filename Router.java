import javafx.util.Pair;

import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Router extends Node {

    private ConcurrentLinkedQueue<DatagramPacket> queue; // packets to be sent once a route is found

    private ConcurrentHashMap<Integer, HashMap<Integer, Integer>> portToNeighbours; // port : neighbours,dist
    private ArrayList<Integer> routers;

    private ConcurrentHashMap<Integer, Pair<Integer, ArrayList<Integer>>> routingTable; // dstPort : dist,route

    /**
     * Constructor for Router
     * @param srcPort Source port of this node
     * @param neighbours Neighbouring Router's of this node
     * @param routers All Router's in the network
     */
    Router(int srcPort, int[] neighbours, int[] routers) {
        super(srcPort);

        queue = new ConcurrentLinkedQueue<>();

        routingTable = new ConcurrentHashMap<>();

        this.routers = Main.toArrayList(routers);

        // init portToNeighbours with routerPort : <empty> for all routers in network
        portToNeighbours = new ConcurrentHashMap<>();
//        for (int routerPort : routers) {
//            portToNeighbours.put(routerPort, new HashMap<>());
//        }

        // add info for this router
        portToNeighbours.put(SRC_PORT, new HashMap<>());
        for (int neighbourPort : neighbours) {
            portToNeighbours.get(SRC_PORT).put(neighbourPort, 1);
        }

        routingTable.put(SRC_PORT, new Pair<>(0, new ArrayList<>()));

        listen();
    }

    @Override
    public void processLinkState(DatagramPacket packet, LinkStateContent content) {
        ConcurrentHashMap<Integer, HashMap<Integer, Integer>> newPortToNeighbours = content.getNeighbours();
        if (newPortToNeighbours != null) {
            for (Map.Entry<Integer, HashMap<Integer, Integer>> port : newPortToNeighbours.entrySet()) {
                if (!portToNeighbours.containsKey(port.getKey())) {
                    portToNeighbours.put(port.getKey(), new HashMap<>());
                }
                if (portToNeighbours.get(port.getKey()).isEmpty() && !port.getValue().isEmpty()) {
                    for (int newNeighbour : port.getValue().keySet()) {
                        portToNeighbours.get(port.getKey()).put(newNeighbour, 1);
                    }
                }
            }
        }

        // if received information about all routers in the system
//        if (!Main.isEmpty(portToNeighbours.values())) {//TODO only want to do this if it changes??
            route();
//        }
    }

    public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
        int packetPort = packet.getPort();

        switch (content.getFunc()) {
            case SEND_INFO: // send info about neighbours to all other routers in the network
//                for (int port : portToNeighbours.keySet()) {
                for (int port : routers) {
                    if (port != SRC_PORT) { // don't want to send to self
                        sendLinkState(portToNeighbours, port);
                    }
                }
                break;
            default:
                if (routingTable.size() < routers.size() || routingTable.isEmpty()) { // if don't know route yet
                    System.out.println(SRC_PORT + ": Adding packet " + content.toString() + " to queue");
                    queue.add(packet);
                } else {
                    int dstPort = content.getDstPort();
                    if (dstPort == SRC_PORT) { // if this is dst
                        System.out.println(SRC_PORT + ": Received packet!");
                        /*
                        Process packet
                         */
                    } else { // else forward
                        int nextPort = routingTable.get(dstPort)
                                .getValue() // get route
                                .get(0); // get next port in route
                        sendFileFunc(content, nextPort);
                    }
                }
                break;
        }
    }

    /**
     * Form a routing table using Dijkstra's Algorithm
     */
    private void route() {
        ArrayList<Route> tentative = new ArrayList<>();
        ArrayList<Route> permanent = new ArrayList<>();

        Route n = new Route(SRC_PORT, 0, -1);
        permanent.add(n);

        while (permanent.size() < portToNeighbours.size()) { // while not every node is permanent

            // format n's neighbours and add them to tentative
            if (portToNeighbours.containsKey(n.port)) {
                for (Map.Entry<Integer, Integer> r : portToNeighbours.get(n.port).entrySet()) {
                    tentative.add(new Route(r.getKey(), r.getValue() + n.dist, n.port));
                }

                Routes.removeDups(tentative, permanent);
                if (!tentative.isEmpty()) {
                    n = Routes.getShortest(tentative);
                    tentative.remove(n);
                    permanent.add(n);
                }
            }
        }

        routingTable = Routes.toMap(permanent, SRC_PORT);
        System.out.println(SRC_PORT + ": Constructed routing table: " + routingTable.toString());

        for (DatagramPacket packet : queue) {
            processFileFunc(packet,(FileFuncContent)PacketContent.fromDatagramPacket(packet));
        }
    }
}
