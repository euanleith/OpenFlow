import java.net.DatagramPacket;
import java.util.Scanner;

public class User extends Node {
    String input;

    private static final int DEFAULT_SRC_PORT = 49999;

    User(String input) {
        super(DEFAULT_SRC_PORT);
        this.input = input;
        listen();
    }

    @Override
    public void start() {
        Scanner scan = new Scanner(input);
        while (true) {
//            System.out.print("input: ");
            String input = scan.next();
            switch (input) {
                case "Endpoint":
//                    System.out.print("srcPort and dstPort: ");
                    int srcPort = scan.nextInt();
                    int dstPort = scan.nextInt();
//                    sendFileFunc("send.txt",srcPort,new int[]{dstPort},FORWARD);
                    sendFileFunc("send.txt",srcPort,dstPort,TEST);
                    break;
                case "Router":
//                    System.out.print("Port: ");
                    int port = scan.nextInt();
                    boolean cont = true;
                    while (cont) {
//                        System.out.print("What do: ");
                        input = scan.next();
                        switch (input) {
                            case "info":
//                                sendFileFunc("hello.txt", port, NO_PORTS, SEND_INFO);
                                sendFileFunc("hello.txt",port,port,SEND_INFO);
                                cont = false;
                                break;
                            case "hello":
//                                sendFileFunc("hello.txt", port, NO_PORTS, HELLO);
                                cont = false;
                                break;
                            case "hello2":
//                                sendFileFunc("hello.txt", port, new int[]{-1}, HELLO);
                                cont = false;
                                break;
                            case "?":
                                System.out.println("~");
                        }
                    }
                    break;
                case "Controller":
                    cont = true;
                    while (cont) {
                        System.out.print("What do: ");
                        input = scan.next();
                        switch (input) {
                            case "?":
                                System.out.println("~");
                        }
                    }
                    break;
                case "q":
                    System.out.print("Bye");
                    System.exit(0);
            }
        }
    }

//    @Override
//    public void onReceipt(DatagramPacket packet) {
//
//    }
    public void processFileFunc(DatagramPacket packet, FileFuncContent content) {
    }

//    public static void main(String[] args) {
//        try {
//            (new User()).start();
//            System.out.println("Program completed");
//        } catch(java.lang.Exception e) {e.printStackTrace();}
//    }
}

/*
case "Router":
                    System.out.print("Port: ");
                    int port = scan.nextInt();
                    boolean cont = true;
                    while (cont) {
                        System.out.print("What do: ");
                        input = scan.next();
                        switch (input) {
                            case "hello":
                                sendFileFunc("hello.txt", port, NO_PORTS, HELLO);
                                cont = false;
                                break;
                            case "hello2":
                                sendFileFunc("hello.txt", port, new int[]{-1}, HELLO);
                                cont = false;
                                break;
                            case "?":
                                System.out.println("~");
                        }
                    }
                    break;
 */