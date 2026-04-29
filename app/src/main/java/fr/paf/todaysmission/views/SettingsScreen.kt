package fr.paf.todaysmission.views

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

var token: String = "";
var url: String = "http://192.168.1.14"
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
    val context = LocalContext.current
    Row() {
        Button(onClick = {clickHandler("auth/login", "\"email\": \"paul@gmail.com\","+
                "\"password\": \"paul\"", {}, context)}) {
            Text("Paul")
        }

        Button(onClick = {clickHandler("auth/login", "\"email\": \"test@email.com\","+
                "\"password\": \"password\"", {}, context)}) {
            Text("Test")
        }
    }
}
fun clickHandler(route: String, args: String, test: (json: JSONObject) -> Unit, context: Context)
{
    Log.d("MINE", "pouroud")
    token = runBlocking {
        TokenManager.getToken(context) as String
    }
    Log.d("MINE", "Clicked")
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

//                if (token.isEmpty()) {
                    token = parsedBody.getString("token")

                    CoroutineScope(Dispatchers.IO).launch {
                        TokenManager.saveToken(context, token)
                    }
//                }

                    test(parsedBody)
                } catch (e: Exception) {
                    Log.e("HTTP", "Erreur parsing JSON", e)
                }
            }
        }
    })
}
