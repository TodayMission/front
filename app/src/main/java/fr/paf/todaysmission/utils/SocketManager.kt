package fr.paf.todaysmission.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {

    lateinit var socket: Socket

    fun connect() {
        val options = IO.Options()

        socket = IO.socket("http://10.57.32.5:3000", options)
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }

        socket.on("message-group") { args ->
            val data = args[0]
            Log.d("MINE", "$data")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected")
        }
    }

    fun sendMessage(message: String) {
        socket.emit("message", message)
    }

    fun disconnect() {
        socket.disconnect()
    }
}