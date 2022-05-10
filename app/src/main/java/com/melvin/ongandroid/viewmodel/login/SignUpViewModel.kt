package com.melvin.ongandroid.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.R
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.login.DataUser
import com.melvin.ongandroid.model.login.RegisterUser
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.checkFirstOrLastName
import com.melvin.ongandroid.utils.isEmailValid
import com.melvin.ongandroid.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val repo: OngRepository
) : ViewModel() {

    // LiveData to disable/enable Register Button on Form when is correct
    private val _isRegisterBtnEnabled = MutableLiveData(false)
    val isRegisterBtnEnabled: LiveData<Boolean> = _isRegisterBtnEnabled

    // Parameters from the Form. So we can observe changes in ViewModel
    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    //Validation checks in order to facilitate tests
    var isNameValid = false
    var isEmailValid = false
    var isPasswordValid = false
    var isPasswordConfirmValid = false

    private val _registerUserState: MutableLiveData<Resource<GenericResponse<DataUser>>> =
        MutableLiveData(Resource.Loading())
    val registerUserState: LiveData<Resource<GenericResponse<DataUser>>> = _registerUserState

    /**
     * Set idle
     * created on 10 May 2022 by Leonel Gomez
     * To set an idle state to the live data response
     *
     */
    fun setIdle() {
        _registerUserState.postValue(Resource.idle())
    }

    /**
     * Check fields
     * created on 6 May 2022 by Leonel Gomez
     * Check if all parameters are valid
     * When Valid, [_isRegisterBtnEnabled] changes to true, enabling button.
     *
     */
    fun checkFields() {
        val condition = isNameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid
        _isRegisterBtnEnabled.postValue(condition)
    }

    /**
     * Check name
     * created on 6 May 2022 by Leonel Gomez
     * When Valid, [isNameValid] changes to true
     *
     * @param text name string
     * @return the error text string
     */
    fun checkName(text: CharSequence?): String? {
        name.value = text.toString()
        isNameValid = name.value.toString().checkFirstOrLastName()
        checkFields()
        return if (text.toString().isBlank())
            resourcesProvider.getString(R.string.validation_empty)
        else if (!isNameValid)
            resourcesProvider.getString(R.string.validation_name)
        else
            null
    }

    /**
     * Check email
     * created on 6 May 2022 by Leonel Gomez
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
     * created on 6 May 2022 by Leonel Gomez
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

    /**
     * Check password confirm
     * created on 6 May 2022 by Leonel Gomez
     * When Valid, [isPasswordConfirmValid] changes to true
     *
     * @param text email string
     * @return the error text string
     */
    fun checkPasswordConfirm(text: CharSequence?): String? {
        passwordConfirm.value = text.toString()
        isPasswordConfirmValid = password.value == passwordConfirm.value
        checkFields()
        return if (!isPasswordConfirmValid)  //Passwords are not the same
            resourcesProvider.getString(R.string.validation_password_match)
        else
            null
    }

    fun signUpUser() {
        val condition = isNameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid

        if (condition) {
            viewModelScope.launch(IO) {
                val registerUser = RegisterUser(
                    name = name.value!!, email = email.value!!, password = password.value!!
                )

                repo.signUpUser(registerUser)
                    .catch { e-> _registerUserState.postValue(Resource.errorThrowable(e))}
                    .collect{ resourceDataUser->
                        when(resourceDataUser){
                            is Resource.Success ->
                                _registerUserState.postValue(Resource.success(resourceDataUser.data!!))

                            is Resource.ErrorApi ->
                                _registerUserState.postValue(Resource.errorApi(resourceDataUser.data!!.message))

                            else -> Unit
                        }

                }
            }
        }
    }
}