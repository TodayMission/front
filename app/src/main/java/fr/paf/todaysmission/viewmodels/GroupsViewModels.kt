package fr.paf.todaysmission.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.repository.GroupsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModels @Inject constructor(private val groupsRepository: GroupsRepository): ViewModel() {

    private val _groups = mutableStateOf<List<Group>>(emptyList<Group>())
    val groups: State<List<Group>> = _groups

    init {
        getGroups()
    }

    private fun getGroups() {
        viewModelScope.launch {
            _groups.value = groupsRepository.getGroups()
        }
    }

    fun createGroup(name: String) {
        viewModelScope.launch {
            groupsRepository.createGroup(name)
            getGroups() // 🔥 refresh after creation
        }
    }


}