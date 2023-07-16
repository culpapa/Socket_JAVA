package socket2.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SelectTCPEchoClient1 {

    private Socket clientSocket;    // 클라이언트 소켓
    private BufferedReader br;      // 클라이언트로부터 전달받은 메시지를 읽는 버퍼 메모리
    private PrintWriter pw;         // 클라이언트로 메시지 전송
    private Scanner sc;             // 데이터 입력

    public static void main(String[] args) {
        new SelectTCPEchoClient1();
    }

    public SelectTCPEchoClient1() {
        init();
    }

    public void init() {
        try {
            clientSocket = new Socket("localhost", 6666);
            System.out.println("Server Connect");

            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // 서버로부터 데이터를 받아올 준비
            pw = new PrintWriter(clientSocket.getOutputStream());                           // 서버로 데이터를 보낼 준비

            sc = new Scanner(System.in);    //입력한 데이터를 읽을 준비

            //System.out.println("");

            while(true) {
                System.out.print("to Server : ");
                String inputData = sc.next();

                pw.println(inputData);      //보낼 내용을 읽어와서 서버로 보냄
                pw.flush();                 //프린터 라이터 메모리를 초기화 시켜서 내부에 있던 데이터를 서버로 전송

                if(inputData.equals("exit")) { // exit 입력 시 소켓 닫음
                    break;
                }

                System.out.println("from Server : " + br.readLine()); //서버에서 받은 데이터를 출력
            }
            clientSocket.close();   //연결 종료하면 소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
