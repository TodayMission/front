package fr.paf.todaysmission.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.repository.GroupsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import fr.paf.todaysmission.utils.State

@HiltViewModel
class GroupsViewModels @Inject constructor(private val groupsRepository: GroupsRepository): ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state = _state
    private val _groups = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groups = _groups

    private val _groupID = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groupID = _groupID

    private val _groups_pendings = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groups_pendings = _groups_pendings

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    init {
        getGroups()
        getPendingGroups()
    }

    private fun getGroups() {
        viewModelScope.launch {
            state.value = State.LOADING
           val result = groupsRepository.getGroups()

            result.onSuccess {
                _groups.value = it.sortedByDescending{ it.id.toInt() }
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }

    fun getGroupID(groupId: String) {
        viewModelScope.launch {
            val result = groupsRepository.getGroupsID(groupId)

            result.onSuccess {
                _groupID.value = it
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }

    private fun getPendingGroups() {
        viewModelScope.launch {
            state.value = State.LOADING
            val result = groupsRepository.getPendingGroupsRequest()

            result.onSuccess {
                _groups_pendings.value = it.sortedByDescending{ it.id.toInt() }
                state.value = State.SUCCESS
            }.onFailure {
                state.value = State.ERROR
                error.emit("Serveur timeout")
            }
        }
    }

    fun createGroup(name: String) {
        viewModelScope.launch {
            groupsRepository.createGroup(name)
            getGroups()
        }
    }

    fun inviteToGroup(userId: String, groupId: String) {
        viewModelScope.launch {
            groupsRepository.inviteToGroup(userId, groupId)
            getGroups()
        }
    }

    fun accept(groupId: String) {
        viewModelScope.launch {
            groupsRepository.acceptRequestToGroup(groupId)
            getGroups()
            getPendingGroups()
        }
    }

    fun deny(groupId: String) {
        viewModelScope.launch {
            groupsRepository.denyRequestToGroup(groupId)
            getGroups()
            getPendingGroups()
        }
    }


}