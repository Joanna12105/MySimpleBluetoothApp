package com.example.mysimplebluetoothapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast
import java.io.IOException
import java.util.*

class BTControl : AppCompatActivity() {


    private val TAG = "BTControl Activity"

    private lateinit var address: String
    private lateinit var value: String
    private var isConnected = false
    private var ledIsOn = false
    private var ledFlashing = false
    private var receivingData = false

    private val mHandler: Handler by lazy { Handler() }
    private lateinit var mRunnable: Runnable
    private var mSocket: BluetoothSocket? = null
    private val mBluetooth: BluetoothAdapter by lazy { BluetoothAdapter.getDefaultAdapter() }

    val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val progress: ProgressBar by lazy { findViewById(R.id.progress) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_btcontrol)

        val newint = intent
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS).toString()

        ConnectBT().execute()
    }


    private inner class ConnectBT : AsyncTask<Void, Void, Void>() {
        private var connectSuccess = true

        override fun onPreExecute() {
            toast("Verbinde...")
            progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg devices: Void?): Void? {
            try {
                if (mSocket == null || !isConnected) {
                    val device = mBluetooth.getRemoteDevice(address)
                    mSocket = device.createInsecureRfcommSocketToServiceRecord(mUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    mSocket!!.connect()
                }
            } catch (e: IOException) {
                Log.d(TAG, "Unable to connect")
                e.printStackTrace()
                connectSuccess = false
            }
            return null
        }


    }
}