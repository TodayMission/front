package fr.paf.todaysmission.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val id: String,
    val name: String,
    val member_cout: String,
//    val avatar: String,
//    val lastMessage: String,
//    val lastMessageTime: String,
//    val unreadCount: Int
) : Parcelable
