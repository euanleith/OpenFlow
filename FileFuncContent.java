import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class FileFuncContent extends PacketContent {

    private String filename;
    private int size;
    private int dstPort; // port to send to
    private String func; // info on what the node is to do with the packet

    /**
     * Constructor
     *
     * @param filename File name
     * @param size Size of file
     * @param dstPort Destination port
     * @param func Function to be implemented at receiving end
     */
    FileFuncContent(String filename, int size, int dstPort, String func) {
        type= FILEFUNC;
        this.filename = filename;
        this.size = size;
        this.dstPort = dstPort;
        this.func = func;
    }

    /**
     * Constructs an object out of a datagram packet.
     * @param oin Packet that contains information about a file.
     */
    FileFuncContent(ObjectInputStream oin) {
        try {
            type= FILEFUNC;
            filename = oin.readUTF();
            size = oin.readInt();
            dstPort = oin.readInt();
            func = oin.readUTF();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Writes the content into an ObjectOutputStream
     *
     */
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeUTF(filename);
            oout.writeInt(size);
            oout.writeInt(dstPort);
            oout.writeUTF(func);
        }
        catch(Exception e) {e.printStackTrace();}
    }


    /**
     * Returns the content of the packet as String.
     *
     * @return Returns the content of the packet as String.
     */
    public String toString() {
        return "Filename: " + filename +
                " - Size: " + size +
                " - DstPort: " + dstPort +
                " - Func: " + func;
    }

    /**
     * Returns the file name contained in the packet.
     *
     * @return Returns the file name contained in the packet.
     */
    public String getFileName() {
        return filename;
    }

    /**
     * Returns the file size contained in the packet.
     *
     * @return Returns the file size contained in the packet.
     */
    public int getFileSize() {
        return size;
    }

    /**
     * Returns the dstPort contained in the packet.
     *
     * @return Returns the dstPort contained in the packet.
     */
    public int getDstPort() {
        return dstPort;
    }

    /**
     * Returns the function contained in the packet.
     *
     * @return Returns the function contained in the packet.
     */
    public String getFunc() {
        return func;
    }
}
