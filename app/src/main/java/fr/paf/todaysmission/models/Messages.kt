package fr.paf.todaysmission.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Messages(
    val id: String,
    val nom: String,
    val msg: String,
    val group_id: String,
    val user_id: String? = null,
    val send_at: String?
): Parcelable
