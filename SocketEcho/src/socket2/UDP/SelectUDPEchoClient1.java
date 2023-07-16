package socket2.UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class SelectUDPEchoClient1 {

    private Scanner sc; // 데이터 입력
    ByteBuffer byteBuffer = ByteBuffer.allocate(256);

    public static void main(String[] args) {
        new SelectUDPEchoClient1();
    }

    public SelectUDPEchoClient1() {
        init();
    }

    public void init() {

        try {

            DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
            sc = new Scanner(System.in);

            while(true) {
                System.out.print("to Server : ");
                String data = sc.next();

                Charset charset = Charset.forName("UTF-8");
                byteBuffer = charset.encode(data);

                channel.send(byteBuffer, new InetSocketAddress("localhost", 6666)); // 데이터 보내기
                System.out.println(byteBuffer.toString());


                if (data.equals("exit")) {
                    break;
                }

                SocketAddress socketAddress = channel.receive(byteBuffer);
                String receivedata = new String(byteBuffer.array());
                System.out.println("from Server : " + receivedata);
                
                byteBuffer.clear();
            }
            channel.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
