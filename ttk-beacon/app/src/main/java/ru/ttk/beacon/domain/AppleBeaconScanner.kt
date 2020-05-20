package ru.ttk.beacon.domain

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.utils.Optional

interface AppleBeaconScanner {

    fun scan(): Observable<List<AppleBeacon>>

    fun scan(uuid: String): Observable<Optional<AppleBeacon>>
}