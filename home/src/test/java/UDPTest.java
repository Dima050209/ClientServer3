import hw3.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPTest {
    StoreClientUDP client;
    StoreServerUDP server;
    @Before
    public void setup() throws SocketException, UnknownHostException {
        server = new StoreServerUDP();
        server.start();
        client = new StoreClientUDP();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        Product apple = new Product("apple",100,10);
        PacketBuilder packetBuilder = new PacketBuilder();
        Command command = new Command(CommandType.GET_AMOUNT, "user1",0,"apple");
        byte[] echo = client.sendEcho(packetBuilder.encode(command));
        System.out.println(packetBuilder.decode(echo));
    }

    @After
    public void tearDown() throws IOException {
        PacketBuilder packetBuilder = new PacketBuilder();
        Command endCommand = new Command(CommandType.END, "user1",0,"");
        byte[] endMsg = client.sendEcho(packetBuilder.encode(endCommand));
        System.out.println(packetBuilder.decode(endMsg));
        client.close();
    }
}
