package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentAboutUsBinding
import com.melvin.ongandroid.viewmodel.AboutUsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentAboutUsBinding
    private val aboutUsViewModel: AboutUsViewModel by activityViewModels()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)

        // Change toolbar title
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.nosotros)

        return binding.root
    }

}