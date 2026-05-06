package fr.paf.todaysmission.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.repository.GroupChallenge
import fr.paf.todaysmission.repository.GroupsRepository
import fr.paf.todaysmission.repository.MessagesRepository
import fr.paf.todaysmission.repository.SocketRepository
import fr.paf.todaysmission.utils.SocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import fr.paf.todaysmission.utils.State
import org.json.JSONObject

@HiltViewModel
class GroupsViewModels @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val socketRepository: SocketRepository,
    private val messagesRepository: MessagesRepository
): ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state = _state
    private val _groups = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groups = _groups

    private val _groups_pendings = MutableStateFlow<List<Group>>(emptyList<Group>())
    val groups_pendings = _groups_pendings

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    private val _messages = MutableStateFlow<List<JSONObject>>(emptyList())
    val messages = _messages

    private val _messages_merged = MutableStateFlow<List<JSONObject>>(emptyList())
    val messages_merged = _messages_merged

    private val _name = MutableStateFlow<String>("Unknown")
    val name = _name
    private var currentGroupId: String? = null


    init {
        getGroups()
        getPendingGroups()
        startListening()
    }

    fun mergeData(
        challenges: List<GroupChallenge>,
        messages: List<JSONObject>
    ) {
        Log.d("MINE", "passed HERE MERGE")

        val challengeJson = challenges.map { challenge ->
            JSONObject().apply {
                put("type", "CHALLENGE")
                put("id", challenge.id)
                put("nom", "CHALLENGE")
                put("groupId", "")
                put("message", challenge.name)
                put("send_at", challenge.created_at)
                put("data", challenge)
            }
        }



        _messages_merged.value = (challengeJson + messages)
            .sortedBy { obj ->
                obj.getString("send_at") // ⚠️ doit être un format ISO (yyyy-MM-dd...)
            }

        Log.d("MINE", _messages_merged.value.toString());
    }

    fun getGroupName(id: String) {
        viewModelScope.launch {
            val results = groupsRepository.getName(id)

            results.onSuccess {
                _name.value = it.get(0).name
            }.onFailure {
                error.emit("Serveur timeout")
            }
        }
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

    fun sendMessage(groupId: String, message: String) {
        viewModelScope.launch {
            val result = messagesRepository.sendMessage(groupId, message)

            result.onSuccess {
                socketRepository.sendGroupMessage(groupId, message, {
                    _messages.value += it
                })
//                ages(groupId)
            }.onFailure {
                error.emit(it.message ?: "Erreur lors de l'envoi du message")
            }
        }
    }

    fun joinGroup(groupId: String) {
        socketRepository.joinGroup(groupId);
    }


    fun startListening() {
        socketRepository.listenMessages { message ->
            val socketMessage = JSONObject(message)
            val socketGroupId = socketMessage.optString("groupId").removePrefix("group-")
            val groupId = currentGroupId

            if (groupId != null && socketGroupId == groupId) {
                _messages.value += socketMessage
//                getMessages(groupId)
            } else {
                _messages.value += socketMessage
            }
        }
    }

    fun getMessages(groupId: String) {
        currentGroupId = groupId

        viewModelScope.launch {
            val result = messagesRepository.getGroupMessages(groupId)

            result.onSuccess {
                _messages.value = it.map { message ->
                        JSONObject()
                            .put("type", "MESSAGE")
                            .put("id", message.id)
                            .put("nom", message.nom)
                            .put("groupId", message.group_id)
                            .put("message", message.msg)
                            .put("send_at", message.send_at)
                            .put("user_id", message.user_id ?: "")

                }
            }.onFailure {
                error.emit(it.message ?: "Erreur lors du chargement des messages")
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
