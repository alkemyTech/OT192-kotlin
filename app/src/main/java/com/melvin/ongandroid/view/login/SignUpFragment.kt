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
     *
     */
    private fun setEditTextSettings() {
        with(binding.fragmentSignUpName) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                error = signUpViewModel.checkName(text)
            }
        }
        with(binding.fragmentSignUpEmail) {
            editText!!.doOnTextChanged { text, _, _, _ ->
                error = signUpViewModel.checkEmail(text)
            }
        }
        with(binding.fragmentSignUpPassword) {
            editText!!.doOnTextChanged { text, _, _, _ ->
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
                error = signUpViewModel.checkPasswordConfirm(text)
            }
        }
    }

    /**
     *
    When all fields are correct given signUpViewModel.checkFields() register new user.
    Handle States when Success.
    Error will be implemented in #24

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
                    }

                    is Resource.ErrorThrowable -> {
                        FirebaseEvent.setEvent(requireContext(),"sign_up_error")

                    }

                    is Resource.Loading -> {

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
}