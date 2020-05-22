package ru.ttk.beacon.ui.module.bluetooth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.core.qualifier.qualifier
import ru.ttk.beacon.R
import ru.ttk.beacon.ui.navigation.RouterType
import ru.ttk.beacon.ui.utils.lifecycleViewModel
import ru.ttk.beacon.ui.utils.livedata.observeEvent

class BluetoothDisabledFragment : Fragment(R.layout.fragment_bluetooth_disabled) {

    private val viewModel by lifecycleViewModel {
        BluetoothDisabledViewModel(
            bluetoothInteractor = get(),
            bluetoothStateObserver = get(),
            router = get(RouterType.FULL_SCREEN.qualifier)
        )
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
}
