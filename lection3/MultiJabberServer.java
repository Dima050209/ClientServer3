package lection3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiJabberServer {

    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Сервер запущено.");

        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    new ServeOneJabber(socket);
                } catch (IOException e) {
                    //в разі невдачі закриваємо сокет
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }
}
