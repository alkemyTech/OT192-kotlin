package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentTestimonialBinding
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.gone
import com.melvin.ongandroid.utils.visible
import com.melvin.ongandroid.view.adapters.TestimonialAdapter
import com.melvin.ongandroid.viewmodel.TestimonialsViewModel


class TestimonialFragment : Fragment() {
    private lateinit var binding: FragmentTestimonialBinding

    private val viewModel: TestimonialsViewModel by activityViewModels()
    private lateinit var recycler: RecyclerView
    private val adaptador by lazy { TestimonialAdapter() }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestimonialBinding.inflate(layoutInflater, container, false)

        recycler = binding.recyclerViewTestimonials

        setUpRecycler()
        // Change toolbar title
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.testimonios)

        return binding.root
    }


    private fun setUpRecycler(){
        recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }


        viewModel.testimonialState.observe(viewLifecycleOwner){
            binding.apply {
                when(it){
                    is Resource.Success ->{
                        progressLoader.root.gone()
                        scrollViewTestimonials.visible()
                        adaptador.submitList(it.data)

                    }

                    is Resource.Loading ->{
                        progressLoader.root.visible()
                        scrollViewTestimonials.gone()
                    }

                    is Resource.ErrorThrowable ->{
                        progressLoader.root.gone()
                        scrollViewTestimonials.gone()
                        showDialog(callback = { viewModel.fetchTestiomnial() })

                    }

                    is Resource.ErrorApi ->{
                        progressLoader.root.gone()
                        scrollViewTestimonials.gone()
                        showDialog(
                            message = it.errorMessage?: "Error",
                            callback = { viewModel.fetchTestiomnial() })

                    }

                    else -> Unit
                }

            }
        }
    }


    private fun showDialog(
        title: String = "Hubo Un Error",
        message: String = "Hubo un error, Intentele nuevamente",
        negative: String? = null,
        positive: String? = null,
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negative) { _, _ -> }
            .setPositiveButton(positive) { _, _ -> callback?.invoke() }
            .show()
    }

}