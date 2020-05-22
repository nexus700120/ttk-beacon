package ru.ttk.beacon.ui.module.scanner.beacon.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.common.RxViewModel
import timber.log.Timber

class AppleBeaconListViewModel(private val scanner: AppleBeaconScanner) : RxViewModel() {

    private var disposable: Disposable? = null

    private val _beacons = MutableLiveData<List<AppleBeacon>>()
    val beacons: LiveData<List<AppleBeacon>> = _beacons

    fun onResume() {
        disposable = scanner.scan()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.sortedWith(compareBy({ beacon -> beacon.distance }, { beacon -> beacon.mac })) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _beacons.value = it },
                onError = { Timber.e(it) }
            )
    }

    fun onPause() {
        disposable?.dispose()
    }
}
