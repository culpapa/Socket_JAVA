package TCPFile;

import java.io.*;
import java.net.Socket;

public class TCPFileClient {

    public static void main(String[] args) {
        new TCPFileClient().init();
    }

    public void init() {

        File file = new File("C:/CData/test.txt");
        String FileName = file.getName();

        // 파일이 존재하지 않을 경우
        if(!file.exists()) {
            System.out.println(FileName + "File does no exist");
            return;
        }

        try {
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Start file transfer");

            // 파일 기록할 버퍼 스트림
            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
            BufferedOutputStream BOS = new BufferedOutputStream(DOS);

            // 파일명 전송
            DOS.writeUTF(FileName);

            // 파일을 읽어올 버퍼 스트림
            BufferedInputStream BIS = new BufferedInputStream(new FileInputStream(file));

            byte[] temp = new byte[1024];
            int length;

            // 파일 내용 읽어와 소켓으로 전송
            while ((length = BIS.read(temp)) > 0) {
                BOS.write(temp, 0, length);
            }
            BOS.flush();
            System.out.println("File transfer complete");

            BIS.close();
            BOS.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File transfer failed" + e.getMessage());
        }
    }
}
