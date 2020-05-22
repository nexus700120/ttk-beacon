package ru.ttk.beacon.ui.module.scanner.ble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.BleDevice
import ru.ttk.beacon.ui.common.BlePropsFormatter.formatRssi

class BleDeviceAdapter : RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {

    private val items = mutableListOf<BleDevice>()

    fun update(devices: List<BleDevice>) {
        val diffCallback = BleDeviceDiffCallback(items, devices)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.apply {
            clear()
            addAll(devices)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ble_device_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameView = view.findViewById<TextView>(R.id.name)
        private val macView = view.findViewById<TextView>(R.id.mac_value)
        private val rssiView = view.findViewById<TextView>(R.id.rssi_value)

        fun bind(device: BleDevice) {
            nameView.text = device.name ?: itemView.context.getString(R.string.unknown_name)
            macView.text = device.mac
            rssiView.text = formatRssi(device.rssi)
        }
    }
}
