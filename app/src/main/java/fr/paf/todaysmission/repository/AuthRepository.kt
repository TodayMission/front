package fr.paf.todaysmission.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

data class AuthSession(
    val token: String,
    val userId: String,
)

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    suspend fun login(username: String, password: String): Result<AuthSession> = withContext(Dispatchers.IO){
        // Tolérant : certaines APIs attendent "email", d'autres "user"
        val json = """{ "email": "$username", "user": "$username", "password": "$password" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/auth/login")
            .post(body)
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyStr = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                Log.e("AUTH", "Login failed: ${response.code} $bodyStr")
                return@withContext Result.failure(IllegalStateException("Login failed (${response.code})"))
            }

            val parsed = JSONObject(bodyStr.ifBlank { "{}" })
            val token =
                parsed.optString("token")
                    .ifBlank { parsed.optString("accessToken") }
                    .ifBlank { parsed.optString("access_token") }
            val userId = parsed.optString("userId")
                    .ifBlank { parsed.optString("id") }
                    .ifBlank { parsed.optJSONObject("user")?.optString("id").orEmpty() }

            TokenManager.saveToken(context, token)
            Result.success(AuthSession(token = token, userId = userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // register


}