package ru.ttk.beacon.ui.module.scanner.beacon.list

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.android.ext.android.get
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.module.scanner.beacon.detail.AppleBeaconBottomSheet
import ru.ttk.beacon.ui.utils.lifecycleViewModel

class AppleBeaconListFragment : Fragment(R.layout.fragment_apple_beacons) {

    private val viewModel by lifecycleViewModel { AppleBeaconListViewModel(get()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AppleBeaconListAdapter { showBottomSheet(it) }
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

        viewModel.beacons.observe(viewLifecycleOwner) {
            scanning.isInvisible = it.isNotEmpty()
            adapter.update(it)
        }
    }

    private fun showBottomSheet(beacon: AppleBeacon) {
        AppleBeaconBottomSheet.show(parentFragmentManager, beacon)
    }
}