package com.example.projectmanpro

import android.os.Parcel
import android.os.Parcelable

data class Grup(
    var gambar: String?,
    var nama: String?,
    var kategori: String?,
    var createdBy: String?,
    var original: String?

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gambar)
        parcel.writeString(nama)
        parcel.writeString(kategori)
        parcel.writeString(createdBy)
        parcel.writeString(original)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grup> {
        override fun createFromParcel(parcel: Parcel): Grup {
            return Grup(parcel)
        }

        override fun newArray(size: Int): Array<Grup?> {
            return arrayOfNulls(size)
        }
    }
}
