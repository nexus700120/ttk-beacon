package ru.ttk.beacon.di

import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.ttk.beacon.data.BleRawScanner
import ru.ttk.beacon.data.beacon.AppleBeaconScannerImpl2
import ru.ttk.beacon.data.beacon.DistanceCalculator
import ru.ttk.beacon.data.beacon.parser.AppleAdvertisingPacketParser
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.ui.common.bluetooth.BluetoothStateObserver
import ru.ttk.beacon.ui.module.bluetooth.BluetoothInteractor
import ru.ttk.beacon.ui.module.bluetooth.BluetoothInteractorImpl
import ru.ttk.beacon.ui.navigation.RouterType
import ru.ttk.beacon.ui.utils.BleHelper

private val fullScreenQualifier = RouterType.FULL_SCREEN.qualifier

val navModule = module {
    single(fullScreenQualifier) { Cicerone.create() }
    single(fullScreenQualifier) { get<Cicerone<Router>>(fullScreenQualifier).router }
    single(fullScreenQualifier) { get<Cicerone<Router>>(fullScreenQualifier).navigatorHolder }
}

val bluetoothModule = module {
    factory<BluetoothInteractor> { BluetoothInteractorImpl(get()) }
    factory { BluetoothStateObserver(get()) }
    single { BleHelper(get()) }
//    single { BeaconManagerInitializer(get()).initialize() }
//    single<AppleBeaconScanner> { AppleBeaconScannerImpl(get(), get()) }
    single { BleRawScanner() }
    single<AppleBeaconScanner> {
        AppleBeaconScannerImpl2(
            bleRawScanner = get(),
            parser = AppleAdvertisingPacketParser(),
            calculator = DistanceCalculator()
        )
    }

}