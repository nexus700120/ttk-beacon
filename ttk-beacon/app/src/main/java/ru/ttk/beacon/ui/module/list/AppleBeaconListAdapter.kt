package ru.ttk.beacon.ui.module.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.AppleBeacon
import java.text.DecimalFormat

class AppleBeaconListAdapter : RecyclerView.Adapter<AppleBeaconListAdapter.BeaconViewHolder>() {

    private val items = mutableListOf<AppleBeacon>()

    fun update(items: List<AppleBeacon>) {
        val diffCallback = AppleBeaconDiffCallback(this.items, items)
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
        return BeaconViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class BeaconViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val macView = view.findViewById<TextView>(R.id.mac_value)
        private val distanceView = view.findViewById<TextView>(R.id.distance)
        private val uuidView = view.findViewById<TextView>(R.id.uuid_value)
        private val majorView = view.findViewById<TextView>(R.id.major_value)
        private val minorView = view.findViewById<TextView>(R.id.minor_value)
        private val rssiView = view.findViewById<TextView>(R.id.rssi_value)

        @SuppressLint("SetTextI18n")
        fun bind(beacon: AppleBeacon) {
            macView.text = beacon.mac
            distanceView.text = beacon.distance.toPrettyDistance()
            uuidView.text = beacon.uuid
            majorView.text = beacon.major.toString()
            minorView.text = beacon.minor.toString()
            rssiView.text = "${beacon.rssi} dbm"
        }

        private fun Double.toPrettyDistance(): String = buildString {
            append(DecimalFormat("0.00").format(this@toPrettyDistance))
            append(" m")
        }
    }
}