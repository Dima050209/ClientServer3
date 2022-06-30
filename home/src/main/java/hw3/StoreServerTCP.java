package hw3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class StoreServerTCP {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    InputStream inStream;
    DataInputStream dis;

    public void start(int port) throws IOException, InterruptedException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        inStream = clientSocket.getInputStream();
        dis = new DataInputStream(inStream);

        String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
            out.println("hello client");
            System.out.println(greeting);
            Decryptor decryptor = new Decryptor(readBytes(),clientSocket);
            decryptor.join();
        }
        else {
            out.println("unrecognised greeting");
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

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Product apple = new Product("apple", 100, 10);
        StoreServerTCP server=new StoreServerTCP();
        server.start(6666);
        server.stop();
    }
}
