package hw3;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Processor extends Thread{
    Command command;
    private Socket clientSocket;
    private DatagramSocket datagramSocket;
    private InetAddress address;
    private int port;

    public Processor(Command command, Socket clientSocket) {
        this.command = command;
        this.clientSocket = clientSocket;
        this.start();
    }
    public Processor(Command command, DatagramSocket datagramSocket, InetAddress address, int port) {
        this.command = command;
        this.datagramSocket = datagramSocket;
        this.address = address;
        this.port = port;
        this.start();

    }
    @Override
    public void run(){
        super.run();
        process();
    }

    public void process() {
        System.out.println(command);
        CommandType commandId = command.getCommandId();
        String userId = command.getUserId();
        String productId = command.getProductId() + "";
        double commandBody = command.getCommandBody();

        switch (commandId){
            case ADD_AMOUNT:
                Product.getProductById(productId).addAmount((int)commandBody);
                break;
            case GET_AMOUNT:
                Product.getProductById(productId).getAmount();
                break;
            case SET_PRICE:
                Product.getProductById(productId).setPrice(commandBody);
                break;
            case TAKE_AMOUNT:
                Product.getProductById(productId).takeAmount((int)commandBody);
                break;
            case ADD_PRODUCT_GROUP:
                Product.addProductGroup("Group"+(int)commandBody);
                break;
            case ADD_PRODUCT_TO_GROUP:
                Product.getProductById(productId).setProductGroup("Group"+(int)commandBody);
                break;
            default:
                break;
        }
        Command okCommand = new Command(CommandType.OK,userId,commandBody,productId);
        if(clientSocket != null) {
            Encryptor encryptor = new Encryptor(okCommand, clientSocket);
            try {
                encryptor.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            Encryptor encryptor = new Encryptor(okCommand, datagramSocket, address, port);
            try {
                encryptor.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
