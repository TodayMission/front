package fr.paf.todaysmission.viewmodels

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.repository.GroupsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject
import fr.paf.todaysmission.utils.State

@HiltViewModel
class GroupsViewModels @Inject constructor(private val groupsRepository: GroupsRepository): ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state = _state
    private val _groups = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groups = _groups

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    init {
        getGroups()
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

    fun createGroup(name: String) {
        viewModelScope.launch {
            groupsRepository.createGroup(name)
            getGroups()
        }
    }


}