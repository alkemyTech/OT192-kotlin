package com.melvin.ongandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.MainDispatcherRule
import com.melvin.ongandroid.getOrAwaitValue
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.Members
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class AboutUsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var ruleInstantExecutor: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: OngRepository

    private val viewModel: AboutUsViewModel by lazy { AboutUsViewModel(repository) }


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `Members State should be Success when api request of members is successful`() = runTest {

        val response = GenericResponse(true, listOf(Members(10)))

        // Define response for members Api Request
        coEvery { repository.fetchMembers() }.returns(flowOf(Resource.success(response)))

        // When
        viewModel.fetchMembers()

        // Verify
        coVerify { repository.fetchMembers() }

        // This Should be True
        assert(viewModel.membersState.getOrAwaitValue() is Resource.Success)
    }

    @Test
    fun `Check if List of Members in ViewModel is the same that repo`() = runTest {

        val response = GenericResponse(true, listOf(Members(10)))

        // Define response for members Api Request
        coEvery { repository.fetchMembers() }.returns(flowOf(Resource.success(response)))

        // When
        viewModel.fetchMembers()

        // Verify
        coVerify { repository.fetchMembers() }

        // This Should be True
        assert(viewModel.membersState.getOrAwaitValue().data?.data?.firstOrNull() == response.data.first())
    }

    @Test
    fun `Members state should be ErrorApi when there was an error in ApiRequest`() = runTest {

        // Bad Response
        val response = Response.error<GenericResponse<List<Members>>>(
            400,
            "{\"key\":[\"somestuff\"]}".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery { repository.fetchMembers() }.returns(
            flowOf(
                Resource.errorApi(response.errorBody().toString())
            )
        )

        // When
        viewModel.fetchMembers()

        // Verify
        coVerify { repository.fetchMembers() }

        // This Should be True
        assert(viewModel.membersState.getOrAwaitValue() is Resource.ErrorApi)
    }

    @Test
    fun `Members state should catch exceptions`() = runTest {
        // Simulated Exceptions
        val exception = IOException()
        coEvery { repository.fetchMembers() }.returns(flowOf(Resource.errorThrowable(exception)))

        // When
        viewModel.fetchMembers()

        // Verify
        coVerify { repository.fetchMembers() }

        // This Should be True
        assert(viewModel.membersState.getOrAwaitValue() is Resource.ErrorThrowable)
    }

}