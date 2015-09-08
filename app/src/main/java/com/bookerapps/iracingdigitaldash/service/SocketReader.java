package com.bookerapps.iracingdigitaldash.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bookerapps.iracingdigitaldash.TelemetryDataListener;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONStringer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * Created by joey on 9/6/2015.
 */

public class SocketReader {
    private String address = "239.255.0.1";
    private int port = 1776;
    private boolean isReading = false;
    TelemetryDataListener telemetryDataListener;

    public SocketReader(TelemetryDataListener listener, String address, int port)
    {
        this.telemetryDataListener = listener;
        this.address = address;
        this.port = port;
    }

    public  void StopReading()
    {
        this.isReading = false;
    }

    public void StartReading() {
        this.isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastSocket socket = new MulticastSocket(port);
                    socket.joinGroup(InetAddress.getByName(address));
                    byte[] buffer = new byte[256];
                    while (isReading) {
                        DatagramPacket message = new DatagramPacket(buffer, buffer.length);
                        socket.receive(message);
                        String result = new String(message.getData(), 0, message.getLength(), "UTF-8");
                        telemetryDataListener.onTelemetryDataUpdate(result);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
