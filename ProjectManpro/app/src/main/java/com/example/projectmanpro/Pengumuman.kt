package com.example.projectmanpro

import android.os.Parcel
import android.os.Parcelable

data class Pengumuman(
    var image: String?,
    var judul: String?,
    var date: String?,
    var isi: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(judul)
        parcel.writeString(date)
        parcel.writeString(isi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pengumuman> {
        override fun createFromParcel(parcel: Parcel): Pengumuman {
            return Pengumuman(parcel)
        }

        override fun newArray(size: Int): Array<Pengumuman?> {
            return arrayOfNulls(size)
        }
    }
}
