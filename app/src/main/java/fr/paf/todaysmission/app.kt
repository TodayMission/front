package fr.paf.todaysmission

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.paf.todaysmission.utils.SocketManager

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SocketManager.connect()
    }
}