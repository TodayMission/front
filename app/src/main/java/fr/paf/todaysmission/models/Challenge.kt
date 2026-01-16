package fr.paf.todaysmission.models

import android.content.IntentSender
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Challenge (
    val id: String,
    val name: String,
    val description: String,
    val status: String,
//    val created_at: Date,
//    val isFinished: Date,
    val group_id: Int
): Parcelable

val superChallenge = listOf<Challenge>(
    Challenge(
        id = "1",
        name = "Gober un kiwi",
        description = "Gober le kiwi entier dès le matin",
        status = "En Cours",
        group_id =  2
    ),
    Challenge(
        id = "2",
        name = "Courir 5km tout les jours",
        description = "Perds du poids stp",
        status = "En Cours",
        group_id =  2
    ),
    Challenge(
        id = "3",
        name = "Gober deux kiwi jaunes",
        description = "Gobe des kiwi stp",
        status = "Terminé",
        group_id =  2
    ),
    Challenge(
        id = "4",
        name = "Faire un salto depuis un immeuble",
        description = "Meurt stp",
        status = "Terminé",
        group_id =  2
    )

)