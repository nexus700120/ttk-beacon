package ru.ttk.beacon.data.beacon

import io.reactivex.rxjava3.core.Observable
import ru.ttk.beacon.data.BleRawScanner
import ru.ttk.beacon.data.beacon.parser.AppleAdvertisingPacketParser
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.utils.Optional

class AppleBeaconScannerImpl2(
    bleRawScanner: BleRawScanner,
    private val parser: AppleAdvertisingPacketParser,
    private val calculator: DistanceCalculator
) : AppleBeaconScanner {

    private val observable = bleRawScanner.scan()
        .map {
            it.mapNotNull { scanResult ->
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
            }.sortedWith(compareBy({ beacon -> beacon.distance }, { beacon -> beacon.mac }))
        }
        .publish()
        .refCount()

    override fun scan(): Observable<List<AppleBeacon>> = observable

    override fun scan(mac: String): Observable<Optional<AppleBeacon>> =
        observable.map { Optional.toOptional(it.firstOrNull { beacon -> beacon.mac == mac }) }
}
