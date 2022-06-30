package lection3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ObjectClient {
    public static void main(String[] args) throws IOException {
        InetAddress addr = InetAddress.getByName(null);
        System.out.println("адреса =" + addr);
        Socket socket = new Socket(addr, ObjectServer.PORT);
        try {
            TestStudent ts = new TestStudent("Вальт", 3);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(ts);
            oos.flush();
            oos.close();
        } finally {
            System.out.println("закриваємо клієнт");
            socket.close();
        }
    }
}
