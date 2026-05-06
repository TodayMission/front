package fr.paf.todaysmission.views

import android.graphics.fonts.FontStyle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Send
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.patrykandpatrick.vico.core.common.shape.Shape
import fr.paf.todaysmission.components.BottomModalSheet
import fr.paf.todaysmission.components.MessageCard
import fr.paf.todaysmission.models.Challenge
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.models.Messages
import fr.paf.todaysmission.utils.TokenManager
import fr.paf.todaysmission.repository.GroupChallenge
import fr.paf.todaysmission.viewmodels.ChallengesViewModel
import fr.paf.todaysmission.viewmodels.GroupsViewModels
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    id: String,
    navController: NavController,
    challengesViewModel: ChallengesViewModel = hiltViewModel(),
    groupsViewModel: GroupsViewModels = hiltViewModel()
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val joinMessage by challengesViewModel.message.collectAsState()
    val challenges by challengesViewModel.challenges.collectAsState()
    val messages by groupsViewModel.messages.collectAsState()
    var currentUserId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        val token = TokenManager.getToken(context)
        currentUserId = if (token != null) TokenManager.getUserIdFromToken(token) else null

        val messages_merged by groupsViewModel.messages_merged.collectAsState()
        val name by groupsViewModel.name.collectAsState()
    }

    LaunchedEffect(Unit) {
        //get name of group
        groupsViewModel.getGroupName(id)
        //get challenge of groups
        challengesViewModel.getGroupChallenges(id)
        //get messages of groups
        groupsViewModel.getMessages(id)

//        groupsViewModel.mergeData(challenges, messages)
    }

    LaunchedEffect(challenges, messages) {
        groupsViewModel.mergeData(challenges, messages)
    }
    LaunchedEffect(joinMessage) {
        joinMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            challengesViewModel.clearMessage()
        }
    }

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
                        Text(name.capitalize(), textAlign = TextAlign.Center, color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFF039be5)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("invite/${id}") }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFF039be5)
                            )
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)) {
                itemsIndexed(messages_merged) { _, msg ->
                  if(msg.optString("type") == "CHALLENGE") {
                    val challenge = msg.opt("data") as GroupChallenge
                    MessageCard(
                        Messages(
                            id = challenge.id,
                            nom = "SYSTEME",
                            msg = "Challenge ${challenge.name}",
                            group_id = id,
                            send_at = ""
                        ),
                        true
                    )

                    if (challenge.isJoined) {
                         Row() {
                            Button (
                                onClick = { navController.navigate("upload/${challenge.id}/${id}") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                            ) { Text("Envoyer preuve") }
                            Button (
                                onClick = { navController.navigate("group/uploads/${challenge.id}") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xDEDDD5FF)),
                                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                            ) { Text("Voir preuves") }

                        }
                    } else {
                        JoinChallengeButton {
                            challengesViewModel.joinChallenge(challenge.id, id)
                        }
                    }
                }
                itemsIndexed(messages) { _, msg ->
                    val msgUserId = msg.optString("user_id")
                    val isCurrentUser = msgUserId.isNotEmpty() && msgUserId == currentUserId
                    val ms = Messages(
                        id = msg.optString("id", "random"),
                        nom = msg.optString("nom", "SYSTEME"),
                        msg = msg.optString("message"),
                        group_id = msg.optString("groupId").removePrefix("group-"),
                        user_id = msgUserId.ifEmpty { null },
                        send_at = ""
                    )

                    MessageCard(ms, false, isCurrentUser)
                }

            }
        }
        BottomBar(
            showBottomSheet,
            onDismiss = { showBottomSheet = true },
            onSendMessage = { message ->
                groupsViewModel.sendMessage(id,message)
            }
        )
        if (showBottomSheet) {
            BottomModalSheet(showBottomSheet, onDismiss = { showBottomSheet = false }, sheetState, true, { nameValue ->
                showBottomSheet = false
                //create challenge with wiewmodel
                challengesViewModel.createChallenge(nameValue, id)
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
                    imageVector = Icons.Default.Add,
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
                    focusedBorderColor = Color(0xFF4F7EFF),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            IconButton(
                onClick = { sendMessage() },
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF4F7EFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun JoinChallengeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text("Rejoindre le challenge", color = Color.White)
    }
}
