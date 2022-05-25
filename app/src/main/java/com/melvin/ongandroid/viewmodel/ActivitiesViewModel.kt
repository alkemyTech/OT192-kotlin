package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.ActivityUI
import com.melvin.ongandroid.model.toUI
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    /**
     * Activity list of MutableLiveData Resource type
     * Parameter to check State of response Activity post request
     * created on 1 May 2022 by Leonel Gomez
     */
    private val _activitiesState: MutableLiveData<Resource<MutableList<ActivityUI>>> =
        MutableLiveData(
            Resource.loading()
        )
    val activitiesState: LiveData<Resource<MutableList<ActivityUI>>> = _activitiesState

    init {
        fetchActivities()
    }

    /**
     * Fetch activities
     * created on 1 May 2022 by Leonel Gomez
     *
     */
    fun fetchActivities() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getActivities()
                .catch { throwable ->
                    _activitiesState.postValue(
                        Resource.errorThrowable(Exception(throwable.message))
                    )
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> try {
                            if (resource.data != null) {
                                val values = resource.data.data.map { it.toUI() }
                                _activitiesState.postValue(Resource.success(values.toMutableList()))
                            }
                        } catch (e: Exception) {
                            _activitiesState.postValue(Resource.errorThrowable(e))
                        }
                        is Resource.ErrorApi -> _activitiesState.postValue(
                            Resource.errorApi(resource.errorMessage ?: "")
                        )
                        is Resource.ErrorThrowable -> _activitiesState.postValue(
                            Resource.errorThrowable(resource.errorThrowable ?: Exception(""))
                        )
                        is Resource.Loading -> _activitiesState.postValue(Resource.loading())
                    }
                }
        }
    }

}