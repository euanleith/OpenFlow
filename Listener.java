import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

/**
 *
 * Listener thread
 *
 * Listens for incoming packets on a datagram socket and informs registered receivers about incoming packets.
 */
public class Listener extends Thread {

    private static final int PACKET_SIZE = 65536;
    private CountDownLatch latch;
    private Node node;
    private DatagramSocket socket;

    public Listener(Node node) {
        latch = new CountDownLatch(1);
        this.node = node;
        socket = node.getSocket();
    }

    /*
     *  Telling the listener that the socket has been initialized
     */
    public void go() {
        latch.countDown();
    }

    /*
     * Listen for incoming packets and inform receivers
     */
    public void run() {
        latch.countDown();//TODO
        try {
            latch.await();
            // Endless loop: attempt to receive packet, notify receivers, etc
            while(true) {
                DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                socket.receive(packet);

                node.onReceipt(packet);
            }
        } catch (SocketException e) {
        } catch (Exception e) {e.printStackTrace();}
    }
}