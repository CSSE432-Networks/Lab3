import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jesse Shellabarger on 4/21/2017.
 */
public class Proxy {

    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        Proxy prox = new Proxy();
        prox.start();
    }

    public Proxy() {
        System.out.println("Enter a port to listen on.");
        Scanner scanner = new Scanner(System.in);
        int port = Integer.parseInt(scanner.nextLine());

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() throws IOException {
        while (true) {
            Socket sock = serverSocket.accept();
            new ProxyThread(sock).run();
        }
    }
}
