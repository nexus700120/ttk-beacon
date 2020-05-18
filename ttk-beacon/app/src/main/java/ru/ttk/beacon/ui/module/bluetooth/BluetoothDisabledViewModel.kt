package ru.ttk.beacon.ui.module.bluetooth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ttk.beacon.ui.common.RxViewModel
import ru.ttk.beacon.ui.common.bluetooth.BluetoothState
import ru.ttk.beacon.ui.common.bluetooth.BluetoothStateObserver
import ru.ttk.beacon.ui.utils.livedata.UnitEvent
import timber.log.Timber

class BluetoothDisabledViewModel(
    private val bluetoothInteractor: BluetoothInteractor,
    private val bluetoothStateObserver: BluetoothStateObserver
) : RxViewModel() {

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private val _showError = MutableLiveData<UnitEvent>()
    val showError: LiveData<UnitEvent> = _showError

    private var stateDisposable: Disposable? = null
    private var beaconListScreenOpened = false

    fun enableBluetooth() {
        bluetoothInteractor.enableBluetooth()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _showProgress.value = true }
            .doAfterTerminate { _showProgress.value = false }
            .subscribeBy(
                onComplete = { openBeaconList() },
                onError = {
                    Timber.e(it)
                    _showError.value = UnitEvent()
                })
            .bindToLifeCycle()
    }

    fun onForeground() {
        stateDisposable = bluetoothStateObserver.observe()
            .filter { it == BluetoothState.ON }
            .subscribeBy { openBeaconList() }
    }

    fun onBackground() {
        stateDisposable?.dispose()
    }

    private fun openBeaconList() {
        if (!beaconListScreenOpened) {
            beaconListScreenOpened = true
            Timber.d("Opening beacon list screen")
        }
    }
}