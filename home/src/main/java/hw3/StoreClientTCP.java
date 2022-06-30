package hw3;

import java.io.*;
import java.net.Socket;

public class StoreClientTCP {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    OutputStream outStream;
    DataOutputStream dos;
    InputStream inStream;
    DataInputStream dis;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outStream = clientSocket.getOutputStream();
        dos = new DataOutputStream(outStream);
        inStream = clientSocket.getInputStream();
        dis = new DataInputStream(inStream);

    }

    public void sendMessage(byte[] msg) throws IOException, InterruptedException {
        out.println("hello server");
        String resp = in.readLine();
        if(resp.equals("hello client")){
            sendBytes(msg);
            byte[] response = new byte[0];
            while(true) {
                if(dis.available() > 0) {
                    response = readBytes();
                    break;
                }
            }
            PacketBuilder packetBuilder = new PacketBuilder();
            System.out.println(packetBuilder.decode(response));
        } else {
            System.out.println("Invalid try");
        }
    }
    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);
        }
    }
    public byte[] readBytes() throws IOException {

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PacketBuilder packetBuilder = new PacketBuilder();
        Command command = new Command(
                CommandType.GET_AMOUNT, "user1", 0, "apple");

        StoreClientTCP client = new StoreClientTCP();
        client.startConnection("127.0.0.1", 6666);
        client.sendMessage(packetBuilder.encode(command));

        client.stopConnection();
    }
}
