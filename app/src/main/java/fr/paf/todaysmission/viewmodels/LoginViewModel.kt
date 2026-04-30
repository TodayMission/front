package fr.paf.todaysmission.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.repository.AuthRepository
import fr.paf.todaysmission.repository.AuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import fr.paf.todaysmission.utils.State

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state = _state

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    private val _session = MutableStateFlow<AuthSession?>(null)
    val session = _session

//    init {
//        _state.value = State.SUCCESS
//    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = State.LOADING
            _error.value = null
            loginRepository.login(username, password)
                .onSuccess {
                    _session.value = it
                    _state.value = State.SUCCESS
                }
                .onFailure {
                    _state.value = State.ERROR
                    _error.value = it.message?.takeIf { msg -> msg.isNotBlank() } ?: "Erreur lors de la connexion"
                }
        }
    }
}