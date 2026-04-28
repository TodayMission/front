package fr.paf.todaysmission.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.utils.TokenManager
import fr.paf.todaysmission.views.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import javax.inject.Inject

class GroupsRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val _client = OkHttpClient()
    private val _baseUrl = "http://10.57.32.5:3000"
    private val _token = runBlocking {
        TokenManager.getToken(context) as String
    }

    suspend fun getGroups(): List<Group> = withContext(Dispatchers.IO){
        Log.d("MINE", "Le token est $_token")
        val request = Request.Builder()
            .url("$_baseUrl/me/groups")
            .addHeader("Authorization", "Bearer $_token")
            .build()

        val response = _client.newCall(request).execute()

        val body = parseGroups(response.body!!.string()) ?: emptyList<Group>();

        return@withContext body
    }

    private fun parseGroups(json: String): List<Group> {
        val list = mutableListOf<Group>()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                Group(
                    id = obj.optString("group_id"),
                    name = obj.optString("group_id", "Unknown")
                )
            )
        }

        return list
    }
}