package com.joeshaw.udp_demo.long_connection;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/***客户端：本来客户端是使用pb来实现的，但是这里使用java来模拟
 * UDP Client端
 ***/
public class UdpClient {

    private String sendStr = "hello";
    private String netAddress = "192.168.1.200";
    private final int PORT = 5062;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public UdpClient(){
        try {
            datagramSocket = new DatagramSocket();
            byte[] buf = new byte[8];
            buf[0]= -91;
            buf[1]= -85;
            buf[2]= (byte) 2;
            buf[3]= (byte) -73;
            buf[4]= (byte) 1;
            buf[5]= (byte) 0;
            buf[6]= (byte) 0;
            buf[7]= (byte) -70;
            //-91, -85, 2, -73, 1, 0, 0, -70
            //a5 ab 02 b1 00 00 00 b3  a5 ab 02 b1 00 00 00 00 00 00 00 d6 89
            //结果:[-91, -85, 2, -73, 1, 0, 0, -70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            InetAddress address = InetAddress.getByName(netAddress);
            datagramPacket = new DatagramPacket(buf, buf.length, address, PORT);
            datagramSocket.send(datagramPacket);
            for (int i = 0; i < 2; i++) {
                byte[] receBuf = new byte[25];
                DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
                datagramSocket.receive(recePacket);

                if (i!=0){
                    String receStr = new String(recePacket.getData(), 0 , recePacket.getLength());
                    System.out.println("第"+(i+1)+"次接收数据"+ Arrays.toString(recePacket.getData()));
                }

                //获取服务端ip
                String serverIp = recePacket.getAddress().getHostAddress();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if(datagramSocket != null){
                datagramSocket.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UdpClient udpClient = new UdpClient();
                }
            }).start();
           /// Thread.sleep(4000);
        }
    }
}
