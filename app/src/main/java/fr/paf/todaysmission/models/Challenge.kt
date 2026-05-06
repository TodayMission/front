package fr.paf.todaysmission.models

import android.content.IntentSender
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date
import java.util.TimeZone

@Parcelize
data class Challenge (
    val id: String,
    val name: String,
    val status: Boolean,
    val member_count: String,
    val date_end: String,
    val group_id: String
): Parcelable