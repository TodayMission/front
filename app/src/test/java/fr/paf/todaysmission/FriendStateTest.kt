import fr.paf.todaysmission.models.Users
import fr.paf.todaysmission.repository.FriendRepository
import fr.paf.todaysmission.viewmodels.FriendViewModels
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import fr.paf.todaysmission.utils.State

@OptIn(ExperimentalCoroutinesApi::class)
class FriendStateTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: FriendRepository
    private lateinit var viewModel: FriendViewModels

    @Before
    fun setup() {
        repository = mockk()
    }

    @Test
    fun `getFriends success updates state and data`() = runTest {
        val fakeUsers = listOf(Users("1", ""), Users("2", ""))

        coEvery { repository.getFriends() } returns Result.success(fakeUsers)
        coEvery { repository.getPendingFriends() } returns Result.success(emptyList())
        coEvery { repository.getIncomingFriends() } returns Result.success(emptyList())

        viewModel = FriendViewModels(repository)

        advanceUntilIdle() //

        assert(viewModel.state.value == State.SUCCESS)
        assert(viewModel.friends.value == fakeUsers)
    }

    @Test
    fun `getFriends failure sets error state`() = runTest {
        coEvery { repository.getFriends() } returns Result.failure(Exception())
        coEvery { repository.getPendingFriends() } returns Result.success(emptyList())
        coEvery { repository.getIncomingFriends() } returns Result.success(emptyList())

        viewModel = FriendViewModels(repository)

        advanceUntilIdle()

        assert(viewModel.state.value == State.ERROR)
        assert(viewModel.error.value == "Serveur timeout")
    }

    @Test
    fun `sendFriendRequest success sets success state`() = runTest {
        coEvery { repository.sendFriendRequest("1") } returns Result.success("")
        coEvery { repository.getFriends() } returns Result.success(emptyList())
        coEvery { repository.getPendingFriends() } returns Result.success(emptyList())
        coEvery { repository.getIncomingFriends() } returns Result.success(emptyList())

        viewModel = FriendViewModels(repository)

        viewModel.sendFriendRequest("1")
        advanceUntilIdle()

        assert(viewModel.state.value == State.SUCCESS)
    }
}