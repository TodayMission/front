package fr.paf.todaysmission.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Users
import fr.paf.todaysmission.utils.TokenManager
import fr.paf.todaysmission.views.clickHandler
import fr.paf.todaysmission.views.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.net.SocketTimeoutException
import javax.inject.Inject

class FriendRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val _token = runBlocking {
        TokenManager.getToken(context) as String
    }

    suspend fun sendFriendRequest(userId: String) = withContext(Dispatchers.IO){
        val json = """{ "user": "$userId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/friends/send")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        _client.newCall(request).execute()
    }

    suspend fun acceptFriendRequest(userId: String) = withContext(Dispatchers.IO){
        val json = """{ "user": "$userId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/friends/accept")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        _client.newCall(request).execute()
    }

    suspend fun denyFriendRequest(userId: String) = withContext(Dispatchers.IO){
        val json = """{ "user": "$userId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/friends/deny")
            .addHeader("Authorization", "Bearer $_token")
            .delete(body)
            .build()

        _client.newCall(request).execute()
    }

    suspend fun deleteFriend(userId: String) = withContext(Dispatchers.IO){
        val json = """{ "user": "$userId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/friends/delete")
            .addHeader("Authorization", "Bearer $_token")
            .delete(body)
            .build()

        _client.newCall(request).execute()
    }

    suspend fun getIncomingFriends(): Result<List<Users>> = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("$_baseUrl/me/incoming_friend")
            .addHeader("Authorization", "Bearer $_token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val body = parseFriends(response.body!!.string()) ?: emptyList<Users>();
            Result.success(body)
        } catch(e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getFriends(): Result<List<Users>> = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("$_baseUrl/me/friends")
            .addHeader("Authorization", "Bearer $_token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val body = parseFriends(response.body!!.string()) ?: emptyList<Users>();
            Result.success(body)
        } catch(e: Exception) {
            Result.failure(e)
        }

    }

    private fun parseFriends(json: String): List<Users> {
        val list = mutableListOf<Users>()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                Users(
                    id = obj.optString("id"),
                    name = obj.optString("name", "Unknown")
                )
            )
        }

        return list
    }
}