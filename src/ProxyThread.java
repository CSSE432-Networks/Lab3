import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Jesse Shellabarger on 4/21/2017.
 */
public class ProxyThread extends Thread {
    private Socket socket;

    public ProxyThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
