package com.melvin.ongandroid.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentLogInBinding
import com.melvin.ongandroid.utils.hideKeyboard
import com.melvin.ongandroid.viewmodel.login.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.application.SomosMasApp.Companion.prefs
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.view.MainActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private val logInViewModel: LogInViewModel by activityViewModels()
    private val facebookCallbackManager = CallbackManager.Factory.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)

        //Initial configuration
        logInViewModel.setIdle()
        setEditTextSettings()
        logInViewModel.checkFields()
        setListeners()
        setObservers()

        return binding.root
    }

    private fun setListeners() {
        //To hide keyboard when click on screen
        binding.frontLayout.setOnClickListener { it.hideKeyboard() }

        //To login when click on login button
        loginUser()

        //Navigation to Sign Up fragment
        irASignUp()

        //To log in with Facebook option
        facebookLogInListener()
    }

    private fun setObservers() {
        //Activation of Log In button with an observer
        logInViewModel.isLogInBtnEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.buttonLogIn.isEnabled = enabled
            binding.buttonLogIn.alpha = if (enabled) 1.0F else 0.3F
        }

        // Observe the state of the login and manage the logic of the login
        logInViewModel.loginState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Save token in shared preferences
                    lifecycleScope.launch {
                        prefs.saveUserToken(result.data?.token ?: "")
                    }
                    //Navigate to Home fragment
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                is Resource.Loading -> {
                    // Show Progress bar
                    enableUI(false)
                }
                is Resource.ErrorThrowable -> {
                    // Hide Progress bar
                    enableUI(true)

                    // Show Dialog with error message when an error occurs
                    handleExceptions(result.errorThrowable)

                    // Reset error after being displayed
                    logInViewModel.setIdle()

                }
                is Resource.ErrorApi -> {
                    // Hide Progress bar
                    enableUI(true)

                    // Show Dialog with error message when an error occurs
                    handleExceptions(result.errorMessage ?: getString(R.string.dialog_error))

                    // Reset error after being displayed
                    logInViewModel.setIdle()

                }
                is Resource.Idle -> {
                    // Hide Progress bar
                    enableUI(true)
                }
            }
        }

    }

    /**
     * Set edit text settings
     * doOnTextChange action for email and password fields
     * and modify the error field of the respective layouts
     * created on 5 May 2022 by Leonel Gomez
     * updated on 9 May 2022 by Leonel Gomez: reset credential errors
     *
     */
    private fun setEditTextSettings() {
        with(binding.textInputLayoutEmailLogIn) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                //Reset the credential error of the other field
                binding.textInputLayoutPasswordLogIn.apply {
                    if (error == getString(R.string.dialog_error_credentials)) error = ""
                }
                error = logInViewModel.checkEmail(text)
            }
        }
        with(binding.textInputLayoutPasswordLogIn) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                //Reset the credential error of the other field
                binding.textInputLayoutEmailLogIn.apply {
                    if (error == getString(R.string.dialog_error_credentials)) error = ""
                }
                error = logInViewModel.checkPassword(text)
            }
        }
    }

    /**
     * Navigates to [R.id.action_logInFragment_to_SignUpFragment]
     * when user clicks on button SignUp
     */
    private fun irASignUp() {
        binding.buttonSignUpLogin.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_logInFragment_to_SignUpFragment)
        }
    }

    
    // Log In user when user clicks on button Log In.
    private fun loginUser() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.textInputLayoutEmailLogIn.editText?.text.toString()
            val password = binding.textInputLayoutPasswordLogIn.editText?.text.toString()
            logInViewModel.loginUser(email, password)
            // Show Progress bar
            enableUI(false)
        }
    }

    // Enable UI when loading data is finished
    private fun enableUI(enable: Boolean) {
        binding.progressLoader.root.visibility = if (enable) View.GONE else View.VISIBLE
    }

    /**
     * Handle exceptions
     * created on 9 May 2022 by Leonel Gomez
     *
     * @param errorMessage
     */
    private fun handleExceptions(errorMessage: String) {
        val message = when (errorMessage) {
            "No token" -> {
                // Credential error message is displayed on typed fields
                binding.textInputLayoutEmailLogIn.error =
                    getString(R.string.dialog_error_credentials)
                binding.textInputLayoutPasswordLogIn.error =
                    getString(R.string.dialog_error_credentials)
                getString(R.string.dialog_error_credentials)
            }
            else -> errorMessage
        }
        // Show a modal/dialog with the text of the error
        showDialog(
            title = getString(R.string.dialog_error),
            message = message
        )
    }

    /**
     * Handle exceptions
     * created on 9 May 2022 by Leonel Gomez
     *
     * @param exception from a throwable
     */
    private fun handleExceptions(exception: Throwable?) {
        when (exception) {
            is HttpException -> {
                val message = when (exception.code()) {
                    400 -> getString(R.string.dialog_error_bad_request)
                    404 -> getString(R.string.dialog_error_resource_not_found)
                    in 405..499 -> getString(R.string.dialog_error_client_error)
                    in 500..599 -> getString(R.string.dialog_error_server_error)
                    else -> getString(R.string.dialog_error_unknown_error)
                }
                showDialog(
                    title = getString(R.string.dialog_error),
                    message = message
                )
            }
            is UnknownHostException -> {
                showDialog(
                    title = getString(R.string.dialog_error),
                    message = getString(R.string.dialog_error_connection)
                )
            }
            else -> {
                showDialog(
                    title = getString(R.string.dialog_error),
                    message = getString(R.string.dialog_error)
                )
            }
        }
    }

    /**
     * Show dialog
     * created on 9 May 2022 by Leonel Gomez
     *
     * @param title string title text
     * @param message string message text
     * @param negative string text in the negative button, default null (not showed)
     * @param positive string text in the positive button, default null (not showed)
     * @param callback function that is called when positive button is clicked, default null (no action)
     */
    private fun showDialog(
        title: String,
        message: String,
        negative: String? = null,
        positive: String? = null,
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negative) { _, _ -> }
            .setPositiveButton(positive) { _, _ -> callback?.invoke() }
            .show()
    }

    // Facebook listener to respond to the click of the button - 16/05/2022 L.Gomez
    private fun facebookLogInListener() {
        binding.buttonFacebookLogin.setOnClickListener {
            facebookLogInAction()
        }
    }

    // Facebook action to try to log in with Facebook - 15/05/2022 L.Gomez
    private fun facebookLogInAction() {
        // Show Progress bar
        enableUI(false)

        // Open Facebook Auth window
        LoginManager.getInstance()
            .logInWithReadPermissions(requireActivity(), facebookCallbackManager, listOf("email"))

        // Registers a login callback
        LoginManager.getInstance().registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    showDialog(
                        getString(R.string.dialog_cancel),
                        getString(R.string.dialog_cancel_facebook)
                    )
                    // Hide Progress bar
                    enableUI(true)
                }

                override fun onError(error: FacebookException) {
                    // Show error message
                    showDialog(
                        getString(R.string.dialog_error),
                        getString(R.string.dialog_error_credentials)
                    )
                    // Hide Progress bar
                    enableUI(true)
                }

                override fun onSuccess(result: LoginResult) {
                    // Show Progress bar
                    enableUI(false)

                    // Log token in Firebase Auth
                    result.accessToken.let { token ->
                        logInViewModel.logInWithFacebook(token.token)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK - 15/05/2022 L.Gomez
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

}