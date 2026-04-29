package fr.paf.todaysmission.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.paf.todaysmission.components.BottomModalSheet
import fr.paf.todaysmission.components.GroupCard
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.viewmodels.GroupsViewModels
import fr.paf.todaysmission.utils.State
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGroupScreen(navController: NavController, groupsViewModel: GroupsViewModels = hiltViewModel()){
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val groups by groupsViewModel.groups.collectAsState()
    val error by groupsViewModel.error.collectAsState()
    val state by groupsViewModel.state.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
                        Text("My Groups", textAlign = TextAlign.Center)
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("notif") }) // creation de groupe
                        {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
                        IconButton(onClick = { navController.navigate("friends") }) // creation de groupe
                        {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                State.LOADING -> CircularProgressIndicator()
                State.SUCCESS -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        itemsIndexed(groups) { index, superGroup ->
                            GroupCard(
                                superGroup,
                                navController
                            )
                        }
                    }
                }

                State.ERROR -> Text("Error")
            }
        }
        if (showBottomSheet){
            BottomModalSheet(showBottomSheet, onDismiss = { showBottomSheet = false }, sheetState, false, { nameValue ->
                groupsViewModel.createGroup(nameValue)
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }

            })
        }
    }
}