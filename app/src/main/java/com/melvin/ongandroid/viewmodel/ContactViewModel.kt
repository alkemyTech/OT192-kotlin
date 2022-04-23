package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.repository.OngRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(repo: OngRepository) : ViewModel() {

    // LiveData to disable/enable Button on ContactForm when is correct
    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEneabled: LiveData<Boolean> = _isButtonEnabled

    // Parameters from the Contact Form. So we can observe changes in ViewModel
    val firstName  = MutableLiveData<String>("")
    val lastName  = MutableLiveData<String>("")
    val email  = MutableLiveData<String>("")
    val conctactMessage  = MutableLiveData<String>("")
}