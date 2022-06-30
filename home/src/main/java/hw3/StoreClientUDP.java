package hw3;

import java.io.IOException;
import java.net.*;

public class StoreClientUDP {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public StoreClientUDP() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public byte[] sendEcho(byte[] msg) throws IOException {
        // buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, address, 4445);
        socket.send(packet);
        //packet = new DatagramPacket(msg, msg.length);

        socket.receive(packet);
        byte[] received = packet.getData();
        if(received != null){
            return received;
        } else {
            return sendEcho(msg);
        }
    }

    public void close() {
        socket.close();
    }
}
