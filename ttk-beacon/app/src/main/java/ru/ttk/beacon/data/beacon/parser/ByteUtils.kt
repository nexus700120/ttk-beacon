package ru.ttk.beacon.data.beacon.parser

import at.favre.lib.bytes.Bytes

object ByteUtils {

    fun ByteArray.toUnsignedShortOrNull(): Int? = runCatching {
        require(size == 2)
        Bytes.wrap(byteArrayOf(0x00, 0x00, first(), last())).toInt()
    }.getOrNull()

    fun ByteArray.toUuidStringOrNull(): String? = runCatching {
        Bytes.wrap(this).toUUID().toString()
    }.getOrNull()

    fun ByteArray.toHexStringOrNull(limit: Int? = null): String? = runCatching {
        val array = if (limit == null || limit >= size) {
            this
        } else {
            sliceArray(IntRange(0, limit - 1))
        }
        Bytes.wrap(array)
            .encodeHex(true)
            .chunked(2)
            .joinToString(prefix = "[", postfix = "]") { "0x$it" }
    }.getOrNull()

}