package lection3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpURLTest {

    public static void main(String[] args) throws IOException {
        URL hp = new URL("http://distedu.ukma.kiev.ua");
        BufferedReader in = new BufferedReader(new InputStreamReader(hp.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }
        in.close();
    }

}
