package socket1.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPEchoServer {

    private DatagramSocket socket;  // UDP 서버 소켓

    public static void main(String[] args) {
        new UDPEchoServer();
    }

    public UDPEchoServer() {
        init();
    }

    public void init() {
        try {

            socket = new DatagramSocket(8888);  // 9999번 포트의 UDP 소켓 생성

            DatagramPacket inpacket, outpacket; // 수신, 송신 패킷 변수 선언
            System.out.println("Server is ready");

            while(true) {
                // 데이터 수신
                byte[] bmsg = new byte[1024];   // 데이터 저장할 byte 배열 선언

                inpacket = new DatagramPacket(bmsg, bmsg.length);  // 수신 패킷 객체 생성
                socket.receive(inpacket);  // 데이터 수신

                String readData = new String(inpacket.getData(), 0, inpacket.getLength());
                System.out.println("from Client > " + readData);

                // 데이터 전송
                byte[] sendmsg = readData.getBytes();

                outpacket = new DatagramPacket(sendmsg, sendmsg.length, inpacket.getAddress(), inpacket.getPort());

                socket.send(outpacket);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

}
