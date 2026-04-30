package fr.paf.todaysmission.models

import android.content.IntentSender
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Challenge (
    val id: String,
    val name: String,
    val status: String,
): Parcelable