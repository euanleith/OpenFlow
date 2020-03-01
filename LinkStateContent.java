import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

class LinkStateContent extends PacketContent {

    private ConcurrentHashMap<Integer, HashMap<Integer, Integer>> portToNeighbours; // port : neighbour,dist

    /**
     * Constructor
     *
     * @param portToNeighbours map of each port to its neighbours and the distances to them
     */
    LinkStateContent(ConcurrentHashMap<Integer, HashMap<Integer, Integer>> portToNeighbours) {
        type= LINKSTATE;
        this.portToNeighbours = portToNeighbours;
    }

    /**
     * Constructs an object out of a datagram packet.
     * @param oin Packet that contains information about a file.
     */
    LinkStateContent(ObjectInputStream oin) {
        try {
            type= LINKSTATE;
            portToNeighbours = (ConcurrentHashMap<Integer, HashMap<Integer, Integer>>) oin.readObject();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Writes the content into an ObjectOutputStream
     *
     */
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeObject(portToNeighbours);
        }
        catch(Exception e) {e.printStackTrace();}
    }


    /**
     * Returns the content of the packet as String.
     *
     * @return Returns the content of the packet as String.
     */
    public String toString() {
        return "portToNeighbours: " + portToNeighbours.toString();
    }

    /**
     * Returns the portToNeighbours contained in the packet.
     *
     * @return Returns the portToNeighbours contained in the packet.
     */
    public ConcurrentHashMap<Integer, HashMap<Integer, Integer>> getNeighbours() {
        return portToNeighbours;
    }
}
