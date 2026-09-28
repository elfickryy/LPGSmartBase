package id.lpgsmartbase.data.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import java.util.UUID

object BluetoothPrinterManager {
    private const val SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    private var socket: BluetoothSocket? = null
    private var connectedAddress: String? = null
    fun paired(context: Context): Result<Set<BluetoothDevice>> = runCatching {
        val adapter = BluetoothAdapter.getDefaultAdapter() ?: error("Bluetooth tidak tersedia")
        check(adapter.isEnabled) { "Bluetooth mati" }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != android.content.pm.PackageManager.PERMISSION_GRANTED) error("Izin Bluetooth ditolak")
        adapter.bondedDevices
    }
    @Synchronized fun connect(context: Context, address: String): Result<Unit> = runCatching {
        if (connectedAddress == address && socket?.isConnected == true) return@runCatching
        disconnect()
        val device = paired(context).getOrThrow().firstOrNull { it.address == address } ?: error("Printer default tidak ditemukan")
        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID)).also { it.connect() }
        connectedAddress = address
    }
    @Synchronized fun disconnect(): Result<Unit> = runCatching {
        socket?.close()
        socket = null
        connectedAddress = null
    }
    @Synchronized fun print(context: Context, address: String, text: String): Result<Unit> = runCatching {
        connect(context, address).getOrThrow()
        socket?.outputStream?.use { it.write(text.toByteArray()); it.flush() } ?: error("Koneksi printer terputus")
        disconnect().getOrThrow()
    }
}
