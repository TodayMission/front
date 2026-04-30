package fr.paf.todaysmission.utils

import android.content.Context
import android.net.Uri
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

    val multipartBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "upload", requestBody)
        .build()

    val request = Request.Builder()
        .url("http://10.0.2.2:3000/challenges/$challengeId/upload?userId=$userId")
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