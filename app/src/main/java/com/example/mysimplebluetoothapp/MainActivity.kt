package com.example.mysimplebluetoothapp


import android.bluetooth.BluetoothAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    private val btnPairedDevices : Button by lazy { findViewById(R.id.PairedDevicesButton) }
    private val btnBluetoothConnection : Button by lazy { findViewById(R.id.searchBluetoothConnectionButton) }
    private val lvPairedDevices : ListView by lazy{ findViewById(R.id.PairedDevicesListView) }

    private lateinit var mBluetooth: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetooth = BluetoothAdapter.getDefaultAdapter()
        if(mBluetooth == null)
        {
            toast(getString(R.string.btNotAvailable))
            finish();
        }

        btnPairedDevices.setOnClickListener {
            Log.i(TAG, getString(R.string.ConnectedDummyText))
            toast(getString(R.string.ConnectedDummyText))
            getPairedDevices()
        }

        btnBluetoothConnection.setOnClickListener {
            Log.i(TAG, getString(R.string.SearchDummyText))
            toast(getString(R.string.SearchDummyText))
        }

    }


    override fun onResume() {
        super.onResume()
        if (!mBluetooth.isEnabled) {
            val turnBTOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnBTOn, 1)
        }
    }

    private fun getPairedDevices() {

        val pairedDevices = mBluetooth!!.bondedDevices
        val list = ArrayList<Any>()

        if (pairedDevices.size > 0) {
            for (bt in pairedDevices) {
                list.add("""${bt.name}${bt.address}""".trimIndent())
            }
        } else {
            toast(getString(R.string.btNoPairedDevices))
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item,
            list)
        lvPairedDevices.adapter = adapter
    }
}