package ru.ttk.beacon.data.beacon

import kotlin.math.pow

class DistanceCalculator {

    fun calculateDistance(txPower: Int, rssi: Int): Double {
        if (rssi == 0) {
            return -1.0
        }
        val ratio = rssi * 1.0 / txPower
        return if (ratio < 1.0) {
            ratio.pow(10.0)
        } else {
            COEFFICIENT_1 * ratio.pow(COEFFICIENT_2) + COEFFICIENT_3
        }
    }

    companion object {
        // default coefficients from AltBeacon
        private const val COEFFICIENT_1 = 0.42093
        private const val COEFFICIENT_2 = 6.9476
        private const val COEFFICIENT_3 = 0.54992

    }
}
