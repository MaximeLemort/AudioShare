package com.info63.iut.audioshare.metier;

/**
 * Created by maxime on 03/02/2016.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    public AcceptThread(BluetoothAdapter ba) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = ba.listenUsingRfcommWithServiceRecord("AudioShare", UUID.fromString("0122481238865999551147"));
        } catch (IOException e) { Log.e("BtEvent", "Error accepting a connection"); }
        mmServerSocket = tmp;
    }

    public void run() {
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
                //manageConnectedSocket(socket); -- TODO once connection is complete
                try {
                    mmServerSocket.close();
                }
                catch (IOException e) {
                    Log.e("BtEvent", "Error during the socket process");
                    break;}
                break;
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { Log.e("BtEvent", "Error cancelling socket"); }
    }
}
