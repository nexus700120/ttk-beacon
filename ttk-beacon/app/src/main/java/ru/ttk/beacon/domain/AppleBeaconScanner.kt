package ru.ttk.beacon.domain

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.domain.entity.AppleBeacon

interface AppleBeaconScanner {
    fun scan(): Observable<List<AppleBeacon>>
}