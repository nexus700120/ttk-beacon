package ru.ttk.beacon.ui.module.scanner.beacon.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.common.AppleBeaconFormatter.formatDistance
import ru.ttk.beacon.ui.common.AppleBeaconFormatter.formatRssi

class AppleBeaconListAdapter(
    private val clickListener: (AppleBeacon) -> Unit
) : RecyclerView.Adapter<AppleBeaconListAdapter.BeaconViewHolder>() {

    private val items = mutableListOf<AppleBeacon>()

    fun update(items: List<AppleBeacon>) {
        val diffCallback = AppleBeaconListDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.apply {
            clear()
            addAll(items)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_apple_beacons_list, parent, false)
        return BeaconViewHolder(view) {
            clickListener(items[it])
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class BeaconViewHolder(
        view: View,
        private val clickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val macView = view.findViewById<TextView>(R.id.mac_value)
        private val distanceView = view.findViewById<TextView>(R.id.distance)
        private val uuidView = view.findViewById<TextView>(R.id.uuid_value)
        private val majorView = view.findViewById<TextView>(R.id.major_value)
        private val minorView = view.findViewById<TextView>(R.id.minor_value)
        private val rssiView = view.findViewById<TextView>(R.id.rssi_value)

        init {
            itemView.setOnClickListener { clickListener(adapterPosition) }
        }

        @SuppressLint("SetTextI18n")
        fun bind(beacon: AppleBeacon) {
            macView.text = beacon.mac
            distanceView.text = formatDistance(beacon)
            uuidView.text = beacon.uuid
            majorView.text = beacon.major.toString()
            minorView.text = beacon.minor.toString()
            rssiView.text = formatRssi(beacon)
        }
    }
}
