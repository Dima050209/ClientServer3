package lection3;

import java.io.*;
import java.net.Socket;

public class ServeOneJabber extends Thread {

    public ServeOneJabber(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //включаємо автоматичне виштовхування
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        //Якщо будь-який з вище перерахованих викликів приведе до виникнення
        //виключення, тоді викликаючий буде відповідальний за закриття сокета
        //інакше потік закриє його
        start();//викликаємо run();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END"))
                    break;
                System.out.println("Ехо стрічка:" + str);
                out.println(str);
            }
            System.out.println("Закриваємо сокет на сервері");
        } catch (IOException e) {
            System.err.println("IO Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Сокет не закрито ...");
            }
        }
    }

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
}
