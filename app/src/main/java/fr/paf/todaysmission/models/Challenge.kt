package fr.paf.todaysmission.models

import android.content.IntentSender
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Challenge (
    val id: String,
    val name: String,
//    val description: String,
    val status: String,
//    val created_at: Date,
//    val isFinished: Date,
//    val group_id: Int
): Parcelable