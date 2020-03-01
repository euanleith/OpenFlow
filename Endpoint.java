import java.net.DatagramPacket;
import java.util.Scanner;

public class Endpoint extends Node {
    private final int DST_PORT;
    private final int USER_PORT = 49999;

    /**
     * Constructor for Endpoint
     * @param srcPort Source port of this node
     * @param dstPort Destination port of router for this node
     */
    Endpoint(int srcPort, int dstPort) {
        super(srcPort);

        this.DST_PORT = dstPort;

        listen();
    }

    public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
        int packetPort = packet.getPort();

        switch (content.getFunc()) {
            case SEND:
                if (packetPort == USER_PORT) {
                    sendFileFunc(content, DST_PORT);
                } else {
                    System.out.println("Received packet!!!");
                }
                break;
            case TEST://TODO RECEIVE?
                //TODO
                break;
            default:
                System.out.println("Invalid function request");
                break;
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("srcPort: ");
        int srcPort = scan.nextInt();

        System.out.println("dstPort: ");
        int dstPort = scan.nextInt();

        try {
            (new Endpoint(srcPort, dstPort)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
