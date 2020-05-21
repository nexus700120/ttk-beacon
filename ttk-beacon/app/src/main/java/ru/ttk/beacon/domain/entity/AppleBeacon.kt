package ru.ttk.beacon.domain.entity

class AppleBeacon(
    val uuid: String,
    val mac: String,
    val major: Int,
    val minor: Int,
    val rssi: Int,
    val distance: Double
) {
    override fun toString(): String = buildString {
        append("AppleBeacon(")
        append("uuid=$uuid,")
        append("mac=$mac,")
        append("major=$major,")
        append("minor=$minor,")
        append("rssi=$rssi,")
        append("distance=$distance")
        append(")")
    }
}