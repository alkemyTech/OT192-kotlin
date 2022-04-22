package com.melvin.ongandroid.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.melvin.ongandroid.databinding.FragmentContactBinding
import com.melvin.ongandroid.viewmodel.ContactViewModel

class ContactFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentContactBinding
    private lateinit var viewModel: ContactViewModel

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // View Model
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
    }

}