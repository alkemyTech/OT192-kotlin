package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.inicio)

        binding.textView.text = getString(R.string.inicio)

        return binding.root
    }


}