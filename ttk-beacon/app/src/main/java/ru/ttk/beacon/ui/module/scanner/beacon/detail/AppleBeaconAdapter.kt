package ru.ttk.beacon.ui.module.scanner.beacon.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.common.AppleBeaconFormatter
import ru.ttk.beacon.ui.common.AppleBeaconFormatter.formatDistance
import ru.ttk.beacon.ui.common.AppleBeaconFormatter.formatRssi

class AppleBeaconAdapter(private val context: Context) : RecyclerView.Adapter<AppleBeaconAdapter.RowViewHolder>() {

    private val items = mutableListOf<Pair<String, String>>()

    fun setBeacon(beacon: AppleBeacon?) {
        val newItems = listOf(
            context.getString(R.string.mac) to (beacon?.mac ?: UNKNOWN),
            context.getString(R.string.uuid) to (beacon?.uuid ?: UNKNOWN),
            context.getString(R.string.major) to (beacon?.major?.toString() ?: UNKNOWN),
            context.getString(R.string.minor) to (beacon?.minor?.toString() ?: UNKNOWN),
            context.getString(R.string.rssi) to (beacon?.let { formatRssi(it) } ?: UNKNOWN),
            context.getString(R.string.distance) to (beacon?.let { formatDistance(it) } ?: UNKNOWN)
        )
        val diffCallback = AppleBeaconDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.apply {
            clear()
            addAll(newItems)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_apple_beacon, parent, false)
        return RowViewHolder(view)
    }

    override fun getItemCount(): Int = 6

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val (title, value) = items[position]
        holder.bind(title, value)
    }

    class RowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val titleView = view.findViewById<TextView>(R.id.title)
        private val valueView = view.findViewById<TextView>(R.id.value)

        fun bind(title: String, value: String) {
            titleView.text = title
            valueView.text = value
        }
    }

    companion object {
        private const val UNKNOWN = "—"
    }
}