package ru.ttk.beacon.data.beacon.parser

import org.junit.Assert.*
import org.junit.Test

class AppleAdvertisingPacketParserTest {

    private val iBeacon1 =
        byteArrayOf(26, -1, 76, 0, 2, 21, -40, 5, 101, -66, -108, 74, 78, 25, -86, -32, 115, 42, 70, -98, -90, -115, 0, 35, 0, 6, -65)
    private val iBeacon2 =
        byteArrayOf(26, -1, 76, 0, 2, 21, 15, -15, -99, -93, 18, -79, 72, 19, -120, 62, -104, 115, -123, -44, -65, 59, -1, -1, -1, -1, -128)

    @Test
    fun manufacturerValidation() {
        val parser = AppleAdvertisingPacketParser()
        assertTrue(parser.isAppleManufacturer(iBeacon1))
        assertTrue(parser.isAppleManufacturer(byteArrayOf(0x0, 0x0, 0x4C, 0x00, 0x02, 0x15)))
        assertFalse(parser.isAppleManufacturer(byteArrayOf(0x0, 0x0, 0x4B, 0x00, 0x02, 0x15)))
        assertFalse(parser.isAppleManufacturer(byteArrayOf(0x0, 0x0, 0x4C, 0x00, 0x02)))
    }

    @Test
    fun lengthValidation() {
        val parser = AppleAdvertisingPacketParser()
        assertTrue(parser.isLengthValid(iBeacon1))
        assertTrue(parser.isLengthValid(byteArrayOf(26)))
        assertFalse(parser.isLengthValid(byteArrayOf()))
        assertFalse(parser.isLengthValid(byteArrayOf(38)))
    }

    @Test
    fun uuidExtracting() {
        val parser = AppleAdvertisingPacketParser()
        assertEquals("d80565be-944a-4e19-aae0-732a469ea68d", parser.extractUuid(iBeacon1))
        assertNull(parser.extractUuid(byteArrayOf(0, 1, 2, 3, 4, 5, 6)))
        assertNull(parser.extractUuid(byteArrayOf()))
    }

    @Test
    fun extractMajor() {
        val parser = AppleAdvertisingPacketParser()
        assertEquals(35, parser.extractMajor(iBeacon1))
        assertEquals(65535, parser.extractMajor(iBeacon2))
    }

    @Test
    fun extractMinor() {
        val parser = AppleAdvertisingPacketParser()
        assertEquals(6, parser.extractMinor(iBeacon1))
        assertEquals(65535, parser.extractMinor(iBeacon2))
    }

    @Test
    fun extractPower() {
        val parser = AppleAdvertisingPacketParser()
        assertEquals(-65, parser.extractPower(iBeacon1))
        assertEquals(-128, parser.extractPower(iBeacon2))
    }
}