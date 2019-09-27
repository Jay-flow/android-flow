package io.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData (
    val social: String,
    val token: String,
    val name: String?,
    val email: String?,
    val gender: String?,
    val age: String?,
    val birthday: String?,
    val profile_thum: String?,
    val profile_image: String?
) : Parcelable