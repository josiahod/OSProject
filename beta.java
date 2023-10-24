import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;


public class beta extends JFrame {

    private JTextField urlField;
    private JButton downloadButton;

    public beta() {
        setTitle("Proxy and Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        urlField = new JTextField("https://stackoverflow.com", 30);
        downloadButton = new JButton("Download");

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                downloadWebPage(url);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter URL:"));
        panel.add(urlField);
        panel.add(downloadButton);

        add(panel);
    }

    private void downloadWebPage(String url) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Save Location");
            fileChooser.setSelectedFile(new File("downloaded.html"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                
                if (!fileToSave.getName().toLowerCase().endsWith(".html")) {
                    fileToSave = new File(fileToSave.getParent(), fileToSave.getName() + ".html");
                }

                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
                URL urlObj = new URL(url);
                
				/*
				 * I literally cannot get this proxy working i dont know what im doing wrong :/
				 * URLConnection conn = urlObj.openConnection(proxy);
				 */   
                
                URLConnection conn = urlObj.openConnection();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                }

                reader.close();
                writer.close();

                System.out.println("HTML downloaded successfully!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Download Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                beta gui = new beta();
                gui.setVisible(true);
            }
        });
    }
}
