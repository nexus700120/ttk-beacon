package ru.ttk.beacon.ui.module.bluetooth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import ru.ttk.beacon.R
import ru.ttk.beacon.ui.common.bluetooth.BluetoothStateObserver
import ru.ttk.beacon.ui.utils.livedata.observeEvent
import ru.ttk.beacon.ui.utils.viewModelFactory

class BluetoothDisabledFragment : Fragment(R.layout.fragment_bluetooth_disabled) {

    private val viewModel by viewModels<BluetoothDisabledViewModel> {
        viewModelFactory { BluetoothDisabledViewModel(get(), get()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<View>(R.id.enable_bluetooth).apply {
            setOnClickListener { viewModel.enableBluetooth() }
        }
        val progress = view.findViewById<View>(R.id.progress)

        viewModel.showProgress.observe(viewLifecycleOwner) {
            progress.isVisible = it
            button.isVisible = !progress.isVisible
        }
        viewModel.showError.observeEvent(viewLifecycleOwner) {
            Snackbar.make(view, R.string.unknown_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onForeground()
    }

    override fun onStop() {
        viewModel.onBackground()
        super.onStop()
    }
}
