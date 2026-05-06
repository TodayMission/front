package fr.paf.todaysmission.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fr.paf.todaysmission.repository._baseUrl
import fr.paf.todaysmission.viewmodels.ProofViewModel

@Composable
fun ProofGroupScreen(challengeId: String, navController: NavController, proofViewModel: ProofViewModel = hiltViewModel()) {

    val proofs by proofViewModel.proofs.collectAsState()

    LaunchedEffect(challengeId) {

        proofViewModel.getProofs(challengeId)

    }

    Scaffold() { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text("PREUVES")
            proofs.forEach { url ->
                AsyncImage(
                    model = _baseUrl + "/image/$url",
                    contentDescription = "preuve",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}