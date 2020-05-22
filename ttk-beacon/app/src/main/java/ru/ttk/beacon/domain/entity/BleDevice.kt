package ru.ttk.beacon.domain.entity

class BleDevice(
    val name: String?,
    val mac: String,
    val rssi: Int
) {
    override fun toString(): String = buildString {
        append("BleDevice(")
        append("name=$name")
        append("mac=$mac,")
        append("rssi=$rssi")
        append(")")
    }
}