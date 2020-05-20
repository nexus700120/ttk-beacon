package ru.ttk.beacon.domain.entity

class AppleBeacon(
    val uuid: String,
    val mac: String,
    val major: Int,
    val minor: Int,
    val rssi: Int,
    val distance: Double
)