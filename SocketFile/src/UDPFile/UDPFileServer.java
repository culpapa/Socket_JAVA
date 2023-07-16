package UDPFile;

import java.io.*;
import java.net.*;

public class UDPFileServer {

    public static void main(String[] args) {
        new UDPFileServer().init();
    }

    public void init() {

        try {
            DatagramSocket serverSocket = new DatagramSocket(9999);
            byte[] buf = new byte[1024];
            System.out.println("Server is ready");

            File file = null;
            DataOutputStream DOS = null;

            while(true) {

                DatagramPacket DP = new DatagramPacket(new byte[1024], 1024);
                serverSocket.receive(DP);
                String str = new String(DP.getData()).trim();

                // 파일 전송 시작 메시지를 받을 경우
                if(str.equals("start")) {
                    System.out.println("Start saving file");

                    DP = new DatagramPacket(buf, buf.length);
                    serverSocket.receive(DP);
                    str = new String(DP.getData()).trim();

                    file = new File("C:/SData/" + str);
                    DOS = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                }
                else {
                    DOS.write(str.getBytes(), 0, str.getBytes().length);
                    DOS.flush();
                    System.out.println("File save complete\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save file" + e.getMessage());
        }
    }
}
