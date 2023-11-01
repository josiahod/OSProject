
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ProxyClient {
    public static void main(String[] args) {
        String serverName = "localhost"; // Proxy server name
        int port = 8080;                // Proxy server port

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            OutputStream outToServer = client.getOutputStream();
            PrintWriter out = new PrintWriter(outToServer, true);

            Scanner scanner = new Scanner(System.in);
            // Sending URL to proxy server
            System.out.print("Enter URL to fetch: ");
            String urlToSend = scanner.nextLine();
            System.out.println("Sending URL: " + urlToSend);
            out.println(urlToSend);

            // Receiving data from proxy server
            InputStream inFromServer = client.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inFromServer));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
