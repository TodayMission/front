package fr.paf.todaysmission

import MainDispatcherRule
import fr.paf.todaysmission.repository.AuthRepository
import fr.paf.todaysmission.repository.AuthSession
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


@OptIn(ExperimentalCoroutinesApi::class)
class LoginStateTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = LoginViewModel(repository)
    }

    // regarde si ça marche
    @Test
    fun `login success updates state and session`() = runTest {

        // fake data
        val fakeSession = AuthSession("token123")

        //  Used to define what behaviour is going to be mocked.
        coEvery { repository.login("user", "pass") } returns Result.success(fakeSession)

        // launch task
        viewModel.login("user", "pass")

        // wait
        advanceUntilIdle()

        // verification
        assert(viewModel.state.value == State.SUCCESS)
        assert(viewModel.session.value == fakeSession)
        assert(viewModel.error.value == null)
    }

    // regarde si ça marche pas
    @Test
    fun `login failure updates state and error message`() = runTest {
        val exception = Exception("Invalid credentials")

        coEvery { repository.login("user", "wrong") } returns Result.failure(exception)

        viewModel.login("user", "wrong")
        advanceUntilIdle()

        assert(viewModel.state.value == State.ERROR)
        assert(viewModel.session.value == null)
        assert(viewModel.error.value == "Invalid credentials")
    }

    // regarde si ça load bien
    @Test
    fun `login sets loading state before result`() = runTest {
        val fakeSession = AuthSession("token123")

        coEvery { repository.login(any(), any()) } coAnswers {
            Result.success(fakeSession)
        }

        viewModel.login("user", "pass")
        assert(viewModel.state.value == State.LOADING)
        advanceUntilIdle()

        assert(viewModel.state.value == State.SUCCESS)
    }
}