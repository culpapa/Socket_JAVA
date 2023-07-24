import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileTransferClient {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("IP를 입력하세요 : ");
        String IP = input.next();

        System.out.print("Port를 입력하세요 : ");
        int port = Integer.valueOf(input.next());

        System.out.print("전송할 파일 경로를 입력하세요 : ");
        String sendDir = input.next();

        System.out.print("저장할 파일 경로를 입력하세요 : ");
        String saveDir = input.next();

        try {
            Socket socket = new Socket(IP, port);
            System.out.println("connected to server");

            sendFileToServer(socket, sendDir, saveDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendFileToServer(Socket socket, String sendDir, String saveDir) {
        try {
            File file = new File(sendDir);
            String FileName = file.getName();

            // 파일이 존재하지 않을 경우
            if(!file.exists()) {
                System.out.println(FileName + " File does no exist");
                return;
            }

            // 파일 기록할 버퍼 스트림
            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
            BufferedOutputStream BOS = new BufferedOutputStream(DOS);

            // 파일명 전송
            DOS.writeUTF(saveDir);

            byte[] fileData = new byte[(int) file.length()];

            // 파일 읽기
            FileInputStream FIS = new FileInputStream(file);
            BufferedInputStream BIS = new BufferedInputStream(FIS);

            BIS.read(fileData);

            // 클라이언트에게 파일 데이터 전송
            OutputStream os = socket.getOutputStream();
            os.write(fileData, 0, fileData.length);
            os.flush();

            socket.close();

            System.out.println("File transfer complete");

        } catch (IOException e) {
            System.out.println("File transfer failed");
            e.printStackTrace();
        }

    }
}
