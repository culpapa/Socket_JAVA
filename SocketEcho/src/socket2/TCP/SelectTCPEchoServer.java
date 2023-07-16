package socket2.TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;;
import java.util.Iterator;
import java.util.Set;

public class SelectTCPEchoServer {

    public static void main(String[] args)  {
        new SelectTCPEchoServer();
    }

    public SelectTCPEchoServer() {
        init();
    }

    public void init() {

        try {
            Selector selector = Selector.open();  // 셀렉터 생성
            ServerSocketChannel serverSocket = ServerSocketChannel.open(); // 채널 설정
            serverSocket.bind(new InetSocketAddress("localhost", 6666));   // ip, port 설정
            serverSocket.configureBlocking(false);                         // 논블록 설정
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);       // 채널 accept 대기 설정
            ByteBuffer buffer = ByteBuffer.allocate(256);                  // 버퍼 생성

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 준비된 채널 발생 -> 집합 반환 받음
                Iterator<SelectionKey> iter = selectedKeys.iterator();    // 설렉터 키 셋을 가져옴

                while (iter.hasNext()) {

                    SelectionKey key = iter.next();

                    // 접속일 경우
                    if (key.isAcceptable()) {
                        accept(selector, serverSocket);
                    }

                    // 수신일 경우
                    if (key.isReadable()) {
                        EchoReceive(buffer, key);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void accept(Selector selector, ServerSocketChannel serverSocket) {

        try {
            SocketChannel channel = serverSocket.accept();    // 키 채널 가져옴
            channel.configureBlocking(false);                 // 논블록 설정
            channel.register(selector, SelectionKey.OP_READ); // 채널 셀렉터에 등록
            System.out.println("new client connected...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void EchoReceive(ByteBuffer buffer, SelectionKey key) {

        try {
            SocketChannel channel = (SocketChannel) key.channel(); // 키 채널 가져옴

            channel.read(buffer);  // 데이터 수신

            //byte[] data = buffer.array();
            //String msg = new String(data);

            // exit 접속 종료
            String data = new String(buffer.array());
            System.out.println("from Client > " + data.trim());

            if (!(data.trim().equals("exit"))) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
            else {
                buffer.clear();
                channel.close();
                System.out.println("Not accepting client messages anymore");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}
