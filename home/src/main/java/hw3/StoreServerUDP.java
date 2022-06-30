package hw3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class StoreServerUDP extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public StoreServerUDP() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;
        PacketBuilder packetBuilder = new PacketBuilder();
        while (running) {

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);

            byte[] received = packet.getData();
            Command receivedComm = packetBuilder.decode(received);
            if (receivedComm.getCommandId() == CommandType.END) {
                running = false;
            }
            Decryptor decryptor = new Decryptor(received, socket, address, port);
            try {
                decryptor.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        socket.close();
    }
}
