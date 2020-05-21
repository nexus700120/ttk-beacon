package ru.ttk.beacon.data

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.domain.BleDeviceScanner
import ru.ttk.beacon.domain.entity.BleDevice
import timber.log.Timber

class BleDeviceScannerImpl(private val bleRawScanner: BleRawScanner) : BleDeviceScanner {

    override fun scan(): Observable<List<BleDevice>> = bleRawScanner.scan()
        .doOnNext {
           Timber.d("onNext")
        }
        .map { list ->
            list.map { BleDevice(mac = it.device.address, rssi = it.rssi) }
                .sortedBy { it.mac }
        }
}