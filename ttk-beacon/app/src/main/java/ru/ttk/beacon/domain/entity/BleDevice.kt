package ru.ttk.beacon.domain.entity

class BleDevice(
    val mac: String,
    val rssi: Int
) {
    override fun toString(): String = buildString {
        append("BleDevice(")
        append("mac=$mac,")
        append("rssi=$rssi")
        append(")")
    }
}