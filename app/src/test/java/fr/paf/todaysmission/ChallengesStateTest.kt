package fr.paf.todaysmission

import MainDispatcherRule
import fr.paf.todaysmission.repository.AuthRepository
import fr.paf.todaysmission.repository.AuthSession
import fr.paf.todaysmission.repository.ChallengesRepository
import fr.paf.todaysmission.repository.GroupChallenge
import fr.paf.todaysmission.viewmodels.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import fr.paf.todaysmission.utils.State
import fr.paf.todaysmission.viewmodels.ChallengesViewModel

class ChallengesStateTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: ChallengesRepository
    private lateinit var viewModel: ChallengesViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = ChallengesViewModel(repository)
    }

    @Test
    fun `getGroupChallenges success updates challenges`() = runTest {
        val fakeChallenges = listOf(
            GroupChallenge("1", "Challenge 1", false),
            GroupChallenge("2", "Challenge 2", true)
        )

        coEvery { repository.getGroupChallenges("group1") } returns Result.success(fakeChallenges)

        viewModel.getGroupChallenges("group1")
        advanceUntilIdle()

        assert(viewModel.challenges.value == fakeChallenges)
        assert(viewModel.message.value == null)
    }

    @Test
    fun `getGroupChallenges failure updates message`() = runTest {
        val exception = Exception("Erreur serveur")

        coEvery { repository.getGroupChallenges("group1") } returns Result.failure(exception)

        viewModel.getGroupChallenges("group1")
        advanceUntilIdle()
        assert(viewModel.message.value == "Erreur serveur")
    }

    @Test
    fun `createChallenge success updates message and refreshes list`() = runTest {
        val fakeChallenges = listOf(GroupChallenge("1", "New Challenge", false))

        coEvery { repository.createChallenge("New", "group1") } returns Result.success("Challenge créé")
        coEvery { repository.getGroupChallenges("group1") } returns Result.success(fakeChallenges)

        viewModel.createChallenge("New", "group1")
        advanceUntilIdle()

        assert(viewModel.message.value == "Challenge créé")
        assert(viewModel.challenges.value == fakeChallenges)
    }

    @Test
    fun `createChallenge failure updates message`() = runTest {
        val exception = Exception("Erreur création")

        coEvery { repository.createChallenge("New", "group1") } returns Result.failure(exception)
        viewModel.createChallenge("New", "group1")
        advanceUntilIdle()

        assert(viewModel.message.value == "Erreur création")
    }

    @Test
    fun `joinChallenge success updates message and refreshes list`() = runTest {
        val fakeChallenges = listOf(GroupChallenge("1", "Challenge", true))

        coEvery { repository.joinChallenge("challenge1") } returns Result.success(Unit)
        coEvery { repository.getGroupChallenges("group1") } returns Result.success(fakeChallenges)

        viewModel.joinChallenge("challenge1", "group1")
        advanceUntilIdle()

        assert(viewModel.message.value == "Vous avez rejoint le challenge")
        assert(viewModel.challenges.value == fakeChallenges)
    }


}