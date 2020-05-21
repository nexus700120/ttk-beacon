package ru.ttk.beacon.data.beacon.parser

import androidx.annotation.VisibleForTesting
import java.nio.ByteBuffer
import java.util.*

class AppleAdvertisingPacketParser {

    fun parse(byteArray: ByteArray?): AppleAdvertisingPacket? {
        byteArray ?: return null
        if (isLengthValid(byteArray) && isAppleManufacturer(byteArray)) {
            return AppleAdvertisingPacket(
                uuid = extractUuid(byteArray) ?: return null,
                major = extractMajor(byteArray) ?: return null,
                minor = extractMinor(byteArray) ?: return null,
                txPower = extractPower(byteArray) ?: return null
            )
        }
        return null
    }

    @VisibleForTesting
    fun isLengthValid(byteArray: ByteArray): Boolean =
        byteArray.firstOrNull() == PACKET_LENGTH

    @VisibleForTesting
    fun isAppleManufacturer(byteArray: ByteArray): Boolean {
        if (isRangeInBounds(byteArray, manufacturerRange)) {
            return byteArray.sliceArray(manufacturerRange) contentEquals appleManufacturer
        }
        return false
    }

    @VisibleForTesting
    fun extractUuid(byteArray: ByteArray): String? {
        if (isRangeInBounds(byteArray, uuidRange)) {
            val uuidBytes = byteArray.sliceArray(uuidRange)
            val buf = ByteBuffer.wrap(uuidBytes).asLongBuffer()
            return UUID(buf.get(), buf.get()).toString()
                .takeUnless { it.isEmpty() }
        }
        return null
    }

    @VisibleForTesting
    fun extractMajor(byteArray: ByteArray): Int? {
        if (isRangeInBounds(byteArray, majorRange)) {
            return byteArray.sliceArray(majorRange).toInt()
        }
        return null
    }

    @VisibleForTesting
    fun extractMinor(byteArray: ByteArray): Int? {
        if (isRangeInBounds(byteArray, minorRange)) {
            return byteArray.sliceArray(minorRange).toInt()
        }
        return null
    }

    @VisibleForTesting
    fun extractPower(byteArray: ByteArray): Int? =
        byteArray.getOrNull(POWER_INDEX)?.toInt()

    private fun isRangeInBounds(byteArray: ByteArray, range: IntRange): Boolean =
        byteArray.isNotEmpty() && range.first <= byteArray.lastIndex && range.last <= byteArray.lastIndex

    private fun ByteArray.toInt(): Int {
        require(size <= 2) { "max 2 bytes" }
        return ((first().toInt() and 0xff) shl 8) or (last().toInt() and 0xff)
    }

    companion object {
        private const val PACKET_LENGTH: Byte = 26.toByte()
        private const val POWER_INDEX = 26

        private val appleManufacturer = byteArrayOf(0x4C, 0x00, 0x02, 0x15)
        private val manufacturerRange = IntRange(2, 5)

        private val uuidRange = IntRange(6, 21)
        private val majorRange = IntRange(22, 23)
        private val minorRange = IntRange(24, 25)
    }
}