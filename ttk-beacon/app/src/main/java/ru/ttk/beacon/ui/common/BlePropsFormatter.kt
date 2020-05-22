package ru.ttk.beacon.ui.common

import java.text.DecimalFormat

object BlePropsFormatter {

    private val distanceFormat = DecimalFormat("0.00")

    fun formatRssi(rssi: Int): String = "$rssi dbm"

    fun formatDistance(distance: Double): String =
        "${distanceFormat.format(distance)} m"
}