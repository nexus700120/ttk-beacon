package ru.ttk.beacon.data.beacon

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.data.ble.BleRawScanner
import ru.ttk.beacon.data.beacon.parser.AppleAdvertisingPacketParser
import ru.ttk.beacon.data.beacon.parser.ByteUtils.toHexStringOrNull
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.utils.Optional
import timber.log.Timber

class AppleBeaconScannerImpl(
    bleRawScanner: BleRawScanner,
    private val parser: AppleAdvertisingPacketParser,
    private val calculator: DistanceCalculator
) : AppleBeaconScanner {

    private val observable = bleRawScanner.scan()
        .map {
            it.mapNotNull { scanResult ->
                Timber.d("packet: ${scanResult.scanRecord?.bytes?.toHexStringOrNull(26)}")
                parser.parse(scanResult.scanRecord?.bytes)?.let { packet ->
                    AppleBeacon(
                        uuid = packet.uuid,
                        mac = scanResult.device.address,
                        major = packet.major,
                        minor = packet.minor,
                        rssi = scanResult.rssi,
                        distance = calculator.calculateDistance(packet.txPower, scanResult.rssi)
                    )
                }
            }
        }
        .publish()
        .refCount()

    override fun scan(): Observable<List<AppleBeacon>> = observable

    override fun scan(mac: String): Observable<Optional<AppleBeacon>> =
        observable.map { Optional.toOptional(it.firstOrNull { beacon -> beacon.mac == mac }) }
}
