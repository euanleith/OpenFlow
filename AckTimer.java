import javafx.util.Pair;
import java.net.DatagramPacket;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Acknowledgement thread
 *
 * Checks if the sent packets have been acknowledged by a given time
 * If not, implements 'go back n' ack protocol
 */
public class AckTimer extends Thread {

    private final static int TIMEOUT = 5 * (10 ^ 9);
    private Clock clock;
    private ConcurrentHashMap<Integer, LinkedList<Pair<DatagramPacket, Instant>>> queue; // dstPrt : packet,timeSent
    private Node node;

    public AckTimer(Node node) {
        clock = Clock.systemDefaultZone();
        queue = new ConcurrentHashMap<>();
        this.node = node;
    }

    /**
     * Run on start-up
     * Checks if the sent packets have been acknowledged by a given time
     * If not, implements 'go back n' ack protocol
     */
    public void run() {
        // Endless loop: checking packets have received acknowledgements by a given time
        while (true) {
            //this.sleep(time);//TODO note this is  in milliseconds
            //no need to check each port, only need to check least recently added
            //does sleep stop this from adding new ones?
            //if so, this isn't doable
            //if not, should sleep any time queue is empty
            for (int port : queue.keySet()) {
                queue.computeIfPresent(port, (k, packets) -> {
                    if (!packets.isEmpty() &&
                            clock.instant().getNano() - packets.peek().getValue().getNano() < TIMEOUT) { // reached timeout
                        System.out.println(node.SRC_PORT + ": No ack received from port " + packets.peek().getKey().getPort());
                        for (int i = 0; i < packets.size(); i++) { // resend packets
                            Pair<DatagramPacket, Instant> packet = packets.remove();
                            node.onReceipt(packet.getKey());
                        }
                    }
                    return packets;
                });
            }
        }
    }

    /**
     * Adds a packet to the queue for that packet's destination port
     * Adds port if it isn't already listed
     * @param packet packet to be added to the queue
     */
    public void add(DatagramPacket packet) {
        queue.compute(packet.getPort(), (k, v) -> {
//            System.out.println("adding " + packet.getPort() + " " + clock.instant());
            if (v == null) {
                v = new LinkedList<>();
            }
            v.add(new Pair<>(packet, clock.instant()));
            return v;
        });
    }

    /**
     * Removes the packet from the top of the queue for a given port
     * i.e. removes the least recent packet that was sent to that port
     * that has yet to be acknowledged
     * @param dstPort port of the queue to be removed from
     */
    public void remove(int dstPort) {
        queue.computeIfPresent(dstPort, (k, v) -> {
//            System.out.println("removing " + dstPort + " " + clock.instant());
            if (!v.isEmpty()) {
                v.remove();
            }
            return v;
        });
    }

    /**
     * Adds a port to the list
     * Note: not necessary, but may improve performance
     * @param dstPort port to be added
     */
    public void addPort(int dstPort) {
        queue.computeIfAbsent(dstPort, v -> new LinkedList<>());
    }

    /**
     * Removes a port and any packets in its queue
     * To be called if the port is no longer being sent to
     * @param dstPort port to be removed
     */
    public void removePort(int dstPort) {
        queue.remove(dstPort);
    }
}