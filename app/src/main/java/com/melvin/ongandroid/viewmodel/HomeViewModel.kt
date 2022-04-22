package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.repository.OngRepository
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel @Inject constructor(private val repo: OngRepository): ViewModel() {

    private val _newsResponse: MutableLiveData<NewsResponse> = MutableLiveData(NewsResponse())
    val newsResponse: LiveData<NewsResponse> = _newsResponse

    init {
        fetchLatestNews()
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Uses Repository function with flow. When collected, posted values on[_newsResponse].
     */
    private fun fetchLatestNews(){
        viewModelScope.launch(IO) {
            repo.fetchLatestNews().collect{ newsResponse ->
                _newsResponse.postValue(newsResponse)
            }
        }

    }
}