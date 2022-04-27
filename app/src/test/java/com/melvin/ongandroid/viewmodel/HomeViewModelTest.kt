package com.melvin.ongandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.MainDispatcherRule
import com.melvin.ongandroid.getOrAwaitValue
import com.melvin.ongandroid.model.News
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `check if List of News in ViewModel is the same that repo `()= runTest {

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




}