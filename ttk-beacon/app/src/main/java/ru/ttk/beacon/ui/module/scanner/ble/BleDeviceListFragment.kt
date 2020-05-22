package ru.ttk.beacon.ui.module.scanner.ble

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.android.ext.android.get
import ru.ttk.beacon.R
import ru.ttk.beacon.ui.module.scanner.beacon.list.AppleBeaconListViewModel
import ru.ttk.beacon.ui.utils.viewModelFactory

class BleDeviceListFragment : Fragment(R.layout.fragment_ble_devices) {

    private val viewModel by viewModels<BleDeviceListViewModel> {
        viewModelFactory { BleDeviceListViewModel(get()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BleDeviceAdapter()
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL).apply {
                val drawable = requireNotNull(ContextCompat.getDrawable(view.context, R.drawable.divider))
                setDrawable(drawable)
            })
            itemAnimator.let {
                if (it is SimpleItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }
            setAdapter(adapter)
        }
        val scanning = view.findViewById<View>(R.id.scanning)

        viewModel.devices.observe(viewLifecycleOwner) {
            scanning.isInvisible = it.isNotEmpty()
            adapter.update(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }
}