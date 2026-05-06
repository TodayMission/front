package fr.paf.todaysmission.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Challenge
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

data class GroupChallenge(
    val id: String,
    val name: String,
    val isJoined: Boolean,
    val created_at: String
)

class ChallengesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun getProofsChallenge(challengeId: String): Result<List<String?>> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()

        val request = Request.Builder()
            .url("$_baseUrl/upload?id=$challengeId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            return@withContext Result.success(parseProofs(bodyString))
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }
    suspend fun getGroupChallenges(groupId: String): Result<List<GroupChallenge>> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()

        val request = Request.Builder()
            .url("$_baseUrl/challenges?groupId=$groupId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            val array = JSONArray(bodyString)
            val challenges = mutableListOf<GroupChallenge>()

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                challenges.add(GroupChallenge(
                    id = item.optString("id"),
                    name = item.optString("name"),
                    isJoined = item.optBoolean("isJoined"),
                    created_at = item.optString("created_at")
                ))
            }

            Result.success(challenges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createChallenge(name: String, groupId: String): Result<String> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()
        //get name and groupid from view
        val json = """{ "name": "$name", "groupId": "$groupId" }"""
        val body = json.toRequestBody("application/json".toMediaType())
        //post to  back with token
        val request = Request.Builder()
            .url("$_baseUrl/challenges/create")
            .addHeader("Authorization", "Bearer $token")
            .post(body)
            .build()

        try {
            //execute request
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()
            //stop and return error to wiewmodel
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            Result.success("Challenge cree")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinChallenge(challengeId: String): Result<Unit> = withContext(Dispatchers.IO) {
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

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChallengesUser(userId: String): Result<List<Challenge>> = withContext(Dispatchers.IO) {
        val token = TokenManager.getToken(context).orEmpty()
        val request = Request.Builder()
            .url("$_baseUrl/challenges/user/$userId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        try {
            val response = _client.newCall(request).execute()
            val bodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception(bodyString))
            }

            Result.success(parseChallenges(bodyString))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseProofs(json: String): List<String> {
        val list = mutableListOf<String>()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                obj.optString("path").replace("uploads/", "")
            )
        }

        return list
    }

    private fun parseChallenges(json: String): List<Challenge> {
        val list = mutableListOf<Challenge>()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                Challenge(
                    id = obj.optString("id"),
                    name = obj.optString("name"),
                    status = obj.optBoolean("is_completed"),
                    member_count = obj.optString("member_count"),
                    date_end = obj.optString("end_at"),
                    group_id = obj.optString("group_id")
                )
            )
        }

        return list
    }
}
