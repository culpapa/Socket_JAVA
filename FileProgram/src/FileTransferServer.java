import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTransferServer {

    public static void main(String[] args) {
        int PORT = 9999;
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is ready");

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                Runnable worker = new FileTransferHandler(socket);
                executor.execute(worker);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class FileTransferHandler implements Runnable {
        private Socket socket;

        public FileTransferHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            receiveFileFromClient(socket);
        }

        private void receiveFileFromClient(Socket socket) {
            try {

                // 파일 읽어올 버퍼 스트림
                DataInputStream DIS = new DataInputStream(socket.getInputStream());
                BufferedInputStream BIS = new BufferedInputStream(DIS);

                // 클라이언트에서 보낸 파일 이름 받음
                String savePath = DIS.readUTF();

                byte[] fileData = new byte[1024];
                InputStream is = socket.getInputStream();

                // 파일 저장
                FileOutputStream FOS = new FileOutputStream(savePath);
                BufferedOutputStream BOS = new BufferedOutputStream(FOS);
                int bytesRead;

                while ((bytesRead = is.read(fileData)) != -1) {
                    BOS.write(fileData, 0, bytesRead);
                }
                BOS.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("File save complete\n");
        }
    }
}
