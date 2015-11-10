package com.example.olegx100.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Oleg on 07/11/2015.
 */
public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    public AcceptThread(BluetoothAdapter btAdapter, String NAME, UUID MY_UUID) {

        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {

        try {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                }
            }
        }
        catch (Exception ex) {

        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        }
        catch (IOException e){
        }
    }

    public void manageConnectedSocket(BluetoothSocket socket) {

        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            while (true)
            {
                os.write(new byte [] {0, 0, 0, 0});
            }
        }
        catch (Exception ex) {

        }
    }
}