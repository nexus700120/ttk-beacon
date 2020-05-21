package ru.ttk.beacon.ui.module.apple.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.common.RxViewModel
import ru.ttk.beacon.ui.utils.Optional
import timber.log.Timber

class AppleBeaconViewModel(
    private val scanner: AppleBeaconScanner,
    private val initialBeacon: AppleBeacon
) : RxViewModel() {

    private val _beacon = MutableLiveData<Optional<AppleBeacon>>()
    val beacon: LiveData<Optional<AppleBeacon>> = _beacon

    private var disposable: Disposable? = null

    fun onStart() {
        disposable = scanner.scan(initialBeacon.mac)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _beacon.value = it }, { Timber.e(it) })
    }

    fun onStop() {
        disposable?.dispose()
    }
}