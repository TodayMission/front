package fr.paf.todaysmission.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.repository.ChallengesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val challengesRepository: ChallengesRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun joinChallenge(challengeId: String) {
        viewModelScope.launch {
            val result = challengesRepository.joinChallenge(challengeId)

            _message.value = result.getOrElse {
                it.message ?: "Erreur lors du join challenge"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
