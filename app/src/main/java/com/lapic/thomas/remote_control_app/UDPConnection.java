package com.lapic.thomas.remote_control_app;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPConnection implements Runnable {

    private String ip;
    private int port;

    public UDPConnection(String ip_port) {
        this.ip = ip_port.substring(0, ip_port.indexOf(":"));
        this.port = Integer.parseInt(ip_port.substring(ip_port.lastIndexOf(":")+1));
    }

    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(ip);
            byte[] buf = ("The String to Send").getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, port);
            udpSocket.send(packet);
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }

    public String sendMessage(int code) {
        Long tsLong = System.currentTimeMillis()/1000;

        Thread thread = new Thread(this);
        thread.start();
        return "Timestamp: " + tsLong;
    }

}
