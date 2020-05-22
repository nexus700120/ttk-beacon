package ru.ttk.beacon.ui.module.scanner.beacon.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.common.RxViewModel
import ru.ttk.beacon.ui.utils.Optional
import ru.ttk.beacon.ui.utils.toOptional
import timber.log.Timber

class AppleBeaconViewModel(
    private val scanner: AppleBeaconScanner,
    private val initialBeacon: AppleBeacon
) : RxViewModel() {

    private val _beacon = MutableLiveData<Optional<AppleBeacon>>().apply {
        value = initialBeacon.toOptional()
    }
    val beacon: LiveData<Optional<AppleBeacon>> = _beacon

    override fun onResume() {
        super.onResume()
        scanner.scan(initialBeacon.mac)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { _beacon.value = it }, onError = { Timber.e(it) })
            .unsubscribeOnPause()
    }
}
