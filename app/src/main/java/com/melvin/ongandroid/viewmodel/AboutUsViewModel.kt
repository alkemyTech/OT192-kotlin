package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.MemberUI
import com.melvin.ongandroid.model.Members
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    /**
     * Member list of MutableLiveData type
     * created on 30 April 2022 by Leonel Gomez
     */
    private val _memberList = MutableLiveData<List<MemberUI>>()
    val memberList: LiveData<List<MemberUI>> = _memberList

    private val _membersState: MutableLiveData<Resource<GenericResponse<List<Members>>>> =
        MutableLiveData(Resource.loading())
    val membersState: LiveData<Resource<GenericResponse<List<Members>>>> = _membersState



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
        fetchMembers()
    }

    /**
     * Fetch Members from [OngRepository].Handles States when:
     * [Resource.Success] -> Post values with list of members.
     * [Resource.ErrorApi] -->Post Values with Error message
     * [Resource.ErrorThrowable] -> Catchs Exceptions.
     * [Resource.Loading] --> Handle State when loading
     *
     * IMPORTANT: this functions uses double bang operator !! because handles all possible
     * states from Response. This way we ensure that is never null.
     */

    fun fetchMembers(){
        viewModelScope.launch(IO) {
            repo.fetchMembers().collect{ resource->
                when(resource){
                    is Resource.Success ->
                        _membersState.postValue(Resource.success(resource.data!!))

                    is Resource.ErrorApi ->
                        _membersState.postValue(Resource.errorApi(resource.errorMessage!!))

                    is Resource.ErrorThrowable ->
                        _membersState.postValue(Resource.errorThrowable(resource.errorThrowable!!))

                    is Resource.Loading -> _membersState.postValue(Resource.loading())
                }
            }
        }
    }

}