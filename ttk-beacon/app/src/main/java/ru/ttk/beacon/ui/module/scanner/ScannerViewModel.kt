package ru.ttk.beacon.ui.module.scanner

import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.terrakok.cicerone.Router
import ru.ttk.beacon.ui.common.RxViewModel
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothState
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothStateObserver
import ru.ttk.beacon.ui.navigation.Screens
import timber.log.Timber

class ScannerViewModel(
    private val stateObserver: BluetoothStateObserver,
    private val router: Router
) : RxViewModel() {

    override fun onResume() {
        super.onResume()
        stateObserver.observe()
            .filter { it != BluetoothState.ON }
            .subscribeBy(onNext = ::handleState, onError = { Timber.e(it) })
            .unsubscribeOnPause()
    }

    private fun handleState(state: BluetoothState) = when (state) {
        BluetoothState.OFF -> router.newRootScreen(Screens.BluetoothDisabled)
        BluetoothState.UNAVAILABLE -> router.newRootScreen(Screens.NotSupported)
        else -> error("Unexpected state")
    }
}
