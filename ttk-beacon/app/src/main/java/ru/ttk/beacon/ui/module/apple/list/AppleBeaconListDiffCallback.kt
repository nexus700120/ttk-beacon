package ru.ttk.beacon.ui.module.apple.list

import androidx.recyclerview.widget.DiffUtil
import ru.ttk.beacon.domain.entity.AppleBeacon

class AppleBeaconListDiffCallback(
    private val old: List<AppleBeacon>,
    private val new: List<AppleBeacon>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
        old[oldPos].mac == new[newPos].mac

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldBeacon = old[oldPos]
        val newBeacon = new[newPos]
        return oldBeacon.distance == newBeacon.distance
            && oldBeacon.major == newBeacon.major
            && oldBeacon.minor == newBeacon.minor
            && oldBeacon.rssi == newBeacon.rssi
            && oldBeacon.uuid == newBeacon.uuid
    }
}