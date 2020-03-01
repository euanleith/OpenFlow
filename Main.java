import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/*
At the start of an execution, the controller will be started and wait for connections from routers.
Once routers are started, they will contact the controller with a Hello message. The controller
should respond to this message with its own Hello message, followed by a FeatureRequest
message, etc. When a router receives a packet from an endpoint, it should try to route the
packet depending on its flow table. If an incoming packet from an endpoint does not match an
entry in the flow table, a router should contact the controller and ask how to proceed. The
controller should have preconfigured (hardcoded) routing information and should inform all
routers along a path, when it is contacted about a packet entering its network. For example if
E1 would send a packet Pa1 addressed to E4 to R1, R1 would contact the controller, which then
would look up how to get from R1 to E4 and send configurations to R2, R4 and R1 about a new
flow. Subsequent packets from E1 to E4 should not require any messages between the routers
and the controller.

-routers contact controller, controller responds with hello and 'FeatureRequest'
-router forwards packet according to its routing table, if it doesn't match table, contact controller

TODO;
 AckTimer: messes up when User requests 'send' with dstEndpoint not yet started, then started
 AckTimer: messes up with multiple packets in the queue
 see 'Assignments' slides
 method descriptions
 */

public class Main {

    public static int[] delimStringToIntArray(String str, String delimiter) {
        String[] strArr = str.split(delimiter);
        int[] result = new int[strArr.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strArr[i]);
        }
        return result;
    }

    public static String[] toStrArr(Object[] arr) {
        String[] result = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = String.valueOf(arr[i]);
        }
        return result;
    }

    // returns true if any map in a list of maps is empty
    static boolean isEmpty(Collection<HashMap<Integer, Integer>> maps) {//TODO
        boolean empty = false;
        for (HashMap map : maps) {
            if (map.isEmpty()) {
                empty = true;
            }
        }
        return empty;
    }

    // convert Set<Integer> to int[]
    static int[] toInt(Set<Integer> set) {
        Integer[] arr = set.toArray(new Integer[0]);
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    static ArrayList<Integer> toArrayList(int[] arr) {
        ArrayList<Integer> result = new ArrayList<>(arr.length);
        for (int num : arr) {
            result.add(num);
        }
        return result;
    }
}
/*
TODO
Router:

public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
        int srcPort = packet.getPort();
        int dstPort = -1;
        if (content.getPorts() != null && content.getPorts().length == 1) {//~
            dstPort = content.getPorts()[0];
        }

        //~would be nice if this was switch(srcPort), but cant have case routingTable.containsKey(srcPort)...
        if (routingTable.containsKey(srcPort, dstPort)) {
            // forward packet to the appropriate node
            int nextPort = routingTable.get(srcPort, dstPort);
            sendFileFunc(content, nextPort);
        } else {
                switch (srcPort) {
                    case CONTROLLER_PORT:
                    case USER_PORT:
                        switch (content.getFunc()) {
                            case NEW_ROUTE: // Controller has returned new route
                                // read file containing prevPort and nextPort,
                                // add a new route 'dstPort,prevPort:nextPort' to routing table,
                                // forward the packet to nextPort
                                //TODO change
                                ArrayList<String> file = Main.readFile(content.getFileName());
                                Main.deleteFile(content.getFileName());
                                if (file != null && file.size() == 2) { // if valid route file
                                    routingTable.put(Integer.parseInt(file.get(0)), dstPort,
                                            Integer.parseInt(file.get(1)));
                                    sendFileFunc(queue.remove(), Integer.parseInt(file.get(1)));
                                } else {
                                    System.out.print("Received invalid route file: ");
                                    //~send something to controller?
                                }
                                break;
                            case HELLO: // send hello to Controller
                                sendFileFunc(content, CONTROLLER_PORT);
                                break;
                            default:
                                System.out.println("Invalid function request");
                        }
                        break;
                    default: // unknown route
                        // ask Controller for route
                        Main.writeToFile(Integer.toString(srcPort),
                                new String[]{Integer.toString(srcPort)});
                        sendFileFunc(Integer.toString(srcPort), CONTROLLER_PORT, content.getPorts(), NEW_ROUTE);
                        queue.add(content);
                }
        }
    }

    Endpoint:

    import java.net.DatagramPacket;
import java.util.Scanner;

public class Endpoint extends Node {
    private int srcPort;
    private int dstRouter //TODO

    Endpoint(int srcPort) {
        super(srcPort);
        this.srcPort = srcPort;
        routingTable.put(50001,50005,50002);
        routingTable.put(50005,50001,50002);

        listen();//TODO
    }

    public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
        switch (content.getFunc()) {
            case SEND:
                if (srcPort != content.getPorts()[0]) {//~don't want to resend if you're the one receiving
                    int nextPort = routingTable.get(srcPort, content.getPorts()[0]);
                    sendFileFunc(content, nextPort);//~temp;should send to another endpoint, but somehow i route this through routers
                }
                break;
            default:
                System.out.println("Invalid function request");
                break;
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("port: ");
        try {
            (new Endpoint(scan.nextInt())).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}

 */