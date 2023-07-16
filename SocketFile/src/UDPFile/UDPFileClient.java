package UDPFile;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPFileClient {

    public static void main(String[] args) {
        new UDPFileClient().init();
    }

    public void init() {

        try {

            File file = new File("C:/CData/test.txt");
            String FileName = file.getName();
            // 파일이 존재하지 않을 경우
            if(!file.exists()) {
                System.out.println(FileName + "File does no exist");
                return;
            }

            DatagramSocket socket = new DatagramSocket();
            InetAddress Addr = InetAddress.getByName("127.0.0.1");
            FileInputStream FIS = new FileInputStream(file);
            DataInputStream DIS = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));;
            DatagramPacket DP = null;

            // 파일 전송 시작 메시지 전송
            String str = "start";
            DP = new DatagramPacket(str.getBytes(), str.getBytes().length, Addr, 9999);
            socket.send(DP);

            // 전송할 파일명 전송
            String data = "udptest.txt";
            DP = new DatagramPacket(data.getBytes(), data.getBytes().length, Addr, 9999);
            socket.send(DP);

            byte[] buf = new byte[1024];

            while(true) {
                int readBytes = FIS.read(buf, 0, buf.length);

                if (readBytes == -1) {
                    break;
                }

                DP = new DatagramPacket(buf, readBytes, Addr, 9999);
                socket.send(DP);

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File transfer failed" + e.getMessage());
        }
    }
}
