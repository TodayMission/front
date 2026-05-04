package fr.paf.todaysmission.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.repository.ChallengesRepository
import fr.paf.todaysmission.repository.GroupChallenge
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

    private val _challenges = MutableStateFlow<List<GroupChallenge>>(emptyList())
    val challenges: StateFlow<List<GroupChallenge>> = _challenges

    fun getGroupChallenges(groupId: String) {
        viewModelScope.launch {
            val result = challengesRepository.getGroupChallenges(groupId)

            result.onSuccess {
                _challenges.value = it
            }.onFailure {
                _message.value = it.message ?: "Erreur lors du chargement des challenges"
            }
        }
    }

    fun createChallenge(name: String, groupId: String) {
        viewModelScope.launch {
            //create challenge with repository
            val result = challengesRepository.createChallenge(name, groupId)

            result.onSuccess {
                _message.value = it
                //reload list of challenge to get new challenge
                getGroupChallenges(groupId)
            }.onFailure {
                _message.value = it.message ?: "Erreur lors de la creation du challenge"
            }
        }
    }

    fun joinChallenge(challengeId: String, groupId: String) {
        viewModelScope.launch {
            val result = challengesRepository.joinChallenge(challengeId)

            result.onSuccess {
                _message.value = "Vous avez rejoint le challenge"
                getGroupChallenges(groupId)
            }.onFailure {
                _message.value = it.message ?: "Erreur lors du join challenge"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
