package com.melvin.ongandroid.viewmodel.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.melvin.ongandroid.R
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.model.login.DataUser
import com.melvin.ongandroid.model.login.User
import com.melvin.ongandroid.repository.OngAuthRepository
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.isEmailValid
import com.melvin.ongandroid.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val repo: OngRepository,
    private val ongAuthRepository: OngAuthRepository
) : ViewModel() {

    private val _loginState: MutableLiveData<Resource<DataUser>> = MutableLiveData()
    val loginState: LiveData<Resource<DataUser>> = _loginState

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
     * Set idle
     * created on 9 May 2022 by Leonel Gomez
     * To set an idle state to the live data response
     *
     */
    fun setIdle() {
        _loginState.postValue(Resource.idle())
    }

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


    //Log In User with email and password and save the response in [_loginState]
    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            repo.login(email, password)
                .catch { throwable ->
                    _loginState.postValue(
                        Resource.errorThrowable(Exception(throwable.message))
                    )
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> try {
                            if (resource.data != null) {
                                val values = resource.data.data
                                _loginState.postValue(Resource.success(values))
                            }
                        } catch (e: Exception) {
                            _loginState.postValue(Resource.errorThrowable(e))
                        }
                        is Resource.ErrorApi -> _loginState.postValue(
                            Resource.errorApi(resource.errorMessage ?: "")
                        )
                        is Resource.ErrorThrowable -> _loginState.postValue(
                            Resource.errorThrowable(resource.errorThrowable ?: Exception(""))
                        )
                        is Resource.Loading -> _loginState.postValue(Resource.loading())
                    }
                }
        }
    }


    /**
     * Function required for Intent In LogInActivity. We need to override start activity for result,
     * with an Intent and a requestCode.
     * Also it needs the id provided in the google firebase json. Added A string resource for this.
     */
    fun startLoginGoogle(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.server_client_id))
            .requestEmail()
            .build()

        val cliente = GoogleSignIn.getClient(activity, gso)

        val signInIntent = cliente.signInIntent
        activity.startActivityForResult(signInIntent, 1)

    }

    /**
     * After the override onActivityResult  LogInActivity we will have a SignedAccount named
     * [accountTask]. This is the result from GoogleSignIn.getSignedInAccountFromIntent(Intent).
     * If the token given [accountTask] is not null, then we can check if the login was a success or
     * not. Given the answer we set state in [_loginState].
     */
    fun finishLoginGoogle(accountTask: Task<GoogleSignInAccount>) {
        viewModelScope.launch(IO) {
            try {
                val account = accountTask.getResult(ApiException::class.java)

                account.idToken.let { token ->
                    val auth = FirebaseAuth.getInstance()
                    val credential = GoogleAuthProvider.getCredential(token, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                Log.d("USUARIO GOOGLE", "${user?.displayName}")

                                _loginState.postValue(Resource.success(DataUser(
                                    User(name = user?.displayName?:""), token = token!!
                                )))

                            } else {
                                _loginState.postValue(Resource.errorApi("Error"))
                            }
                        }
                }
                
            } catch (e: ApiException) {
                _loginState.postValue(Resource.errorThrowable(e))
                
                }
        }
    }
    

    //Log In User with facebook token and save the response in [_loginState] - 15/05/2022 L.Gomez
    fun logInWithFacebook(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = ongAuthRepository.signInWithCredential(token)) {
                is Resource.Success -> {
                    result.data.let {
                        if (it != null)
                            _loginState.postValue(Resource.success(it))
                        else
                            _loginState.postValue(Resource.errorThrowable(Exception("Error")))
                    }
                }
                is Resource.ErrorThrowable -> {
                    _loginState.postValue(Resource.errorThrowable(Exception("Error")))
                }
                is Resource.ErrorApi -> {}
                is Resource.Idle -> {}
                is Resource.Loading -> _loginState.postValue(Resource.loading())

            }
        }
    }


}

