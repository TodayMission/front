package fr.paf.todaysmission.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.repository.ChallengesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProofViewModel @Inject constructor(
    private val proofRepository: ChallengesRepository
) : ViewModel() {

    val _proofs = MutableStateFlow<List<String?>>(emptyList())
    val proofs = _proofs

    fun getProofs(challengesId: String) {
        viewModelScope.launch {
            val result = proofRepository.getProofsChallenge(challengesId)

            result.onSuccess {
                _proofs.value = it
            }.onFailure {
//                _message.value = it.message ?: "Erreur lors du chargement des challenges"
            }
        }
    }
}

