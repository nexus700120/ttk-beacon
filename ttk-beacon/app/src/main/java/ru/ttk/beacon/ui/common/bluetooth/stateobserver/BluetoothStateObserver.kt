package ru.ttk.beacon.ui.common.bluetooth.stateobserver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class BluetoothStateObserver(private val appContext: Context) {

    fun observe(): Observable<BluetoothState> {
        val adapter = BluetoothAdapter.getDefaultAdapter()
            ?: return Observable.just(BluetoothState.UNAVAILABLE)
        val currentState = if (adapter.isEnabled) BluetoothState.ON else BluetoothState.OFF
        val subject = BehaviorSubject.createDefault(currentState)
        val receiver = BluetoothBroadCastReceiver { subject.onNext(it) }
        appContext.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        return subject.distinctUntilChanged()
            .doFinally { appContext.unregisterReceiver(receiver) }
    }

    private class BluetoothBroadCastReceiver(private val callback: (BluetoothState) -> Unit) : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                val internalState = if (state == BluetoothAdapter.STATE_ON) BluetoothState.ON else BluetoothState.OFF
                callback(internalState)
            }
        }
    }
}