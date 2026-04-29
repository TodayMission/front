package fr.paf.todaysmission.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    suspend fun getUser(userId: String) = withContext(Dispatchers.IO){
        val request = Request.Builder().url("$_baseUrl/me?$userId").get().build()
        _client.newCall(request).execute()
    }
}