package fr.paf.todaysmission.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

var token: String = ""
var userId: String = ""
var url: String = "http://10.0.2.2"
var port: String = "3000"

@Composable
fun SettingsScreen(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Coming Soon", style = MaterialTheme.typography.headlineMedium)
        APITestButton()
    }
}

@Composable
fun APITestButton(){
    Button(onClick = {clickHandler("auth/login", "\"email\": \"caca@gmail.com\","+
            "\"password\": \"caca\"", {})}) {
        Text("Test API")
    }
}

fun clickHandler(route: String, args: String, test: (json: JSONObject) -> Unit){
    Log.d("MINE", "Clicked $route")
    val JSON: MediaType = "application/json".toMediaType()
    val client: OkHttpClient = OkHttpClient()

    val json: String = if (args.isEmpty()) "{}" else "{$args}"
    val body: RequestBody = RequestBody.create(JSON, json)

    val request: Request = Request.Builder()
        .url("$url:$port/$route")
        .post(body)
        .addHeader("Authorization", "Bearer $token")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Erreur réseau", e)
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                val bodyStr = it.body?.string()
                Log.d("HTTP", "Code: ${it.code}, Réponse brute : $bodyStr")

                if (!it.isSuccessful) return

                try {
                    val parsedBody = JSONObject(bodyStr ?: "{}")

                    // Récupération du TOKEN
                    if (parsedBody.has("token")) {
                        token = parsedBody.getString("token")
                    }

                    // Récupération du USER ID (plusieurs cas possibles)
                    if (parsedBody.has("userId")) {
                        userId = parsedBody.getString("userId")
                    } else if (parsedBody.has("id")) {
                        userId = parsedBody.getString("id")
                    } else if (parsedBody.has("user")) {
                        val userObj = parsedBody.getJSONObject("user")
                        if (userObj.has("id")) userId = userObj.getString("id")
                    }
                    
                    Log.d("HTTP", "Token actuel: $token")
                    Log.d("HTTP", "UserId actuel: $userId")

                    test(parsedBody)
                } catch (e: Exception) {
                    Log.e("HTTP", "Erreur parsing JSON", e)
                }
            }
        }
    })
}