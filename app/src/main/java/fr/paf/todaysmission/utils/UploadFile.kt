package fr.paf.todaysmission.utils

import android.content.Context
import android.net.Uri
import fr.paf.todaysmission.repository._baseUrl
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import java.io.InputStream

fun uploadFile(
    context: Context,
    uri: Uri,
    challengeId: String,
    userId: String
) {
    val client = OkHttpClient()

    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

    val requestBody = object : RequestBody() {
        override fun contentType() = "application/octet-stream".toMediaTypeOrNull()

        override fun writeTo(sink: BufferedSink) {
            inputStream?.source()?.use { source ->
                sink.writeAll(source)
            }
        }
    }

    val mimeType = context.contentResolver.getType(uri)

    val extension = when (mimeType) {
        "image/jpeg" -> ".jpg"
        "image/png" -> ".png"
        "image/webp" -> ".webp"
        "video/mp4" -> ".mp4"
        "application/pdf" -> ".pdf"
        else -> ""
    }

    val fileName = ((uri.lastPathSegment ?: "file")
        .replace(Regex("[^a-zA-Z0-9._-]"), "_")) + extension

    val multipartBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
            fileName,
            requestBody
        )
        .build()

    val _token = runBlocking {
        TokenManager.getToken(context) as String
    }


    val request = Request.Builder()
        .url("$_baseUrl/challenges/$challengeId/upload")
        .addHeader("Authorization", "Bearer $_token")
        .post(multipartBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: java.io.IOException) {
            println("Upload failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            println("Upload success: ${response.body?.string()}")
        }
    })
}