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
)

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    suspend fun login(username: String, password: String): Result<AuthSession?> = withContext(Dispatchers.IO){
        // Tolérant : certaines APIs attendent "email", d'autres "user"
        val json = """{ "email": "$username", "user": "$username", "password": "$password" }"""

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$_baseUrl/auth/login")
            .post(body)
            .build()

        Log.d("MINE", request.url.toString())

        try {
            val response = _client.newCall(request).execute()
            val bodyStr = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                Log.e("AUTH", "Login failed: ${response.code} $bodyStr")
                return@withContext Result.failure(IllegalStateException("Login failed (${response.code})"))
            }

            if(response.code == 400) {
                return@withContext Result.success(null)
            }

            val parsed = JSONObject(bodyStr.ifBlank { "{}" })
            val token =
                parsed.optString("token")
                    .ifBlank { parsed.optString("accessToken") }
                    .ifBlank { parsed.optString("access_token") }
            val userName = parsed
                .optJSONArray("response")
                ?.optJSONArray(0)
                ?.optJSONObject(0)
                ?.optString("name")
                .orEmpty()


            TokenManager.saveToken(context, token)
            if (userName.isNotBlank()) {
                TokenManager.saveUserName(context, userName)
            }
            Result.success(AuthSession(token = token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // register
}