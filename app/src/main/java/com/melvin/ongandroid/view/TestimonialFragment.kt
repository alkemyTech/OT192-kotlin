package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentTestimonialBinding
import com.melvin.ongandroid.viewmodel.TestimonialsViewModel


class TestimonialFragment : Fragment() {
    private lateinit var binding: FragmentTestimonialBinding

    private val viewModel: TestimonialsViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestimonialBinding.inflate(layoutInflater, container, false)


        return binding.root
    }


}