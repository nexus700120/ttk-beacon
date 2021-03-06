package ru.ttk.beacon.ui.module.scanner.beacon.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.get
import ru.ttk.beacon.R
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.utils.lifecycleViewModel

class AppleBeaconBottomSheet : BottomSheetDialogFragment() {

    private val beacon by lazy {
        requireNotNull(arguments?.getParcelable<ParcelableAppleBeacon>(ARG_BEACON)).beacon
    }

    private val viewModel by lifecycleViewModel { AppleBeaconViewModel(get(), beacon) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_apple_beacon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AppleBeaconAdapter(requireContext().applicationContext).also {
            it.setBeacon(beacon)
        }
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(view.context)
            itemAnimator.let {
                if (it is SimpleItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }
            setHasFixedSize(true)
            setAdapter(adapter)
        }

        viewModel.beacon.observe(viewLifecycleOwner) {
            adapter.setBeacon(it.toNullable())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    peekHeight = Int.MAX_VALUE
                }
            }
        }

    companion object {
        private const val ARG_BEACON = "arg_beacon"

        fun show(fm: FragmentManager, beacon: AppleBeacon) {
            val tag = AppleBeaconBottomSheet::class.java.name
            if (fm.findFragmentByTag(tag) == null) {
                val parcelable = ParcelableAppleBeacon(beacon)
                AppleBeaconBottomSheet()
                    .apply { arguments = bundleOf(ARG_BEACON to parcelable) }
                    .show(fm, tag)
            }
        }
    }
}