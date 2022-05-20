package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.Testimonial
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class TestimonialsViewModel  @Inject constructor(private val repo: OngRepository) : ViewModel() {
    private val _testimonialState: MutableLiveData<Resource<List<Testimonial>>> =
        MutableLiveData(Resource.loading())
    val testimonialState: LiveData<Resource<List<Testimonial>>> = _testimonialState

    fun fetchTestiomnial(){
        viewModelScope.launch(IO) {
            repo.fetchTestiominals()
                .catch { e ->
                    _testimonialState.postValue(Resource.errorThrowable(e))
                }
                .collect{recurso ->
                    when(recurso){
                        is Resource.Success -> _testimonialState.postValue(Resource.Success(recurso.data!!))
                        is Resource.ErrorApi -> _testimonialState.postValue(Resource.ErrorApi(recurso.errorMessage?:"Error"))
                        else -> Unit
                    }

            }
        }
    }


}