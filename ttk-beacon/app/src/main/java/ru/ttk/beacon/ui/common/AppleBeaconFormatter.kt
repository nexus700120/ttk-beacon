package ru.ttk.beacon.ui.common

import ru.ttk.beacon.domain.entity.AppleBeacon
import java.text.DecimalFormat

class AppleBeaconFormatter {

    private val distanceFormat = DecimalFormat("0.00")

    fun formatRssi(beacon: AppleBeacon): String = "${beacon.rssi} dbm"

    fun formatDistance(beacon: AppleBeacon): String =
        "${distanceFormat.format(beacon.distance)} m"
}