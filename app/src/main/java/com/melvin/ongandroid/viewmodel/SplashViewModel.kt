package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.application.SomosMasApp.Companion.prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    // LiveData to know if the user is logged in or not
    private var _userLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val userLoggedIn: LiveData<Boolean> = _userLoggedIn

    init {
        checkUserLoggedIn()
    }

    // Check if the user is logged in from token in shared preferences
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val tokenUser = prefs.getUserToken()
            if (tokenUser != "") {
                _userLoggedIn.postValue(true)
            } else {
                _userLoggedIn.postValue(false)
            }
        }
    }

}