package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.paf.todaysmission.viewmodels.GroupsViewModels

@Composable
fun InviteScreen(groupId: String, groupsViewModels: GroupsViewModels = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var user by remember { mutableStateOf("") }

    Scaffold() { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                placeholder = { "User ID" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // 👉 Your action here
                        keyboardController?.hide()
                        groupsViewModels.inviteToGroup(user, groupId)
                        user = ""
                    }
                )
            )
        }
    }

}