package lection3;
import java.net.*;
import java.util.Date;
import java.io.*;

class JabberClientThread extends Thread {
      
   public JabberClientThread(InetAddress addr) {
      System.out.println("Запустимо клієнт з номером " + id);
      threadcount++;
      try {
         socket = new Socket(addr, MultiJabberServer.PORT);
      }
      catch (IOException e) {
         System.err.println("Не вдалося з'єднатися з сервером");
         // Якщо не вдалося створити сокер нічого
         // не потрібно чистити
      }
      try {
         in = new BufferedReader(new InputStreamReader(socket
               .getInputStream()));

         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
               socket.getOutputStream())), true);
         start();
      }
      catch (IOException e) {
         // Сокет має бути закритий при будь якій помилці
    	 // крім помилки конструктора сокета
         try {
            socket.close();
         }
         catch (IOException e2) {
            System.err.println("Сокет не закрито");
         }
      }
      // Якщо все відбудеться нормально сокет буде закрито
      // в методі run() потоку.
   }
   
   public static int threadCount() {
	      return threadcount;
   }
	   
   public void run() {
      try {
         for (int i = 0; i < 5; i++) {
            out.println("Client " + id + ": " + i +" час відправки: "+new Date().getTime());
            String str = in.readLine();
            System.out.println(str+ " час отримання: "+new Date().getTime());
         }
         out.println("END");
      }
      catch (IOException e) {
         System.err.println("IO Exception");
      }
      finally {
         // Завжди закриває:
         try {
            socket.close();
         }
         catch (IOException e) {
            System.err.println("Socket not closed");
         }
         //threadcount--; // Завершуємо цей потік
      }
   }
   
   private Socket socket;
   private BufferedReader in;
   private PrintWriter out;
   private static int counter = 0;
   private int id = counter++;
   private static int threadcount = 0;
}