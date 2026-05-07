package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.paf.todaysmission.viewmodels.FriendViewModels
import fr.paf.todaysmission.viewmodels.GroupsViewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteScreen(
    groupId: String,
    navController: NavController,
    groupsViewModels: GroupsViewModels = hiltViewModel(),
    friendsViewModel: FriendViewModels = hiltViewModel()
) {
    val friends by friendsViewModel.friends.collectAsState()

    Scaffold(
        containerColor = Color(0xFFf2f6fe),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Invite", textAlign = TextAlign.Center)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color(0xFF039be5)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text("Mes amis", modifier = Modifier.padding(16.dp))

            LazyColumn {
                itemsIndexed(friends) { _, friend ->
                    Button(
                        onClick = { groupsViewModels.inviteToGroup(friend.id, groupId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(friend.name)
                    }
                }
            }
        }
    }

}
