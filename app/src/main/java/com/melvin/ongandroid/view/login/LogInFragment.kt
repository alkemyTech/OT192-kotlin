package com.melvin.ongandroid.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)


        irASignUp()
        return binding.root
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


}