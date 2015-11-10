package com.example.olegx100.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class mainView extends AppCompatActivity implements Joystick.IJoystickEvents {

    Joystick joystick;
    TextView xcoord, ycoord;
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        InitJoystick();
        ConnectBtDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//http://stackoverflow.com/questions/6565144/android-bluetooth-com-port
//http://developer.android.com/guide/topics/connectivity/bluetooth.html

    public void ConnectBtDevice ()
    {
        BluetoothSocket socket = null;
        try {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // Device does not support Bluetooth
            finish(); //exit
        }

        if (!adapter.isEnabled()) {
//make sure the device's bluetooth is enabled
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }

        final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for serial connection
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.d("debug", device.getAddress() + ", " + device.getName());
                if (device.getName().compareTo("QY7") != 0)
                    continue;

                AcceptThread at = new AcceptThread (adapter, "xyz", SERIAL_UUID);
                //at.run();
                //socket = DevConnect(device);
                if (socket != null)
                    break;
            }
        }

        }
        catch (Exception e) {
            Log.d("debug", e.getMessage());
        }
    }

    public BluetoothSocket DevConnect (BluetoothDevice dev)
    {
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        BluetoothSocket socket = null;
        OutputStream out = null;

        try {
            final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            socket = dev.createRfcommSocketToServiceRecord(SERIAL_UUID);
            if (socket != null) {
                socket.connect();
                out = socket.getOutputStream();
                //now you can use out to send output via out.write
                out.write(new byte[]{0, 0, 0, 0});
            }
        }
        catch (Exception ex){
            try{
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            }
            catch (Exception ex2)
            {

            }

        }
        return socket;
    }


    public void btServerConnect () {


    }

    protected void InitJoystick()
    {
        xcoord = (TextView)findViewById(R.id.xcoord);
        ycoord = (TextView)findViewById(R.id.ycoord);
        joystick = (Joystick)findViewById(R.id.Joystick);
        joystick.setEventsListener(this);
    }

    @Override
    public void OnMove (double x, double y){
        xcoord.setText(String.format("%1$.2f", x));
        ycoord.setText(String.format("%1$.2f", y));
    }
}
