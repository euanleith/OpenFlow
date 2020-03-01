//import java.net.DatagramPacket;
//import java.util.ArrayList;
//
//public class Controller extends Node {
//    private static final int DEFAULT_SRC_PORT = 50000;
//
//    private static final int E1_PORT = 50001;
//    private static final int R1_PORT = 50002;
//    private static final int R2_PORT = 50003;
//    private static final int R3_PORT = 50004;
//    private static final int E2_PORT = 50005;
//
//    /**
//     * Constructor
//     *
//     * Attempts to create a socket at a constant port
//     * Initialises queue of packets
//     */
//    public Controller() {
//        super(DEFAULT_SRC_PORT);
//
//        //~temp; predefined route
//        routingTable.put(E1_PORT,E2_PORT,R1_PORT);
//        routingTable.put(R1_PORT,E2_PORT,E2_PORT);
////        routingTable.put(R2_PORT,E2_PORT,R3_PORT);
////        routingTable.put(R3_PORT,E2_PORT,E2_PORT);
//        routingTable.put(E2_PORT,E1_PORT,R1_PORT);
////        routingTable.put(R3_PORT,E1_PORT,R2_PORT);
////        routingTable.put(R2_PORT,E1_PORT,R1_PORT);
//        routingTable.put(R1_PORT,E1_PORT,E1_PORT);
//        System.out.println("\nRouting table:\n" + routingTable.toString());
//
//        listen();
//    }
//
//    public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
//        int srcPort = packet.getPort();
//
//        switch (content.getFunc()) {
//            case HELLO: // Router has said hello
//                //System.out.println("Added " + srcPort);
//                //TODO feature request message? -send router routing table?
//                //-if so, would send all routes for which prevPort is a neighbour to that Router
//                //-then Endpoint should ask for these too?
//                break;
//            case NEW_ROUTE: // Router has requested a route
//                // read file containing prevPort for Router,
//                // return a new file containing prevPort and nextPort to Router
//                ArrayList<String> file = FileFuncs.readFile(content.getFileName());
//                FileFuncs.deleteFile(content.getFileName());
//                if (file != null && file.size() == 1) { // if valid route file
//                    String prevPort = file.get(0);
//                    String nextPort = Integer.toString(routingTable.get(srcPort, content.getPorts()[0]));
//                    FileFuncs.writeToFile(nextPort, new String[]{prevPort,nextPort});
//                    sendFileFunc(nextPort, srcPort, content.getPorts(), NEW_ROUTE);
//                } else {
//                    System.out.println("Received invalid route file");
//                }
//                break;
//            default:
//                System.out.println("Invalid function request");
//                break;
//        }
//    }
//
//    public static void main(String[] args) {
//        try {
//            (new Controller()).start();
//            System.out.println("Program completed");
//        } catch(java.lang.Exception e) {e.printStackTrace();}
//    }
//}
//
