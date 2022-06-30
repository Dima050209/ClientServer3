package lection3;

import java.net.MalformedURLException;
import java.net.URL;

public class MyURL {

    public static void main(String[] args) throws MalformedURLException {
        URL hp = new URL("http://distedu.ukma.kiev.ua");
        System.out.println("Protocol: " + hp.getProtocol());
        System.out.println("Port: " + hp.getPort());
        System.out.println("Host: " + hp.getHost());
        System.out.println("File: " + hp.getFile());
        System.out.println("Ext: " + hp.toExternalForm());
    }
}
