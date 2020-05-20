package ru.ttk.beacon.data

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.altbeacon.beacon.*
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.utils.Optional
import timber.log.Timber
import java.util.*

class AppleBeaconScannerImpl(
    private val appContext: Context,
    private val beaconManager: BeaconManager
) : AppleBeaconScanner {

    private val handler = Handler()
    private val unbindRunnable = Runnable {
        if (beaconManager.isBound(consumer)) {
            Timber.d("unbind")
            beaconManager.unbind(consumer)
            beaconManager.removeRangeNotifier(notifier)
            beaconManager.stopRangingBeaconsInRegion(region)
            beaconsSubject.onNext(emptyList())
        }
    }

    private val beaconsSubject = BehaviorSubject.createDefault(emptyList<Beacon>())

    private val region = Region(UUID.randomUUID().toString(), null, null, null)
    private val notifier = RangeNotifier { beacons, _ ->
        Timber.d("notifier: ${beacons.size}")
        beaconsSubject.onNext(beacons.toList())
    }

    private val consumer = object : BeaconConsumer {

        override fun getApplicationContext(): Context = appContext

        override fun unbindService(connection: ServiceConnection) {
            Timber.d("Unbind service: $connection")
            appContext.unbindService(connection)
        }

        override fun bindService(intent: Intent, connection: ServiceConnection, mode: Int): Boolean {
            Timber.d("Bind service: intent = $intent, connection = $connection, mode = $mode")
            return appContext.bindService(intent, connection, mode)
        }

        override fun onBeaconServiceConnect() {
            Timber.d("Consumer connected to service")
            beaconManager.addRangeNotifier(notifier)
            beaconManager.startRangingBeaconsInRegion(region)
        }
    }

    override fun scan(): Observable<List<AppleBeacon>> = beaconsSubject.map {
        it.sortedBy { item -> item.distance }
            .map { item ->
                AppleBeacon(
                    uuid = item.id1.toUuid().toString(),
                    mac = item.bluetoothAddress,
                    major = item.id2.toInt(),
                    minor = item.id3.toInt(),
                    rssi = item.rssi,
                    distance = item.distance
                )
            }
    }.doOnSubscribe {
        handler.removeCallbacksAndMessages(null)
        if (!beaconManager.isBound(consumer)) {
            Timber.d("bind")
            beaconManager.bind(consumer)
        }
    }.doFinally {
        if (!beaconsSubject.hasObservers()) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(unbindRunnable, UNBIND_DELAY)
        }
    }

    override fun scan(uuid: String): Observable<Optional<AppleBeacon>> =
        scan().map { Optional.toOptional(it.firstOrNull { beacon -> beacon.uuid == uuid }) }

    companion object {
        private const val UNBIND_DELAY = 3000L
    }
}

