package fr.paf.todaysmission.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.utils.SocketManager
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject

class  SocketRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun listenMessages(onMessage: (String) -> Unit) {

        SocketManager.socket.off("message-group")

        SocketManager.socket.on("message-group") { args ->
            val msg = args[0].toString()

            onMessage(msg)
        }
    }

    fun sendGroupMessage(groupId: String, message: String, onSend: (JSONObject) -> Unit) {
        var data = JSONObject()
        val token = runBlocking { TokenManager.getToken(context) }
        val userId = token?.let { runBlocking { TokenManager.getUserIdFromToken(it) } }.orEmpty()
        val userName = runBlocking { TokenManager.getUserName(context) }.orEmpty()

        data.put("id", userId)
        data.put("nom", userName.ifBlank { "Moi" })
        data.put("groupId", "group-$groupId")
        data.put("message", message)

        onSend(data)

        SocketManager.socket.emit("message-group", data)
    }

    fun joinGroup(groupId : String){
        SocketManager.socket.emit("joinGroup", "group-${groupId}")
    }

}