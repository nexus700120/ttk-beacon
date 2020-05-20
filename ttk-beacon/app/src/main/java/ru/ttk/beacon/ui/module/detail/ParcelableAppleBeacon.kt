package ru.ttk.beacon.ui.module.detail

import android.os.Parcel
import android.os.Parcelable
import ru.ttk.beacon.domain.entity.AppleBeacon

class ParcelableAppleBeacon : Parcelable {

    private var _beacon: AppleBeacon? = null
    val beacon: AppleBeacon
        get() = requireNotNull(_beacon)

    constructor(beacon: AppleBeacon) {
        _beacon = beacon
    }

    constructor(parcel: Parcel) {
        _beacon = AppleBeacon(
            uuid = requireNotNull(parcel.readString()),
            mac = requireNotNull(parcel.readString()),
            major = parcel.readInt(),
            minor = parcel.readInt(),
            rssi = parcel.readInt(),
            distance = parcel.readDouble()
        )
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(beacon.uuid)
        dest.writeString(beacon.mac)
        dest.writeInt(beacon.major)
        dest.writeInt(beacon.minor)
        dest.writeInt(beacon.rssi)
        dest.writeDouble(beacon.distance)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ParcelableAppleBeacon> {

        override fun createFromParcel(parcel: Parcel): ParcelableAppleBeacon {
            return ParcelableAppleBeacon(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableAppleBeacon?> {
            return arrayOfNulls(size)
        }
    }
}