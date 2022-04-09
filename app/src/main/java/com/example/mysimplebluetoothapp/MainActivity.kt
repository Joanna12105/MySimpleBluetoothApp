package com.example.mysimplebluetoothapp

import android.bluetooth.BluetoothAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    private val btnConnectedDevices : Button by lazy { findViewById(R.id.ConnectedDevicesButton) }
    private val btnBluetoothConnection : Button by lazy { findViewById(R.id.searchBluetoothConnectionButton) }
    private val lvConnectedDevices : ListView by lazy{ findViewById(R.id.ConnectedDevicesListView) }

    private lateinit var mBluetooth: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConnectedDevices.setOnClickListener {
            Log.i(TAG, getString(R.string.ConnectedDummyText))
            toast(getString(R.string.ConnectedDummyText))
        }


        btnBluetoothConnection.setOnClickListener {
            Log.i(TAG, getString(R.string.SearchDummyText))
            toast(getString(R.string.SearchDummyText))
        }

        mBluetooth = BluetoothAdapter.getDefaultAdapter()
        if(mBluetooth == null)
        {
            toast(getString(R.string.btNotAvailable))
            finish();
        }
    }


    override fun onResume() {
        super.onResume()
        if (!mBluetooth.isEnabled) {
            val turnBTOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnBTOn, 1)
        }
    }
}