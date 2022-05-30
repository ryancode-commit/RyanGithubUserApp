package com.ra.consumerapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
        var name: String? = null,
        var photo: String? = null,
        var username:String? = null,
        var following: String? = null,
        var followers:String? = null,
        var repository: String? = null,
        var company: String? = null,
        var location: String? = null
) : Parcelable