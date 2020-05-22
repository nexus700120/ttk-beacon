package ru.ttk.beacon.ui.module.scanner.ble

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ttk.beacon.domain.BleDeviceScanner
import ru.ttk.beacon.domain.entity.BleDevice
import ru.ttk.beacon.ui.common.RxViewModel
import timber.log.Timber

class BleDeviceListViewModel(
    private val bleDeviceScanner: BleDeviceScanner
) : RxViewModel() {

    private val _devices = MutableLiveData<List<BleDevice>>()
    val devices: LiveData<List<BleDevice>> = _devices

    private var disposable: Disposable? = null

    fun onStart() {
        disposable = bleDeviceScanner.scan()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.sortedBy { device -> device.mac } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _devices.value = it },
                onError = { Timber.e(it) }
            )
    }

    fun onStop() {
        disposable?.dispose()
    }
}