package fr.paf.todaysmission.viewModel

import androidx.lifecycle.ViewModel
import fr.paf.todaysmission.models.Challenge
import fr.paf.todaysmission.models.superChallenge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val pendingChallenges: List<Challenge> = superChallenge,
    val finishedChallenges: List<Challenge> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class HomeViewModel() : ViewModel() {
    private val pending = superChallenge.filter { it.status == "En Cours" }
    private val finished = superChallenge.filter { it.status != "En Cours" }

    val uiState = MutableStateFlow(
        HomeUiState(
            pendingChallenges = pending,
            finishedChallenges = finished,
        )
    )
}