package com.info63.iut.audioshare.metier;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.IOException;

/**
 * Created by maxime on 02/17/2016.
 */

public class BluetoothHandler extends Thread{
    private BluetoothAdapter mBluetoothAdapter;
    private AcceptThread at;
    private ConnectThread ct;

    public BluetoothHandler() {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public AcceptThread startServer(){
        at = new AcceptThread(mBluetoothAdapter);
        return at;
    }

    public ConnectThread startClient(BluetoothDevice bd){
        ct = new ConnectThread(bd, this);
        return ct;
    }


}

