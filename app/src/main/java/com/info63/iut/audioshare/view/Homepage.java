package com.info63.iut.audioshare.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.info63.iut.audioshare.R;
import com.info63.iut.audioshare.metier.BluetoothHandler;
import com.info63.iut.audioshare.metier.ConnectThread;
import com.info63.iut.audioshare.metier.AcceptThread;

import java.util.ArrayList;
import java.util.Set;

public class Homepage extends AppCompatActivity {

    private BluetoothHandler blueHandler;
    private BroadcastReceiver mReceiver;
    private ArrayAdapter<String> mArrayAdapter;
    private TextView eventDeviceFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        blueHandler = new BluetoothHandler();
        Log.i("BtEvent", "Handler has been created");
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        ((ListView) findViewById(R.id.listView)).setAdapter(mArrayAdapter);
        eventDeviceFound = (TextView) findViewById(R.id.deviceText);
    }


    public void activerBluetooth(View sender){
        Log.i("BtEvent", "Now enabling Bluetooth");
        if (blueHandler.getmBluetoothAdapter() == null) {
            Log.i("BtEvent", "Bluetooth isn't implemented");
        }
        try {
            if (!blueHandler.getmBluetoothAdapter().isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 0);
                Log.i("BtEvent", "Bluetooth is now enabled");
            }
        }
        catch(NullPointerException ne){
            Log.e("BtEvent", "Error enabling bluetooth");
        }
    }

    public void findDevice(View sender){
        Log.i("BtEvent", "Now searching for devices.");
        if(checkPairedDevices())
        {
            Log.i("BtEvent", "A paired device has been found");
            eventDeviceFound.setText("Device found - registered");
        }
        else Log.i("BtEvent", "No paired devices has been found");
        mReceiver = checkDiscoverableDevice();
        if(mReceiver!=null)
        {
            Log.i("BtEvent", "A new device has been found");
            eventDeviceFound.setText("Device found - new");
        }
    }

    private BroadcastReceiver checkDiscoverableDevice() {
        Log.i("BtEvent", "Starting to discover new devices...");
        blueHandler.getmBluetoothAdapter().startDiscovery();
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.i("BtEvent", "A device has been found");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i("BtEvent", device.getName() + " --- " + device.getAddress());
                    mArrayAdapter.add(device.getName() + " --- " + device.getAddress());
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Log.i("BtEvent", "Registering the receiver.");
        registerReceiver(mReceiver, filter);
        return mReceiver;
    }

    private boolean checkPairedDevices()
    {
        Log.i("BtEvent", "Checking for paired devices...");
        Set<BluetoothDevice> pairedDevices = blueHandler.getmBluetoothAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.i("BtEvent", "A paired device has been found");
                mArrayAdapter.add(device.getName() + " --- " + device.getAddress());
                Log.i("BtEvent", device.getName());
                mArrayAdapter.notifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    public void stopSearching(View sender){
        eventDeviceFound.setText("Searching has been cancelled");
        blueHandler.getmBluetoothAdapter().cancelDiscovery();
        Log.i("BtEvent", "The search has been halted manually");
    }

    @Override
    protected void onDestroy() {
        Log.i("BtEvent", "Unregistering.");
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }



}
