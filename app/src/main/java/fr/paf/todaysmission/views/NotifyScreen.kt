package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import fr.paf.todaysmission.components.GroupCard
import fr.paf.todaysmission.components.GroupIncomingCard
import fr.paf.todaysmission.viewmodels.GroupsViewModels

@Preview
@Composable
fun NotifycationScreenPreview() {
    NotifyScreen()
}

@Composable
fun NotifyScreen(groupsViewModels: GroupsViewModels = hiltViewModel()) {
    val groups by groupsViewModels.groups_pendings.collectAsState()

    Scaffold() { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                itemsIndexed(groups) { index, superGroup ->
                    GroupIncomingCard(
                        superGroup,
                        { groupsViewModels.accept(superGroup.id) },
                        { groupsViewModels.deny(superGroup.id) }
                    )
                }
            }
        }
    }
}