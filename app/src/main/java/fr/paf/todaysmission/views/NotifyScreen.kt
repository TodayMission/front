package fr.paf.todaysmission.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.paf.todaysmission.components.GroupIncomingCard
import fr.paf.todaysmission.viewmodels.GroupsViewModels


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifyScreen(navController: NavController, groupsViewModels: GroupsViewModels = hiltViewModel()) {
    val groups by groupsViewModels.groups_pendings.collectAsState()

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
                        Text("Notifications", textAlign = TextAlign.Center)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("groups") }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to GroupList",
                                tint = Color.Blue
                            )
                        }
                    },

                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp).fillMaxSize()
            ) {
                if (groups.isNotEmpty()) {
                    itemsIndexed(groups) { index, superGroup ->
                        GroupIncomingCard(
                            superGroup,
                            { groupsViewModels.accept(superGroup.id) },
                            { groupsViewModels.deny(superGroup.id) }
                        )
                    }
                }else {
                   item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No notifications at this time.")
                    }
                  }
                }
            }
        }
    }
}