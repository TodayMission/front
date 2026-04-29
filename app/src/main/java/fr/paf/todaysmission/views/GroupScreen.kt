package fr.paf.todaysmission.views

import android.R
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.paf.todaysmission.components.BottomModalSheet
import fr.paf.todaysmission.components.MessageCard
import fr.paf.todaysmission.models.Messages
import fr.paf.todaysmission.models.msg_test
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(id: String, navController: NavController){
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var messages by remember { mutableStateOf(msg_test) }


    Scaffold(
        containerColor = Color(0xFFf2f6fe),
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("My Groups", textAlign = TextAlign.Center)
                    },
                    actions = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFF039be5)
                            )
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.White)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 10.dp )) {
                itemsIndexed(messages) { index, msg ->
                    if (id == msg.group_id){
                        MessageCard(msg)
                        if (msg.nom == "SYSTEME") {
                            JoinChallengeButton(challengeId = msg.id, userId = userId) { message ->
                                Log.d("JoinChallenge", message)
                            }
                        }
                    }
                }
            }
        }
        BottomBar(
            showBottomSheet,
            onDismiss = { showBottomSheet = true },
            onSendMessage = { message ->
                messages = messages + Messages(
                    id = (messages.size + 1).toString(),
                    nom = "Moi",
                    msg = message,
                    group_id = id
                )
            }
        )
        if (showBottomSheet) {
            BottomModalSheet(showBottomSheet, onDismiss = { showBottomSheet = false }, sheetState, true, { nameValue ->
                showBottomSheet = false
                
                //verif if user is connected
                if (userId.isEmpty()) {
                    Log.e("HTTP", "Erreur : userId est vide. Connectez-vous d'abord dans Settings.")
                }

                val route = "challenges/create?name=$nameValue&groupId=$id"
                Log.d("HTTP", "Tentative de création : $route")
                
                clickHandler(route, "", { result ->
                    val challengeName = result.optString("message", nameValue)
                    
                    messages = messages + Messages(
                        id = (messages.size + 1).toString(),
                        nom = "SYSTEME",
                        msg = "Challenge crée $challengeName",
                        group_id = id
                    )
                }, context)
                scope.launch {
                    sheetState.hide()
                }
            })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomBar(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    fun sendMessage() {
        val trimmedMessage = message.trim()
        if (trimmedMessage.isEmpty()) return

        onSendMessage(trimmedMessage)
        message = ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF4F7EFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Défi",
                    tint = Color.White
                )
            }

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Tapez votre message...", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF4F7EFF)
                )
            )

            IconButton(
                onClick = { sendMessage() },
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF4F7EFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

fun joinChallenge(challengeId: String, userId: String, onSuccess: (String) -> Unit) {
    val route = "challenges/join?challengeId=$challengeId&userId=$userId"

    clickHandler(route, "") { response ->
        onSuccess(response.optString("message", "Succès"))
    }
}

@Composable
fun JoinChallengeButton(challengeId: String, userId: String, onJoinSuccess: (String) -> Unit) {
    Button(
        onClick = {
            joinChallenge(challengeId, userId) { message ->
                onJoinSuccess(message)
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text("Rejoindre le challenge", color = Color.White)
    }
}
