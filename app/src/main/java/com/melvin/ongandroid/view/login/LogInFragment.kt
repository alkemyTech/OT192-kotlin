package com.melvin.ongandroid.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentLogInBinding
import com.melvin.ongandroid.utils.hideKeyboard
import com.melvin.ongandroid.viewmodel.login.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun setObservers() {
        //Activation of Log In button with an observer
        logInViewModel.isLogInBtnEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.buttonLogIn.isEnabled = enabled
            binding.buttonLogIn.alpha = if (enabled) 1.0F else 0.3F
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

}