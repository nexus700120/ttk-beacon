package ru.ttk.beacon.ui.module.scanner.beacon.detail

import androidx.recyclerview.widget.DiffUtil

class AppleBeaconDiffCallback(
    private val old: List<Pair<String, String>>,
    private val new: List<Pair<String, String>>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
        old[oldPos].first == new[newPos].first

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
        old[oldPos].second == new[newPos].second
}