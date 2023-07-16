package socket2.UDP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class SelectUDPEchoServer {

    private static SocketAddress socketAddress;

    public static void main(String[] args) {
        new SelectUDPEchoServer();
    }

    public SelectUDPEchoServer() {
        init();
    }

    public void init() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        try {
            Selector selector = Selector.open();
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(6666));
            channel.register(selector, SelectionKey.OP_READ);

            while(true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 준비된 채널 발생 -> 집합 반환 받음
                Iterator<SelectionKey> iter = selectedKeys.iterator();    // 설렉터 키 셋을 가져옴

                while (iter.hasNext()) {

                    SelectionKey key = iter.next();

                    // 수신일 경우
                    if (key.isReadable()) {
                        EchoReceive(buffer, key);
                    }

                    // 송신일 경우
                    if (key.isValid() && key.isWritable()) {
                        EchoSend(buffer, key);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void EchoReceive(ByteBuffer buffer, SelectionKey key) {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            socketAddress = channel.receive(buffer);

            buffer.flip();
            Charset charset = Charset.forName("UTF-8");
            String data = charset.decode(buffer).toString();

            System.out.println("from Client > " + data);

            if(data.trim().equals("exit")) {
                buffer.clear();
                //channel.close();
                System.out.println("Not accepting client messages anymore");
            }
            else {
                key.interestOps(SelectionKey.OP_WRITE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void EchoSend(ByteBuffer buffer, SelectionKey key) {

        try{
            DatagramChannel channel = (DatagramChannel) key.channel();
            buffer.flip();
            int bytesSent = channel.send(buffer,socketAddress);

            if (bytesSent != 0) {
                key.interestOps(SelectionKey.OP_READ);
            }
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
