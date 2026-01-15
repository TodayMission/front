package fr.paf.todaysmission.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val id: String,
    val name: String,
    val avatar: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int
) : Parcelable



var superGroups = listOf(
    Group(
        id = "1",
        name = "Les Kiwis",
        avatar = "L",
        lastMessage = "Nouveau défi sport",
        lastMessageTime = "2m",
        unreadCount = 3
    ),
    Group(
        id = "2",
        name = "Les Bidulos",
        avatar = "O",
        lastMessage = "Quand on fait un nouveau défis les copaings",
        lastMessageTime = "15m",
        unreadCount = 0
    ),
    Group(
        id = "3",
        name = "Stepfanne",
        avatar = "L",
        lastMessage = "Nouveau défi sport",
        lastMessageTime = "1h",
        unreadCount = 5
    ),
)
