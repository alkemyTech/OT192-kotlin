package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.Contact
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.checkContactMessage
import com.melvin.ongandroid.utils.checkFirstOrLastName
import com.melvin.ongandroid.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    // LiveData to disable/enable Button on ContactForm when is correct
    private val _isButtonEnabled = MutableLiveData(false)
    val isButtonEneabled: LiveData<Boolean> = _isButtonEnabled

    // Parameters from the Contact Form. So we can observe changes in ViewModel
    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val email = MutableLiveData("")
    val contactMessage = MutableLiveData("")

    //Parameter to check State of response Contact post request
    private val _contactResponseState: MutableLiveData<Resource<Contact>> = MutableLiveData()
    val contactResponseState : LiveData<Resource<Contact>> = _contactResponseState

    //Split validation checks in order to facilitate tests
    //created on 26 April 2022 by Leonel Gomez
    var isFirstNameValid = false
    var isLastNameValid = false
    var isEmailValid = false
    var isContactMessageValid = false

    /**
     * Function that checks if all parameters from Consulta are valid.
     * When Valid, [_isButtonEnabled] changes to true, enabling button.
     */
    fun checkContactFormViewModel() {
        //Split validation checks in order to facilitate tests
        //created on 26 April 2022 by Leonel Gomez
        isFirstNameValid = firstName.value.toString().checkFirstOrLastName()
        isLastNameValid = lastName.value.toString().checkFirstOrLastName()
        isEmailValid = email.value.toString().isEmailValid()
        isContactMessageValid = contactMessage.value.toString().checkContactMessage()
        val condition = isFirstNameValid
                && isLastNameValid
                && isEmailValid
                && isContactMessageValid
        _isButtonEnabled.postValue(condition)
    }

    /*
    * Function that sends the Contact Form to the server, and post the response to [_contacts]
    * Also, it sets [_isLoading] to true or false depending the state of the request
     */
    fun sendFormContact(contact: Contact){
        viewModelScope.launch(IO) {
            repo.sendContact(contact)
                .catch { throwable ->
                    _contactResponseState.postValue(
                        Resource.errorThrowable(Exception(throwable.message))
                    ) }
                .collect{ resource ->
                    when (resource) {
                        is Resource.Success -> try {
                            if (resource.data != null) {
                                val values = resource.data.data
                                _contactResponseState.postValue(Resource.success(values))
                            }
                        } catch (e: Exception) {
                            _contactResponseState.postValue(Resource.errorThrowable(e))
                        }
                        is Resource.ErrorApi -> _contactResponseState.postValue(
                            Resource.errorApi(resource.errorMessage ?: "")
                        )
                        is Resource.ErrorThrowable -> _contactResponseState.postValue(
                            Resource.errorThrowable(resource.errorThrowable ?: Exception(""))
                        )
                        is Resource.Loading -> _contactResponseState.postValue(Resource.loading())
                    }
            }
/*
                _contacts.postValue(contactResponse)
                _isLoading.postValue(false)
 */
        }
    }

    /**
     * Set idle
     * created on 9 May 2022 by Leonel Gomez
     * To set an idle state to the live data response
     *
     */
    fun setIdle() {
        _contactResponseState.postValue(Resource.idle())
    }

    // reset live data to default values
    fun resetContactForm() {
        firstName.value = ""
        lastName.value = ""
        email.value = ""
        contactMessage.value = ""
        _isButtonEnabled.value = false
    }

}