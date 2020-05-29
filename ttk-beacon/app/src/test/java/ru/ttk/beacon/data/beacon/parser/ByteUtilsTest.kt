package ru.ttk.beacon.data.beacon.parser

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.ttk.beacon.data.beacon.parser.ByteUtils.toHexStringOrNull
import ru.ttk.beacon.data.beacon.parser.ByteUtils.toUnsignedShortOrNull

class ByteUtilsTest {

    @Test
    fun toUnsignedShortOrNull() {
        assertEquals(65535, byteArrayOf(0xFF.toByte(), 0xFF.toByte()).toUnsignedShortOrNull())
    }

    @Test
    fun toHexStringOrNull() {
        assertEquals("[0x4C, 0x00, 0x02, 0x15]", byteArrayOf(0x4C, 0x00, 0x02, 0x15).toHexStringOrNull())
        assertEquals("[0x4C, 0x00]", byteArrayOf(0x4C, 0x00, 0x02, 0x15).toHexStringOrNull(2))
        assertEquals("[0x4C, 0x00]", byteArrayOf(0x4C, 0x00).toHexStringOrNull(2))
        assertEquals("[0x4C, 0x00]", byteArrayOf(0x4C, 0x00).toHexStringOrNull(200))
    }
}