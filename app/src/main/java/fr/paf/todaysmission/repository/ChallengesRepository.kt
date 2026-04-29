package fr.paf.todaysmission.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class ChallengesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun joinChallenge(challengeId: String): Result<String> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()
        val json = """{ "challengeId": "$challengeId" }"""
        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/challenges/join")
            .addHeader("Authorization", "Bearer $token")
            .post(body)
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            val message = JSONObject(bodyString).optString("message", "Challenge rejoint")
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
