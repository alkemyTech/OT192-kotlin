package com.melvin.ongandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.MainDispatcherRule
import com.melvin.ongandroid.getOrAwaitValue
import com.melvin.ongandroid.model.News
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.ResponseBody
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

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var ruleInstantExecutor: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: OngRepository

    private val viewModel: HomeViewModel by lazy { HomeViewModel(repository) }


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    /** All method in viewModel are privet and executed on init block. That's is why, for now,
     * we are using [HomeViewModel.retryApiCallsHome]. */
    @Test
    fun `News State should be Success  when api request of news is successful`() = runTest {

        val response = NewsResponse(true, listOf(News(23)))

        //Define response for latest news Api Request

        coEvery { repository.fetchLatestNews() }.returns(flowOf(Resource.success(response)))

        //When
        viewModel.retryApiCallsHome()

        /**Verify tha [OngRepository.fetchLatestNews] is called.
         */
        coVerify {
            repository.fetchLatestNews()
        }

        //This Should be True
        assert(viewModel.newsState.getOrAwaitValue() is Resource.Success)
    }

    @Test
    fun `check if List of News in ViewModel is the same that repo `() = runTest {

        val response = NewsResponse(true, listOf(News(23)))

        //Define response for latest news Api Request
        coEvery { repository.fetchLatestNews() }.returns(flowOf(Resource.success(response)))

        //When
        viewModel.retryApiCallsHome()

        /**Verify tha [OngRepository.fetchLatestNews] is called.
         */
        coVerify {
            repository.fetchLatestNews()
        }

        //This Should be True
        assert(viewModel.newsState.getOrAwaitValue().data?.novedades?.firstOrNull() == response.novedades.first())
    }

    @Test
    fun `News States should be ErrorApi when there was an error in ApiRequest `() = runTest {

        // Bad Response
        val response = Response.error<NewsResponse>(
            400,
            "{\"key\":[\"somestuff\"]}".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery { repository.fetchLatestNews() }.returns(
            flowOf(
                Resource.errorApi(response.errorBody().toString())
            )
        )
        //When
        viewModel.retryApiCallsHome()

        //Verify
        coVerify {
            repository.fetchLatestNews()
        }

        // This Shuold be True
        assert(viewModel.newsState.getOrAwaitValue() is Resource.ErrorApi)
    }


}