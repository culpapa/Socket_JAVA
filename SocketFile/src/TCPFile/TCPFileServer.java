package TCPFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPFileServer {

    File SaveDir = new File("C:/SData/");

    public static void main(String[] args) {
        new TCPFileServer().init();
    }

    public void init() {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(9999);
                System.out.println("Server is ready");

                Socket socket = serverSocket.accept();
                System.out.println("Start saving file");

                // 파일 읽어올 버퍼 스트림
                DataInputStream DIS = new DataInputStream(socket.getInputStream());
                BufferedInputStream BIS = new BufferedInputStream(DIS);

                // 클라이언트에서 보낸 파일 이름 받음
                String FileName = DIS.readUTF();
                File SaveFile = new File(SaveDir, FileName);

                // 파일 기록할 버퍼 스트림
                BufferedOutputStream BOS = new BufferedOutputStream(new FileOutputStream(SaveFile));

                byte[] temp = new byte[1024];
                int length;

                while ((length = BIS.read(temp)) > 0) {
                    BOS.write(temp, 0, length);
                }
                BOS.flush();

                System.out.println("File save complete\n");

                BOS.close();
                BIS.close();
                socket.close();
                serverSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save file" + e.getMessage());
            }
        }
    }
}

