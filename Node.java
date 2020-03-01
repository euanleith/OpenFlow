import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Node {
    private static final String DEFAULT_DST_NODE = "localhost";

    // Packet functions
    final static String HELLO = "hello";
    final static String NEW_ROUTE = "route";
    final static String SEND = "send";
    final static String SEND_INFO = "send info";
    final static String TEST = "test";

    final static int[] NO_PORTS = new int[0];

    private AckTimer timer;
    private Listener listener;
//    TwoKeyMap<Integer, Integer, Integer> routingTable; // TODO prevPort,dstPort : nextPort
    private DatagramSocket socket;

    final int SRC_PORT;

    /**
     * Constructor for Node
     * Initialises and starts the AckTimer,
     * and attempts to create a socket at the given port
     * @param srcPort Source port of this node
     */
    Node(int srcPort) {
        this.SRC_PORT = srcPort;

        timer = new AckTimer(this);
        timer.start();

//        routingTable = new TwoKeyMap<>();

        try {
            socket= new DatagramSocket(SRC_PORT);
        }
        catch(java.lang.Exception e) {e.printStackTrace();}
    }

    /**
     * Start listening for incoming packets
     */
    void listen() {
        listener = new Listener(this);
        listener.setDaemon(true);
        listener.start();
        listener.go();
    }

    /**
     * Listen for incoming packets
     * @throws InterruptedException if thread is interrupted for any reason
     */
    public synchronized void start() throws InterruptedException {
        System.out.println(SRC_PORT + ": Waiting for contact");
        this.wait();
    }


    /**
     * Process incoming packet
     * @param packet A packet that has been received
     */
    void onReceipt(DatagramPacket packet) {
        try {

            PacketContent content = PacketContent.fromDatagramPacket(packet);

            switch (content.getType()) {
                case PacketContent.FILEFUNC:
                    sendAck(packet);
                    System.out.println(SRC_PORT + ": Processing packet: " + content.toString());
                    processFileFunc(packet, (FileFuncContent)content);
                    break;
                case PacketContent.LINKSTATE:
                    sendAck(packet);
                    System.out.println(SRC_PORT + ": Processing packet: " + content.toString());
                    processLinkState(packet, (LinkStateContent)content);
                    break;
                case PacketContent.ACKPACKET:
                    System.out.println(SRC_PORT + ": Received ack from " + packet.getPort() + ": " + content.toString());
                    timer.remove(packet.getPort());
                    break;
                default:
                    sendAck(packet, "Received invalid packet type");
                    System.out.println(SRC_PORT + ": Received invalid packet type");
            }
        } catch(Exception e) {e.printStackTrace(); }
    }

    /**
     * Process packet of type FileFuncContent
     * Note that packet and content are equivalent, and are separated for convenience
     * @param packet DatagramPacket to be processed
     * @param content FileFuncContent to be processed
     */
    public abstract void processFileFunc(DatagramPacket packet, FileFuncContent content);

    /**
     * Process packet of type LinkStateContent
     * Note that packet and content are equivalent, and are separated for convenience
     * @param packet DatagramPacket to be processed
     * @param content LinkStateContent to be processed
     */
    void processLinkState(DatagramPacket packet, LinkStateContent content) {

    }


    /**
     * Send packet containing acknowledgement to ~whoever just send the packet
     * @param packet Packet to be acknowledged
     * @return AckPacketContent sent
     */
    AckPacketContent sendAck(DatagramPacket packet) {
        try {
            AckPacketContent ack = new AckPacketContent();
            DatagramPacket response = ack.toDatagramPacket();
            response.setSocketAddress(packet.getSocketAddress());
            socket.send(response);
            return ack;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    /**
     * Send packet containing given acknowledgement to ~whoever just send the packet
     * @param packet Packet to be acknowledged
     * @param msg Acknowledgment message
     * @return AckPacketContent sent
     */
    AckPacketContent sendAck(DatagramPacket packet, String msg) {
        try {
            AckPacketContent ack = new AckPacketContent(msg);
            DatagramPacket response = ack.toDatagramPacket();
            response.setSocketAddress(packet.getSocketAddress());
            socket.send(response);
            return ack;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    /**
     * Send packet containing file and function which is to be forwarded to given port
     * @param fname File name
     * @param nextPort Next port to send to
     * @param dstPort Destination port
     * @param func Function to be implemented at roots
     * @return FileFuncContent sent
     */
    FileFuncContent sendFileFunc(String fname, int nextPort, int dstPort, String func) {
        try {
            File file = new File(fname);
            int size = (int) file.length();

            FileFuncContent content = new FileFuncContent(fname, size, dstPort, func);

            return sendFileFunc(content, nextPort);
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    /**
     * Send packet containing file and function which is to be forwarded to given port
     * @param content FileFuncContent to be sent
     * @param nextPort Next port to send to
     * @return FileFuncContent sent
     */
    FileFuncContent sendFileFunc(FileFuncContent content, int nextPort) {
        try {
            System.out.println(SRC_PORT + ": Sending packet to " + nextPort + ": " + content.toString());
            DatagramPacket packet = content.toDatagramPacket();
            InetSocketAddress dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, nextPort);
            packet.setSocketAddress(dstAddress);
            socket.send(packet);
            timer.add(packet);
            return content;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    LinkStateContent sendLinkState(ConcurrentHashMap<Integer, HashMap<Integer, Integer>> neighbours, int dstPort) {
        LinkStateContent content = new LinkStateContent(neighbours);
        return sendLinkState(content, dstPort);
    }

    LinkStateContent sendLinkState(LinkStateContent content, int dstPort) {
        try {
            System.out.println(SRC_PORT + ": Sending packet to " + dstPort + ": " + content.toString());
            DatagramPacket packet = content.toDatagramPacket();
            InetSocketAddress dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, dstPort);
            packet.setSocketAddress(dstAddress);
            socket.send(packet);
            timer.add(packet);
            return content;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    DatagramSocket getSocket() {
        return socket;
    }
}