package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {
    private val _newsState: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData(Resource.loading())
    val newsState: LiveData<Resource<NewsResponse>> = _newsState

    init {
        fetchLatestNews()
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Collects from Repository and handle diverse States of response.
     * When
     * [Resource.Success] -> Request From Api was Succesfull
     * [Resource.ErrorApi] -> Request from Api had Errors
     * [Resource.ErrorThrowable] -> Catch Exceptions
     */
    fun fetchLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.fetchLatestNews()
                .catch { throwable -> println(throwable.message) }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success ->
                            _newsState.postValue(Resource.success(resource.data!!))

                        is Resource.ErrorApi ->
                            _newsState.postValue(Resource.errorApi(resource.errorMessage!!))

                        is Resource.ErrorThrowable ->
                            _newsState.postValue(Resource.errorThrowable(resource.errorThrowable!!))

                        is Resource.Loading ->
                            _newsState.postValue(Resource.loading())

                    }
                }
        }
    }
}