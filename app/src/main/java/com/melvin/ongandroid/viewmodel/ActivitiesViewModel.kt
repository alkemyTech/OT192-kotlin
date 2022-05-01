package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.model.ActivityUI
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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
     *
     */
    private fun fetchActivities() {
        //TODO: Hardcoded Activities list, replace with data from API
        //TODO: After fetching data, map Activity to ActivityUI object -> map { it.toUI() }
        _activitiesState.postValue(Resource.success(MutableList(12) {
            ActivityUI(
                image = "https://picsum.photos/200/300?random=${(1..100).random()}",
                name = "Apoyo Escolar para el nivel Primario",
                description = "<p>Es un programa destinado a jóvenes a partir del tercer año de secundaria, cuyo objetivo es garantizar su permanencia en el colegio y servir de guía y soporte.&nbsp;</p>",
            )
        }))
    }

}