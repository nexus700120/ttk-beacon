package ru.ttk.beacon.domain

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.domain.entity.BleDevice

interface BleDeviceScanner {
    fun scan(): Observable<List<BleDevice>>
}