package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.paf.todaysmission.components.BottomModalSheet
import fr.paf.todaysmission.components.GroupCard
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.models.superGroups
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGroupScreen(navController: NavController){

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var groupsValue by remember { mutableStateOf(superGroups) }

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
                        IconButton(onClick = { showBottomSheet = true }) // creation de groupe
                        {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)) {
            itemsIndexed(groupsValue) { index, superGroup ->
                GroupCard(
                    superGroup,
                    navController
                )
            }
        }
        if (showBottomSheet){
            BottomModalSheet(showBottomSheet, onDismiss = { showBottomSheet = false }, sheetState, false, { nameValue ->
                var result = clickHandler("/groups/create", "\"name\": \"$nameValue\"", {})
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
                groupsValue += (
                        Group(
                            id = "4",
                            name = nameValue,
                            avatar = "L",
                            lastMessage = "Nouveau défi sport",
                            lastMessageTime = "2m",
                            unreadCount = 3
                        )
                    )
            })
        }
    }
}