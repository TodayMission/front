package fr.paf.todaysmission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.paf.todaysmission.components.FriendCard
import fr.paf.todaysmission.components.FriendIncomingCard
import fr.paf.todaysmission.components.FriendPendingCard
import fr.paf.todaysmission.utils.State
import fr.paf.todaysmission.viewmodels.FriendViewModels

@Composable
fun FriendScreen(navController: NavController, friendsViewModel: FriendViewModels = hiltViewModel()) {
    var context = LocalContext.current
    val state by friendsViewModel.state.collectAsState()
    val friends by friendsViewModel.friends.collectAsState()
    val friends_pending by friendsViewModel.friends_pending.collectAsState()
    val friends_incoming by friendsViewModel.friends_incoming.collectAsState()
    val error by friendsViewModel.error.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var friend_id by remember { mutableStateOf("") }

//    LaunchedEffect(error) {
//        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
//    }

    Scaffold() { innerPadding ->

        Column(
            Modifier.padding(innerPadding)
            .fillMaxSize()
        ) {
            OutlinedTextField(
                value = friend_id,
                singleLine = true,
                onValueChange = { friend_id = it},
                placeholder = { Text("12345", color = androidx.compose.ui.graphics.Color.Gray) },
                modifier = Modifier.height(75.dp).padding(0.dp, 10.dp).fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.LightGray
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // 👉 Your action here
                        keyboardController?.hide()
                        friendsViewModel.sendFriendRequest(friend_id)
                        friend_id = ""
                    }
                )
            );
                when (state) {
                State.LOADING -> {
                    CircularProgressIndicator()
                }
                State.SUCCESS -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        itemsIndexed(friends) { index, friend ->
                            FriendCard(
                                friend,
                                { uid -> friendsViewModel.deleteFriend(uid) }
                            )
                        }
                    }
                }
                State.ERROR -> { Text("Error with the server") }
            }
            HorizontalDivider()
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    itemsIndexed(friends_pending) { index, friend_pending ->
                        FriendPendingCard (
                            friend_pending,
                            { uid -> friendsViewModel.deleteFriend(uid) }
                        )
                    }
            }
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                itemsIndexed(friends_incoming) { index, friend_incoming ->
                    FriendIncomingCard (
                        friend_incoming,
                        { uid -> friendsViewModel.acceptFriendRequest(uid) },
                        { uid -> friendsViewModel.denyFriendRequest(uid) }
                    )
                }
            }
        }
    }
}