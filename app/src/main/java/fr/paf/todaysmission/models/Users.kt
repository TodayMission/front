package fr.paf.todaysmission.models

import android.R
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    val id: String,
    val name: String,

) : Parcelable