package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.paf.todaysmission.utils.uploadFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun VideoPlayer(uri: Uri) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        }
    )
}

@Composable
fun UploadScreen(challengeId: String?, challengeName: String?, groupId: String, navController: NavController) {
    val context = LocalContext.current

    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    var fileType by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        selectedFile = uri
        fileType = uri?.toString()?.let {
            when {
                it.contains("image") -> "image"
                it.contains("video") -> "video"
                it.contains("document") || it.contains("pdf") -> "pdf"
                else -> "unknown"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Upload Challenge: $challengeName")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                filePicker.launch(
                    arrayOf("image/*", "video/*", "application/pdf")
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors =  ButtonDefaults.buttonColors(containerColor = Color(0xFFf7f7f9))
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = null,
                    tint = Color(0xFF3498db)
                )
                Text("Choisir un fichier", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (fileType == "image") {
            selectedFile?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        if (fileType == "video") {
            selectedFile?.let { uri ->
                VideoPlayer(uri)
            }
        }

        if (fileType == "pdf") {
            Text("PDF sélectionné (pas de preview possible)")
        }

        if (fileType == "unknown") {
            Text("Type de fichier non supporté")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = selectedFile?.toString() ?: "Aucun fichier")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            selectedFile?.let { uri ->
                uploadFile(
                    context = context,
                    uri = uri,
                    challengeId = challengeId ?: "1",
                    userId = "1",
        {             scope.launch(Dispatchers.Main) { navController.navigate("group/$groupId") } },
                )
            }
        }, colors =  ButtonDefaults.buttonColors(containerColor = Color(0xFF3498db)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Uploader")
        }
    }
}