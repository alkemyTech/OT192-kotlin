package com.melvin.ongandroid.view.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentSignUpBinding
import com.melvin.ongandroid.services.firebase.FirebaseEvent
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.hideKeyboard
import com.melvin.ongandroid.viewmodel.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.UnknownHostException

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        //Initial configuration
        signUpViewModel.setIdle()
        setEditTextSettings()
        signUpViewModel.checkFields()
        setListeners()
        setObservers()

        signUpNewUser()

        return binding.root
    }

    private fun setListeners() {
        //To hide keyboard when click on screen
        binding.frontLayout.setOnClickListener { it.hideKeyboard() }
    }

    private fun setObservers() {
        //Activation of Register button with an observer
        signUpViewModel.isRegisterBtnEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.fragmentSignUpButton.isEnabled = enabled
            binding.fragmentSignUpButton.alpha = if (enabled) 1.0F else 0.3F
        }
    }

    /**
     * Set edit text settings
     * doOnTextChange action for name, email and passwords fields
     * and modify the error field of the respective layouts
     * created on 6 May 2022 by Leonel Gomez
     * updated on 10 May 2022 by Leonel Gomez: reset register errors
     *
     */
    private fun setEditTextSettings() {
        with(binding.fragmentSignUpName) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                // Reset the error of all the fields
                if(error == getString(R.string.dialog_error_register)) {
                    clearErrorFields()
                }
                // Check errors in this field
                error = signUpViewModel.checkName(text)
            }
        }
        with(binding.fragmentSignUpEmail) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                // Reset the error of all the fields
                if(error == getString(R.string.dialog_error_register)) {
                    clearErrorFields()
                }
                // Check errors in this field
                error = signUpViewModel.checkEmail(text)
            }
        }
        with(binding.fragmentSignUpPassword) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                // Reset the error of all the fields
                if(error == getString(R.string.dialog_error_register)) {
                    clearErrorFields()
                }
                // Check errors in this field
                error = signUpViewModel.checkPassword(text)
                if (text != "") {
                    //To reset error in case of password match
                    binding.fragmentSignUpPasswordConfirm.apply {
                        this.error = signUpViewModel.checkPasswordConfirm(this.editText!!.text)
                    }
                }
            }
        }
        with(binding.fragmentSignUpPasswordConfirm) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                // Reset the error of all the fields
                if(error == getString(R.string.dialog_error_register)) {
                    clearErrorFields()
                }
                // Check errors in this field
                error = signUpViewModel.checkPasswordConfirm(text)
            }
        }
    }

    /**
     *
    When all fields are correct given signUpViewModel.checkFields() register new user.
    Handle States when Success.
    Errors are displayed in a dialog

     Added FireBaseEventes.
        When button is pressed.
        SignUp Success.
        SignUp Error.  
        */

    private fun signUpNewUser() {
        binding.fragmentSignUpButton.setOnClickListener {

            FirebaseEvent.setEvent(requireContext(),"register_pressed")
            
            signUpViewModel.signUpUser()
            signUpViewModel.registerUserState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        showDialog()

                        FirebaseEvent.setEvent(requireContext(),"sign_up_success")
                    }

                    is Resource.ErrorApi -> {
                        FirebaseEvent.setEvent(requireContext(),"sign_up_error")

                        // Show Dialog with error message when an error occurs
                        handleExceptions(it.errorMessage ?: getString(R.string.dialog_error))

                        // Reset error after being displayed
                        signUpViewModel.setIdle()

                        //Show text error in fields
                        showErrorInFields(getString(R.string.dialog_error_register))
                    }

                    is Resource.ErrorThrowable -> {
                        FirebaseEvent.setEvent(requireContext(),"sign_up_error")

                        // Show Dialog with error message when an error occurs
                        handleExceptions(it.errorThrowable)

                        // Reset error after being displayed
                        signUpViewModel.setIdle()

                        //Show text error in fields
                        showErrorInFields(getString(R.string.dialog_error_register))
                    }

                    is Resource.Loading -> {

                    }
                    is Resource.Idle -> {

                    }
                }
            }
        }
    }

    /**
     * Creates a [MaterialAlertDialogBuilder] to show case of success.
     * Has a button that will navigate to main Activity.
     * After Button has been clicked and User was registered, clears all fields
     */

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("Usuario Registrado")
        dialog.setMessage("User was succesfully register")
        dialog.setPositiveButton("Aceptar") { dialogo, wich ->
            requireView().findNavController().navigate(R.id.action_SignUpFragment_to_logInFragment)

            clearRegisterUser()

        }
        dialog.show()
    }

    private fun clearRegisterUser() {
        binding.apply {
            fragmentSignUpName.editText?.text?.clear()
            fragmentSignUpEmail.editText?.text?.clear()
            fragmentSignUpPassword.editText?.text?.clear()
            fragmentSignUpPasswordConfirm.editText?.text?.clear()
        }
    }

    /**
     * Clear error fields on text layouts
     * created on 10 May 2022 by Leonel Gomez
     *
     */
    private fun clearErrorFields() {
        binding.apply {
            fragmentSignUpName.error = null
            fragmentSignUpEmail.error = null
            fragmentSignUpPassword.error = null
            fragmentSignUpPasswordConfirm.error = null
        }
    }

    /**
     * Show error in fields
     * created on 10 May 2022 by Leonel Gomez
     *
     * @param errorMessage the text to be printed in the layout error texts
     */
    private fun showErrorInFields(errorMessage: String) {
        binding.apply {
            fragmentSignUpName.error = errorMessage
            fragmentSignUpEmail.error = errorMessage
            fragmentSignUpPassword.error = errorMessage
            fragmentSignUpPasswordConfirm.error = errorMessage
        }
    }

    /**
     * Handle exceptions
     * created on 10 May 2022 by Leonel Gomez
     *
     * @param errorMessage
     */
    private fun handleExceptions(errorMessage: String) {
        // Show a modal/dialog with the text of the error
        showDialog(
            title = getString(R.string.dialog_error),
            message = errorMessage
        )
    }

    /**
     * Handle exceptions
     * created on 10 May 2022 by Leonel Gomez
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
     * created on 10 May 2022 by Leonel Gomez
     *
     * @param title string title text
     * @param message string message text
     * @param negative string text in the negative button, default null (not showed)
     * @param positive string text in the positive button, default null (not showed)
     * @param negativeCallback function that is called when negative button is clicked or cancel dialog, default null (no action)
     * @param positiveCallback function that is called when positive button is clicked, default null (no action)
     */
    private fun showDialog(
        title: String,
        message: String,
        negative: String? = null,
        positive: String? = null,
        negativeCallback: (() -> Unit)? = null,
        positiveCallback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negative) { _, _ -> negativeCallback?.invoke() }
            .setPositiveButton(positive) { _, _ -> positiveCallback?.invoke() }
            .show()
    }
}