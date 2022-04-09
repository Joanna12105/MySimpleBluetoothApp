package com.example.mysimplebluetoothapp

import android.content.ContentValues.TAG
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

    }
}