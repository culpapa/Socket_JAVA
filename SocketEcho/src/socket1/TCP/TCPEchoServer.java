package socket1.TCP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {
    private ServerSocket serverSocket;  // 서버 소켓
    private BufferedReader br;          // 클라이언트로부터 전달받은 메시지를 읽는 버퍼 메모리
    private PrintWriter pw;             // 클라이언트로 메시지 전송
    private Socket clientSocket;        // 클라이언트 소켓

    public static void main(String[] args) {
        new TCPEchoServer();
    }

    public TCPEchoServer() {
        init();
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(8888); // 8888번 포트의 서버 소켓 생성
            System.out.println("Server is ready");
            System.out.println("connect clinet...");

            clientSocket = serverSocket.accept(); //
            System.out.println("Client has accepted");

            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 클라이언트로부터 데이터를 읽어올 준비
            pw = new PrintWriter(clientSocket.getOutputStream());                          // 클라이언트로부터 데이터를 보낼 준비

            String readData = ""; // 클라이언트로부터 읽어온 데이터를 저장할 공간

            while (!(readData = br.readLine()).equals("exit")) {
                System.out.println("from Client >" + readData);
                pw.println(readData);    // 읽은 메시지를 그대로 클라이언트에 다시 보냄
                pw.flush();              // 프린트 라이터 메모리를 초기화
            }
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
