package fr.paf.todaysmission.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.utils.TokenManager
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

class GroupsRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val _token = runBlocking {
        TokenManager.getToken(context) as String
    }

    suspend fun createGroup(name: String): Result<String?> = withContext(Dispatchers.IO){
        val json = """{ "name": "$name" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/groups/create")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        try {
            _client.newCall(request).execute()
            return@withContext Result.success("")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }


    }

    suspend fun inviteToGroup(userId: String, groupId: String): Result<String?> = withContext(Dispatchers.IO){
        val json = """{ "user": "$userId", "groupId" : "$groupId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/groups/send")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        try{
            _client.newCall(request).execute()
            return@withContext Result.success("")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun acceptRequestToGroup(groupId: String) = withContext(Dispatchers.IO){
        val json = """{ "groupId" : "$groupId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/groups/accept")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        try{
            _client.newCall(request).execute()
            return@withContext Result.success("")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun denyRequestToGroup(groupId: String) = withContext(Dispatchers.IO){
        val json = """{ "groupId" : "$groupId" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/groups/deny")
            .addHeader("Authorization", "Bearer $_token")
            .post(body)
            .build()

        try{
            _client.newCall(request).execute()
            return@withContext Result.success("")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<Group>> = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("$_baseUrl/me/groups")
            .addHeader("Authorization", "Bearer $_token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val body = parseGroups(response.body!!.string()) ?: emptyList<Group>();
            Result.success(body)
        } catch(e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getPendingGroupsRequest(): Result<List<Group>> = withContext(Dispatchers.IO){
        Log.d("MINE", "Le token est $_token")
        val request = Request.Builder()
            .url("$_baseUrl/me/groups_request")
            .addHeader("Authorization", "Bearer $_token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val body = parseGroups(response.body!!.string()) ?: emptyList<Group>();
            Result.success(body)
        } catch(e: Exception) {
            Result.failure(e)
        }

    }

    private fun parseGroups(json: String): List<Group> {
        val list = mutableListOf<Group>()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                Group(
                    id = obj.optString("group_id"),
                    name = obj.optString("name", "Unknown")
                )
            )
        }

        return list
    }
}