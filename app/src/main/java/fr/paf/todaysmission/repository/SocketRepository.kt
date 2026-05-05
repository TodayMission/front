package fr.paf.todaysmission.repository

import fr.paf.todaysmission.utils.SocketManager
import org.json.JSONObject
import javax.inject.Inject

class  SocketRepository @Inject constructor() {

    fun listenMessages(onMessage: (String) -> Unit) {

        SocketManager.socket.off("message-group")

        SocketManager.socket.on("message-group") { args ->
            val msg = args[0].toString()

            onMessage(msg)
        }
    }

    fun sendGroupMessage(groupId: String, message: String, onSend: (JSONObject) -> Unit) {
        var data = JSONObject()

        data.put("groupId", "group-$groupId")
        data.put("message", message)

        onSend(data)

        SocketManager.socket.emit("message-group", data)
    }

    fun joinGroup(groupId : String){
        SocketManager.socket.emit("joinGroup", "group-${groupId}")
    }

}