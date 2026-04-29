package fr.paf.todaysmission.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.paf.todaysmission.components.ChallengeCard
import fr.paf.todaysmission.models.superChallenge


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController){
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
                        Text("Home", textAlign = TextAlign.Center)
                    }
                )
            }
        },
    ) { innerPadding ->
        // a changer plus tard blud
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(8.dp)) {
            val challengesPendings = superChallenge.filter { it.status == "En Cours" }
            val challengesFinish = superChallenge.filter { it.status != "En Cours" }

            if (challengesPendings.isNotEmpty()) {
                item {
                    Text(
                        "Défis en Cours",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp, 8.dp)
                    )
                }
                itemsIndexed(challengesPendings) { index, challenge ->
                    ChallengeCard(challenge, {navController.navigate("upload/${challenge.id}")})
                }
            }

            if (challengesFinish.isNotEmpty()) {
                item {
                    Text(
                        "Défis Terminés",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp, 8.dp)
                    )
                }
                itemsIndexed(challengesFinish) { index, challenge ->
                    ChallengeCard(challenge, {navController.navigate("upload/${challenge.id}")})
                }
            }
        }
    }
}