package io.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData (
    var social: String,
    var token: String?,
    var name: String?,
    var email: String?,
    var gender: String?,
    var age: String?,
    var birthday: String?,
    var profile_thum: String?,
    var profile_image: String?
) : Parcelable