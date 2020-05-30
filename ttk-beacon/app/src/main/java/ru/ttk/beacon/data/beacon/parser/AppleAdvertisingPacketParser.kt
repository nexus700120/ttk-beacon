package ru.ttk.beacon.data.beacon.parser

import androidx.annotation.VisibleForTesting
import ru.ttk.beacon.data.beacon.parser.ByteUtils.sliceArrayOrNull
import ru.ttk.beacon.data.beacon.parser.ByteUtils.toUnsignedShortOrNull
import ru.ttk.beacon.data.beacon.parser.ByteUtils.toUuidStringOrNull

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
    fun isAppleManufacturer(byteArray: ByteArray): Boolean =
        byteArray.sliceArrayOrNull(manufacturerRange)
            ?.contentEquals(appleManufacturer) ?: false

    @VisibleForTesting
    fun extractUuid(byteArray: ByteArray): String? =
        byteArray.sliceArrayOrNull(uuidRange)
            ?.toUuidStringOrNull()

    @VisibleForTesting
    fun extractMajor(byteArray: ByteArray): Int? =
        byteArray.sliceArrayOrNull(majorRange)
            ?.toUnsignedShortOrNull()

    @VisibleForTesting
    fun extractMinor(byteArray: ByteArray): Int? =
        byteArray.sliceArrayOrNull(minorRange)
            ?.toUnsignedShortOrNull()

    @VisibleForTesting
    fun extractPower(byteArray: ByteArray): Int? =
        byteArray.getOrNull(POWER_INDEX)?.toInt()

    companion object {
        private const val PACKET_LENGTH: Byte = 0x1A.toByte()
        private const val POWER_INDEX = 26

        private val appleManufacturer = byteArrayOf(0x4C, 0x00, 0x02, 0x15)
        private val manufacturerRange = IntRange(2, 5)

        private val uuidRange = IntRange(6, 21)
        private val majorRange = IntRange(22, 23)
        private val minorRange = IntRange(24, 25)
    }
}