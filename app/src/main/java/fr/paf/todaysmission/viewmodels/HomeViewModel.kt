package fr.paf.todaysmission.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.paf.todaysmission.models.Challenge
import fr.paf.todaysmission.repository.ChallengesRepository
import fr.paf.todaysmission.utils.State
import fr.paf.todaysmission.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val challengesRepository: ChallengesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _challenge = MutableStateFlow<List<Challenge>>(emptyList())
    val challenge: StateFlow<List<Challenge>> = _challenge.asStateFlow()


    fun loadChallenges() {
        viewModelScope.launch {
            val userId = TokenManager.getUserId(appContext).orEmpty()
            if (userId.isBlank()) {
                _state.value = State.ERROR
                return@launch
            }
            _state.value = State.LOADING
            challengesRepository.getChallengesUser(userId)
                .onSuccess {
                    _challenge.value = it.sortedByDescending { c -> c.id }
                    _state.value = State.SUCCESS
                }
                .onFailure {
                    _state.value = State.ERROR
                }
        }
    }
}
