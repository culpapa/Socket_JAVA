package socket1.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPEchoClient {

    private DatagramSocket socket;  // UDP 클라이언트 소켓
    private Scanner sc; // 데이터 입력

    public static void main(String[] args) {
        new UDPEchoClient();
    }

    public UDPEchoClient() {
        init();
    }

    public void init() {
        try {

            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");

            DatagramPacket inpacket, outpacket; // 수신, 송신 패킷 변수 선언
            System.out.println("Server Connect");

            byte[] bmsg = new byte[1024];
            sc = new Scanner(System.in);    //입력한 데이터를 읽을 준비

            while(true) {
                System.out.print("to Server : ");
                String inputData = sc.next();

                // 데이터 전송
                outpacket = new DatagramPacket(inputData.getBytes(), inputData.getBytes().length, address, 8888);
                socket.send(outpacket);

                if(inputData.equals("exit")) { // exit 입력 시 소켓 닫음
                    break;
                }

                // 데이터 수신
                inpacket = new DatagramPacket(bmsg, bmsg.length);
                socket.receive(inpacket);
                String receiveData = new String(bmsg, 0, inpacket.getLength());
                System.out.println("from Server : " + receiveData);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
