package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.model.MemberUI
import com.melvin.ongandroid.repository.OngRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    /**
     * Member list of MutableLiveData type
     * created on 30 April 2022 by Leonel Gomez
     */
    private val _memberList = MutableLiveData<List<MemberUI>>()
    val memberList: LiveData<List<MemberUI>> = _memberList

    init {
        //TODO: Hardcoded Member list, replace with data from API
        //TODO: After fetching data, map Member to MemberUI object -> map { it.toUI() }
        _memberList.value = MutableList(12) {
            MemberUI(
                photo = "https://picsum.photos/200/300?random=${(1..100).random()}",
                name = "Nombre ${(1..100).random()}",
                description = "Descripción ${(1..100).random()}"
            )
        }
    }
}