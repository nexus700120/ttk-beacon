package ru.ttk.beacon.data.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ttk.beacon.BuildConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class BleRawScanner {

    private val leScanner by lazy {
        BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner
            ?: error("Bluetooth not supported by device")
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val subject = PublishSubject.create<ScanResult>().toSerialized()
    private val isScannerStarted = AtomicBoolean(false)

    private val scanCallback = object : ScanCallback() {

        override fun onScanFailed(errorCode: Int) {
            val message = when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> "Scan failed: a BLE scan with the same settings is already started by the app"
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "Scan failed: app cannot be registered"
                SCAN_FAILED_FEATURE_UNSUPPORTED -> "Scan failed: power optimized scan feature is not supported"
                SCAN_FAILED_INTERNAL_ERROR -> "Scan failed: internal error"
                else -> "Scan failed with unknown error (errorCode=$errorCode)"
            }
            Timber.d(message)
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (BuildConfig.DEBUG) {
                Timber.d("onScanResult: ${result.device.address}")
            }
            subject.onNext(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            if (BuildConfig.DEBUG) {
                Timber.d("onBatchScanResults: ${results.joinToString { it.device.address }}")
            }
            results.forEach { subject.onNext(it) }
        }
    }

    @Suppress("SpellCheckingInspection")
    private val connectable = subject
        .buffer(BUFFER_TIMESPAN, TimeUnit.SECONDS)
        .map { results -> results.associateBy { it.device.address }.values.toList() }
        .replay(1)
        .refCount()
        .doOnSubscribe {
            if (!isScannerStarted.get()) {
                Timber.d("Start scanning")
                isScannerStarted.set(true)
                leScanner.startScan(listOf(), scanSettings, scanCallback)
            }
        }.doFinally {
            if (isScannerStarted.get() && !subject.hasObservers()) {
                Timber.d("Stop scanning")
                isScannerStarted.set(false)
                leScanner.stopScan(scanCallback)
            }
        }

    fun scan(): Observable<List<ScanResult>> = connectable

    companion object {
        @Suppress("SpellCheckingInspection")
        private const val BUFFER_TIMESPAN = 2L
    }
}