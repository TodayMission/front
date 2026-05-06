package fr.paf.todaysmission.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.models.Users
import fr.paf.todaysmission.repository.FriendRepository
import fr.paf.todaysmission.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModels @Inject constructor(private val friendsRepository: FriendRepository): ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state = _state

    private val _friends = MutableStateFlow<List<Users>>(emptyList())
    val friends = _friends

    private val _friends_pending = MutableStateFlow<List<Users>>(emptyList())
    val friends_pending = _friends_pending

    private val _friends_incoming = MutableStateFlow<List<Users>>(emptyList())

    val friends_incoming = _friends_incoming

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    init {
        getAnyFriends()
    }

    fun getAnyFriends() {
        getFriends()
        getPendingFriends()
        getIncomingFriends()
    }

    fun getIncomingFriends() {
        viewModelScope.launch {
//            state.value = State.LOADING

            val results = friendsRepository.getIncomingFriends()

            results.onSuccess {
                _friends_incoming.value = it
//                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }

    fun getFriends() {
        viewModelScope.launch {
            state.value = State.LOADING

            val results = friendsRepository.getFriends()

            results.onSuccess {
                _friends.value = it
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }

    fun getPendingFriends() {
        viewModelScope.launch {
//            state.value = State.LOADING
            val results = friendsRepository.getPendingFriends()

            results.onSuccess {
                Log.d("MINE", it.toString())
                _friends_pending.value = it
//                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }


    fun deleteFriend(userId: String) {
        viewModelScope.launch {
            state.value = State.LOADING

            friendsRepository.deleteFriend(userId)

            getAnyFriends()
            getFriends()
        }
    }

    fun sendFriendRequest(userId: String) {
        viewModelScope.launch {
            state.value = State.LOADING

            val results = friendsRepository.sendFriendRequest(userId)

            results.onSuccess {
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Can't find the following user")
            }

            getAnyFriends()
        }
    }

    fun acceptFriendRequest(userId: String){
        viewModelScope.launch {
            state.value = State.LOADING

            val results = friendsRepository.acceptFriendRequest(userId)

            results.onSuccess {
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Can't accept the request right now")
            }

            getAnyFriends()
        }
    }

    fun denyFriendRequest(userId: String) {
        viewModelScope.launch {
            state.value = State.LOADING

            val results = friendsRepository.denyFriendRequest(userId)

            results.onSuccess {
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Can't deny the request right now")
            }

            getAnyFriends()
        }
    }
}
