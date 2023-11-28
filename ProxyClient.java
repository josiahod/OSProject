import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ProxyClient {
    private JTextField urlTextField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProxyClient client = new ProxyClient();
            client.GUIScreen();
        });
    }

    private void GUIScreen() {
        JFrame frame = new JFrame("OS Proxy Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        urlTextField = new JTextField(20);
        JButton sendButton = new JButton("Send URL");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter URL: "));
        inputPanel.add(urlTextField);
        inputPanel.add(sendButton);


        frame.add(inputPanel, BorderLayout.NORTH);
        frame.setSize(500, 100);
        frame.setVisible(true);

        sendButton.requestFocus();
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();

            String urlToSend = urlTextField.getText();
            if (!urlToSend.isEmpty()) {
                String filename = JOptionPane.showInputDialog("Enter the filename:");
                if (filename != null && !filename.isEmpty()) {
                    if (!filename.toLowerCase().endsWith(".html")) 
                    {
                        filename += ".html";
                    }
                    sendURL(urlToSend, new File(selectedDirectory, filename));
                } else {
                    JOptionPane.showMessageDialog(null, "Filename cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "URL cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sendURL(String url, File file) {
        String serverName = "localhost";
        int port = 8080;

        try {
            String command = "java -cp . MultiThreadedProxyServer.java";
            Process process = Runtime.getRuntime().exec(command);

            System.out.println("Connecting to " + serverName + " on port " + port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try (Socket client = new Socket(serverName, port);
             OutputStream outToServer = client.getOutputStream();
             PrintWriter out = new PrintWriter(outToServer, true)) {

            out.println(url);

            InputStream inFromServer = client.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inFromServer));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(response.toString());
            }

            JOptionPane.showMessageDialog(null, "HTML content saved to: " + file.getAbsolutePath(), "Download Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error sending URL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
