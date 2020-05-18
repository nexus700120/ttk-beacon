package ru.ttk.beacon.ui.module.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

interface BluetoothInteractor {
    fun enableBluetooth(): Completable
}

class BluetoothInteractorImpl(private val appContext: Context) : BluetoothInteractor {

    override fun enableBluetooth(): Completable {
        val adapter = BluetoothAdapter.getDefaultAdapter()
            ?: return Completable.error(Exception("Bluetooth not supported by device"))
        if (adapter.isEnabled) {
            return Completable.complete()
        }
        adapter.enable()
        return Observable.interval(0, 1, TimeUnit.SECONDS)
            .flatMap { if (it >= TIMEOUT_IN_SECS) Observable.error(TimeoutException()) else Observable.just(it) }
            .map { adapter.isEnabled }
            .distinctUntilChanged()
            .takeUntil { it }
            .filter { it }
            .ignoreElements()
    }

    companion object {
        private const val TIMEOUT_IN_SECS = 10L
    }
}