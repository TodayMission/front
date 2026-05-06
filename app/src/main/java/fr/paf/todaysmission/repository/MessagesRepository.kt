package fr.paf.todaysmission.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Messages
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getGroupMessages(groupId: String): Result<List<Messages>> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()

        val request = Request.Builder()
            .url("$_baseUrl/groups/$groupId/messages")
            .addHeader("Authorization", "Bearer $token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            Result.success(parseMessages(bodyString))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(groupId: String, message: String): Result<Unit> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()
        val json = JSONObject().put("message", message).toString()
        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/groups/$groupId/messages")
            .addHeader("Authorization", "Bearer $token")
            .post(body)
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseMessages(json: String): List<Messages> {
        val array = JSONArray(json)
        val messages = mutableListOf<Messages>()

        for (i in 0 until array.length()) {
            messages.add(parseMessage(array.getJSONObject(i)))
        }

        return messages
    }

    private fun parseMessage(obj: JSONObject): Messages {
        return Messages(
            id = obj.optString("id"),
            nom = obj.optString("author_name", "Moi"),
            msg = obj.optString("message"),
            group_id = obj.optString("group_id"),
            user_id = obj.optString("user_id"),
            send_at = obj.optString("send_at")
        )
    }
}
