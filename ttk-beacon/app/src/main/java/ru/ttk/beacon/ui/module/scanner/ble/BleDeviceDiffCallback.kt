package ru.ttk.beacon.ui.module.scanner.ble

import androidx.recyclerview.widget.DiffUtil
import ru.ttk.beacon.domain.entity.BleDevice

class BleDeviceDiffCallback(
    private val old: List<BleDevice>,
    private val new: List<BleDevice>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
        old[oldPos].mac == new[newPos].mac

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldItem = old[oldPos]
        val newItem = new[newPos]
        return oldItem.rssi == newItem.rssi
    }
}