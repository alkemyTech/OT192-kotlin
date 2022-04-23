package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.checkContactMessage
import com.melvin.ongandroid.utils.checkFirstOrLastName
import com.melvin.ongandroid.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    // LiveData to disable/enable Button on ContactForm when is correct
    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEneabled: LiveData<Boolean> = _isButtonEnabled

    // Parameters from the Contact Form. So we can observe changes in ViewModel
    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val contactMessage = MutableLiveData<String>("")

    /**
     * Function that checks if all parameters from Consulta are valid.
     * When Valid, [_isButtonEnabled] changes to true, enabling button.
     */
    fun checkContactFormViewModel() {
        val condition =
            firstName.value.toString().checkFirstOrLastName()
                    && lastName.value.toString().checkFirstOrLastName()
                    && email.value.toString().isEmailValid()
                    && contactMessage.value.toString().checkContactMessage()
        _isButtonEnabled.postValue(condition)
    }
}