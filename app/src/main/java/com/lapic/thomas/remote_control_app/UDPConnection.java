package com.lapic.thomas.remote_control_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class UDPConnection implements Runnable {

    private String TAG = this.getClass().getSimpleName();
    private String ip;
    private int port;
    private long seqNum = 0;
    private String hashSource;
    byte[] messageBuf;

    public UDPConnection(Context context) {
        PreferencesHelper mPref = new PreferencesHelper(context);
        this.ip = mPref.getIp();
        this.port = mPref.getPort();
        this.hashSource = generateHash();
//        this.hashSource = "34c533c3efaa4294e8df603f87728d31";
    }

    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(ip);
//            byte[] buf = ("The String to Send").getBytes();
            DatagramPacket packet = new DatagramPacket(messageBuf, messageBuf.length, serverAddr, port);
            udpSocket.send(packet);
            udpSocket.close();
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }

    public static final String generateHash() {
        RandomString randomString = new RandomString(16);
        String s = randomString.nextString();
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void resetSeqNum() {
        this.seqNum = 0;
    }

    public void connect() {
        try {
            sendMessage((short) 10, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            sendMessage((short) 11, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKey(String code) {
        try {
            sendMessage((short) 100, code);
            sendMessage((short) 101, code);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(short type, String body) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //type
        byteArrayOutputStream.write((byte)(type >> 8));
        byteArrayOutputStream.write((byte)(type));
        Log.e(TAG, ""+byteArrayOutputStream.toByteArray()[0]);
        Log.e(TAG, ""+byteArrayOutputStream.toByteArray()[1]);
        //size
        short size = (short) body.length();
        byteArrayOutputStream.write((byte)(size >> 8));
        byteArrayOutputStream.write((byte)(size));
        //timestamp
        byte[] timestamp = new byte[]{0,0,0,0,0,0};
        byteArrayOutputStream.write(timestamp);
        //seqNum
        byte[] seqNumArray = new byte[]{(byte)(seqNum >> 40),
                                        (byte)(seqNum >> 32),
                                        (byte)(seqNum >> 24),
                                        (byte)(seqNum >> 16),
                                        (byte)(seqNum >> 8),
                                        (byte) seqNum};
        byteArrayOutputStream.write(seqNumArray);
        //source
        byteArrayOutputStream.write(hexStringToByteArray(hashSource));
        //destination
        byte[] destinationArray = new byte[16];
        for (int i = 0; i < destinationArray.length; i++) {
            destinationArray[i] = 0;
        }
        byteArrayOutputStream.write(destinationArray);
        Log.e(TAG, "bodyArray.length: "+ byteArrayOutputStream.size());
        //body
        byte[] bodyArray = body.toString().getBytes("UTF-8");
        byteArrayOutputStream.write(bodyArray);
        messageBuf = byteArrayOutputStream.toByteArray();

        Thread thread = new Thread(this);
        thread.start();
        seqNum++;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


}
