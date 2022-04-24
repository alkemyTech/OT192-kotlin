package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.Contact
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.checkContactMessage
import com.melvin.ongandroid.utils.checkFirstOrLastName
import com.melvin.ongandroid.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    // LiveData to disable/enable Button on ContactForm when is correct
    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEneabled: LiveData<Boolean> = _isButtonEnabled

    // LiveData that save the contact response
    private val _contacts = MutableLiveData<GenericResponse<List<Contact>>>()
    val contacts: LiveData<GenericResponse<List<Contact>>> = _contacts

    // LiveData to show/hide progress bar
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

    /*
    * Function that sends the Contact Form to the server, and post the response to [_contacts]
    * Also, it sets [_isLoading] to true or false depending the state of the request
     */
    fun sendFormContact(contact: Contact){
        viewModelScope.launch(IO) {
            _isLoading.postValue(true)
            repo.sendContact(contact).collect(){ contactResponse ->
                _contacts.postValue(contactResponse)
                _isLoading.postValue(false)
            }

        }
    }
}