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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.paf.todaysmission.components.GroupCard
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.models.superGroups

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGroupScreen(){
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
                        IconButton(onClick = { clickHandler("groups/create", "\"name\": \"MonGroupe\"")  }) // creation de groupe
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
        // a changer plus tard blud
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)) {
            itemsIndexed(superGroups) { index, superGroup ->
                GroupCard(superGroups[index])
            }
        }
    }
}