package fr.paf.todaysmission.models

import android.content.IntentSender
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Challenge (
    val id: String,
    val name: String,
//    val created_at: Date,
//    val isFinished: Date,
    val group_id: Int
): Parcelable

val superChallenge = listOf<Challenge>(
    Challenge(
        id = "1",
        name = "Gober un kiwi",
        group_id =  2
    ),
    Challenge(
        id = "2",
        name = "Courir 5km tout les jours",
        group_id =  2
    ),
    Challenge(
        id = "3",
        name = "Gober deux kiwi jaunes",
        group_id =  2
    ),
    Challenge(
        id = "4",
        name = "Faire un salto depuis un immeuble",
        group_id =  2
    )

)