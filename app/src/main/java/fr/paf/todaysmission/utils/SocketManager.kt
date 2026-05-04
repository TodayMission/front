package fr.paf.todaysmission.utils

import io.socket.client.IO
import io.socket.client.Socket

class SocketManager {

    private lateinit var socket: Socket

    fun connect() {
        val options = IO.Options()

        socket = IO.socket("http://192.168.1.56:3000", options)
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }

        socket.on("message") { args ->
            val data = args[0]
            println("Message reçu: $data")
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