package com.melvin.ongandroid.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.melvin.ongandroid.databinding.FragmentContactBinding
import com.melvin.ongandroid.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentContactBinding
    private val contactViewModel: ContactViewModel by activityViewModels()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(layoutInflater, container, false)

        contactViewModel.isButtonEneabled.observe(viewLifecycleOwner){
            binding.filledButton.isEnabled = it
        }

        return binding.root
    }



}