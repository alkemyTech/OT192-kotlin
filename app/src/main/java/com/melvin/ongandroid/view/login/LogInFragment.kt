package com.melvin.ongandroid.view.login

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
import com.melvin.ongandroid.application.SomosMasApp.Companion.prefs
import com.melvin.ongandroid.utils.Resource
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)

        //Initial configuration
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
                    lifecycleScope.launch {
                        prefs.saveUserToken(result.data?.token ?: "")
                    }
                }
                is Resource.Loading -> {
                    // Show Progress bar
                    enableUI(false)
                }
                is Resource.ErrorThrowable -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Show Dialog with error message when an error occurs

                }
                is Resource.ErrorApi -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Show Dialog with error message when an error occurs

                }
            }
        }

    }

    /**
     * Set edit text settings
     * doOnTextChange action for email and password fields
     * and modify the error field of the respective layouts
     * created on 5 May 2022 by Leonel Gomez
     *
     */
    private fun setEditTextSettings() {
        with(binding.textInputLayoutEmailLogIn) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                error = logInViewModel.checkEmail(text)
            }
        }
        with(binding.textInputLayoutPasswordLogIn) {
            editText!!.doOnTextChanged { text, _, _, _ ->
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

}