package com.melvin.ongandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentHomeBinding
import com.melvin.ongandroid.view.adapters.HomeWelcomeItemAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melvin.ongandroid.model.toUI
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.gone
import com.melvin.ongandroid.utils.visible
import com.melvin.ongandroid.view.adapters.HomeTestimonialsItemAdapter
import com.melvin.ongandroid.view.adapters.NovedadesAdapter
import com.melvin.ongandroid.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val adapterWelcome = HomeWelcomeItemAdapter()
    private val adapterTestimonials = HomeTestimonialsItemAdapter()

    private lateinit var recyclerViewNovedades: RecyclerView
    private val adapter by lazy { NovedadesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.inicio)

        binding.textViewNovedadesHome.text = getString(R.string.novedades_titulo_home)




        /*  Observe Changes in HomeViewModel for latest News
           When news is empty hide Section. When Not, set adapter
           and show only 4 elemtns*/





        //Configuration of Welcome section
        binding.incSectionWelcome.tvTitleWelcome.text =
            getString(R.string.fragment_home_title_welcome)
        setupRecyclerViewSliderWelcome()

        setUpRecyclerViewNews()

        //Configuration of Testimonials section
        binding.incSectionTestimonials.tvTitleTestimonials.text =
            getString(R.string.fragment_home_title_testimonials)
        setupRecyclerViewSilderTestimonials()

        return binding.root
    }




    /**
     * Setup recycler view slider welcome
     * created on 19 April 2022 by Leonel Gomez
     * updated on 24 April 2022 by Leonel Gomez
     */
    private fun setupRecyclerViewSliderWelcome() {
        adapterWelcome.onItemClicked = { }
        //It is a horizontal recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //Initial configuration of recycler view
        with(binding) {
            incSectionWelcome.rvSliderWelcome.setHasFixedSize(true)
            incSectionWelcome.rvSliderWelcome.layoutManager = layoutManager
            incSectionWelcome.rvSliderWelcome.adapter = adapterWelcome
        }

        //the list is populated from a repository asynchronously and updated in live data
        homeViewModel.slideList.observe(viewLifecycleOwner) { result ->
            if (!result.isNullOrEmpty()) {
                //If data is obtained, it is loaded into the recycler adapter
                //There is a conversion (mapping) of Slide object to HomeWelcome object
                // (with enough attributes to display in the UI)
                adapterWelcome.submitList(result.map { it.toUI() })
                binding.incSectionWelcome.rvSliderWelcome.adapter = adapterWelcome

                //The section is displayed
                showHomeWelcomeSection()
            } else {
                //If the list is empty, this section is hidden
                showHomeWelcomeSection(show = false)
            }
        }
    }

    /**
     * Set Up Recycler View News with States from ViewModel
     * When [Resource.Success] -> Make Visible Recycler
     * Else -> Make Invisible Recyler
     */

    private fun setUpRecyclerViewNews(){
        recyclerViewNovedades = binding.recyclerNovedadesHome

        recyclerViewNovedades.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewNovedades.adapter = adapter

        homeViewModel.newsState.observe(viewLifecycleOwner) { estadoNoticias ->
            binding.apply {
                when(estadoNoticias){
                    is Resource.Success ->{
                        adapter.submitList(estadoNoticias.data!!.novedades.take(5))
                        textViewNovedadesHome.visible()
                        recyclerNovedadesHome.visible()
                    }
                    else ->{

                        textViewNovedadesHome.gone()
                        recyclerNovedadesHome.gone()
                    }


                }
            }
        }
    }


    /**
     * Setup recycler view slider testimonials
     * Observe changes in HomeViewModel for testimonials.
     * When testimonials is empty hide section, but when not, show section with only 4 elements
     */
    private fun setupRecyclerViewSilderTestimonials() {

        homeViewModel.testimonials.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (!response.data.isNullOrEmpty()) {
                    true -> {
                        adapterTestimonials.submitList(response.data.take(4).toMutableList())

                        adapterTestimonials.onItemClicked = { }
                        val layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                        incSectionTestimonials.rvSliderTestimonials.setHasFixedSize(true)
                        incSectionTestimonials.rvSliderTestimonials.layoutManager = layoutManager
                        incSectionTestimonials.rvSliderTestimonials.adapter = adapterTestimonials
                    }
                    false -> {
                        incSectionTestimonials.tvTitleTestimonials.visibility = View.GONE
                        incSectionTestimonials.rvSliderTestimonials.visibility = View.GONE
                    }
                }
            }
        }


    }

    /**
     * Show home welcome section
     * Show (or hide) home welcome section (title and recycler view)
     * created on 24 April 2022 by Leonel Gomez
     *
     * @param show Boolean default true, to indicate the show or hide (false) action
     */
    private fun showHomeWelcomeSection(show: Boolean = true) {
        with(binding) {
            if (show)
                incSectionWelcome.root.visibility = View.VISIBLE
            else
                incSectionWelcome.root.visibility = View.GONE
        }
    }

}