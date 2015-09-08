package com.bookerapps.iracingdigitaldash;

import com.bookerapps.iracingdigitaldash.service.SocketReader;
import com.bookerapps.iracingdigitaldash.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class DigitalDashActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_digital_dash);
    }

    private boolean isReading = false;
    TextView txtRpm;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        txtRpm = (TextView) findViewById(R.id.tvRPM);

        TelemetryDataListener listener = new TelemetryDataListener() {
            @Override
            public void onTelemetryDataUpdate(String data) {
                try{
                    System.out.print("Telemetry Data: " + data);
                    Log.d("Telemetry Data", data);
                    JSONObject json = new JSONObject(data);
                    SetText(json.getString("Rpm"));
                }
                catch (JSONException e){

                }
                catch(Exception eX){

                }
            }
        };

        SocketReader reader = new SocketReader(listener, "239.255.0.1", 1776);
        reader.StartReading();
    }

    public void SetText(String rpm){
        txtRpm.setText(rpm);
        txtRpm.invalidate();
        GridLayout parent = (GridLayout)txtRpm.getParent();
        parent.invalidate();
    }
}
