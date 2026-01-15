package fr.paf.todaysmission.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Messages(
    val id: String,
    val nom: String,
    val msg: String,
    val group_id: String
): Parcelable

val msg_test = listOf(
    Messages(
        id = "1",
        nom = "Clément",
        msg = "J'aime les oeufs",
        group_id = "1"
    ),
    Messages(
        id = "2",
        nom = "Aurel",
        msg = "Un ptit défi",
        group_id = "1"
    ),
    Messages(
        id = "3",
        nom = "mot",
        msg = "Yokoso soul society",
        group_id = "2"
    ),
    Messages(
        id = "4",
        nom = "Starfulou",
        msg = "67",
        group_id = "2"
    ),
    Messages(
        id = "5",
        nom = "Starfulou",
        msg = "69",
        group_id = "3"
    )
)