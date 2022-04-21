package com.example.mysimplebluetoothapp


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    companion object {
        const val EXTRA_ADDRESS = "device_address"
    }
    private val btnPairedDevices : Button by lazy { findViewById(R.id.PairedDevicesButton) }
    private val btnBluetoothConnection : Button by lazy { findViewById(R.id.searchBluetoothConnectionButton) }
    private val lvPairedDevices : ListView by lazy{ findViewById(R.id.PairedDevicesListView) }

    private var discoveredDevices = arrayListOf<String>()
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
            getPairedDevices()
        }

        btnBluetoothConnection.setOnClickListener {
            Log.i(TAG, getString(R.string.SearchDummyText))
            checkBTPermission()
            getDiscoverDevices()
        }

        lvPairedDevices.onItemClickListener = lvOnClickListener

    }


    private val lvOnClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
        // stoppe weitere Suche
        if (mBluetooth?.isDiscovering) {
            mBluetooth.cancelDiscovery()
            unregisterReceiver(mBroadcastReceiver);
        }
        btnBluetoothConnection.text = getString(R.string.startSearchDevice);
        // MAC Adresse des ausgewählten Devices an BTControl übergeben
        val info = (view as TextView).text.toString()
        val address = info.substring(info.length - 17)
        toast (address)
        val i = Intent(this@MainActivity, BTControl::class.java)
        i.putExtra(EXTRA_ADDRESS, address)
        startActivity(i)
    }


    override fun onResume() {
        super.onResume()
        if (!mBluetooth.isEnabled) {
            val turnBTOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnBTOn, 1)
        }
    }






    private fun getPairedDevices() {

        val pairedDevices = mBluetooth.bondedDevices
        val list = ArrayList<Any>()

        if (pairedDevices.size > 0) {
            for (bt in pairedDevices) {
                list.add("""${bt.name}${bt.address}""".trimIndent())
            }
        } else {
            toast(getString(R.string.btNoPairedDevices))
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter(this,
            androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
            list)
        lvPairedDevices.adapter = adapter
    }




    private fun checkBTPermission() {
        var permissionCheck = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
        permissionCheck += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
        if (permissionCheck != 0) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 1001)
        }
    }


    private fun getDiscoverDevices() {
        if(!mBluetooth.isDiscovering) { // Suche ist nicht gestartet
            mBluetooth.startDiscovery()  // starte Suche
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND) //auf diese Signale soll unser Broadcast Receiver filtern
            registerReceiver(mBroadcastReceiver, discoverDevicesIntent)
            btnBluetoothConnection.text = getString(R.string.stopSearchDevice);
        } else {                        // Suche ist gestartet
            mBluetooth.cancelDiscovery() // Stoppe suche
            unregisterReceiver(mBroadcastReceiver);
            btnBluetoothConnection.text = getString(R.string.startSearchDevice);
        }
    }


    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val deviceInfo = """${device!!.name}${device.address}""".trimIndent()
                Log.i(TAG, deviceInfo)

                // gefundenes Gerät der Liste hinzufügen, wenn es noch nicht aufgeführt ist
                if (!discoveredDevices.contains(deviceInfo)) {
                    discoveredDevices.add(deviceInfo)
                }

                // aktualisierte Liste im Listview anzeigen
                val adapt = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, discoveredDevices)
                lvPairedDevices.adapter = adapt
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
        mBluetooth.cancelDiscovery()
    }



}