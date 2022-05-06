package com.melvin.ongandroid.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.R
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.utils.isEmailValid
import com.melvin.ongandroid.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    // LiveData to disable/enable Log In Button on Form when is correct
    private val _isLogInBtnEnabled = MutableLiveData(false)
    val isLogInBtnEnabled: LiveData<Boolean> = _isLogInBtnEnabled

    // Parameters from the Form. So we can observe changes in ViewModel
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    //Validation checks in order to facilitate tests
    var isEmailValid = false
    var isPasswordValid = false

    /**
     * Check fields
     * created on 5 May 2022 by Leonel Gomez
     * Check if all parameters are valid
     * When Valid, [_isLogInBtnEnabled] changes to true, enabling button.
     *
     */
    fun checkFields() {
        val condition = isEmailValid && isPasswordValid
        _isLogInBtnEnabled.postValue(condition)
    }

    /**
     * Check email
     * created on 5 May 2022 by Leonel Gomez
     * When Valid, [isEmailValid] changes to true
     *
     * @param text email string
     * @return the error text string
     */
    fun checkEmail(text: CharSequence?): String? {
        email.value = text.toString()
        isEmailValid = email.value.toString().isEmailValid()
        checkFields()
        return if (text.toString().isBlank())
            resourcesProvider.getString(R.string.validation_empty)
        else if (!isEmailValid)
            resourcesProvider.getString(R.string.validation_email)
        else
            null
    }

    /**
     * Check password
     * created on 5 May 2022 by Leonel Gomez
     * When Valid, [isPasswordValid] changes to true
     *
     * @param text email string
     * @return the error text string
     */
    fun checkPassword(text: CharSequence?): String? {
        password.value = text.toString()
        isPasswordValid = password.value.toString().isPasswordValid()
        checkFields()
        return if (text.toString().isBlank())
            resourcesProvider.getString(R.string.validation_empty)
        else if (!isPasswordValid)  //Password is too weak
            resourcesProvider.getString(R.string.validation_password)
        else
            null
    }

}