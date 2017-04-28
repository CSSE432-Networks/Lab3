import java.io.*;
import java.net.InetAddress;
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
            InputStream inputStream = socket.getInputStream();
            StringBuilder requestBuilder = new StringBuilder();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            while(read != -1) {
                requestBuilder.append(new String(bytes));
                if (requestBuilder.toString().contains("\r\n\r\n")) break;
                read = inputStream.read(bytes);

            }

            String request = requestBuilder.toString();
            if (request.equals("")) return;
            System.out.println("~~~~~~~~~~~~ Received HTTP Request ~~~~~~~~~~~~");
            System.out.println(request);


            String hostname = extractHostname(request).trim();
            String requestToSend = removeHostnameFromGet(request, hostname);
            Socket proxySocket = new Socket(InetAddress.getByName(hostname), 80);
            PrintWriter pw = new PrintWriter(proxySocket.getOutputStream());
            pw.write(requestToSend);
            pw.flush();

            InputStream in = proxySocket.getInputStream();
            byte[] arr = new byte[1024*1024];
            int byteRead = in.read(arr);
            while(byteRead != -1) {
                socket.getOutputStream().write(arr, 0, byteRead);
                System.out.print(new String(arr));
                byteRead = in.read(arr);
            }

            pw.close();
            socket.close();
            proxySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeHostnameFromGet(String request, String hostname) {
        return request.replace("http://"+hostname, "");
    }

    private String extractHostname(String request) {
        int index = request.indexOf("Host: ") + 6;
        String substring = request.substring(index);
        String host = substring.substring(0, substring.indexOf("\n"));
        return host;
    }
}
