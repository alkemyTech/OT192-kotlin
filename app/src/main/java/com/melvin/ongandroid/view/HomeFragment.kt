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
import com.melvin.ongandroid.model.HomeWelcome
import com.melvin.ongandroid.view.adapters.HomeWelcomeItemAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.Novedades
import com.melvin.ongandroid.view.adapters.HomeTestimonialsItemAdapter
import com.melvin.ongandroid.view.adapters.NovedadesAdapter
import com.melvin.ongandroid.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapterWelcome = HomeWelcomeItemAdapter()
    private val adapterTestimonials = HomeTestimonialsItemAdapter()

    private lateinit var recyclerViewNovedades: RecyclerView
    private val adapter by lazy { NovedadesAdapter() }

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.inicio)

        binding.textViewNovedadesHome.text = getString(R.string.novedades_titulo_home)

        recyclerViewNovedades = binding.recyclerNovedadesHome

        recyclerViewNovedades.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewNovedades.adapter = adapter


        /*  Observe Changes in HomeViewModel for latest News
           When news is empty hide Section. When Not, set adapter
           and show only 4 elemtns*/
        homeViewModel.newsResponse.observe(viewLifecycleOwner) { newsResponse ->
            binding.apply {
                when (newsResponse.novedades.isNotEmpty()) {
                    true -> {
                        adapter.submitList(newsResponse.novedades.take(5))
                        textViewNovedadesHome.visibility = View.VISIBLE
                        recyclerNovedadesHome.visibility = View.VISIBLE
                    }
                    false -> {

                        textViewNovedadesHome.visibility = View.GONE
                        recyclerNovedadesHome.visibility = View.GONE

                    }
                }
            }


        }


        //Configuration of Welcome section
        binding.incSectionWelcome.tvTitleWelcome.text =
            getString(R.string.fragment_home_title_welcome)
        setupRecyclerViewSliderWelcome()

        //Configuration of Testimonials section
        binding.incSectionTestimonials.tvTitleTestimonials.text =
            getString(R.string.fragment_home_title_testimonials)
        setupRecyclerViewSilderTestimonials()

        return binding.root
    }

    /**
     * Setup recycler view slider welcome
     */
    private fun setupRecyclerViewSliderWelcome() {
        //TODO: Harcoded random list
        val listHomeWelcome = MutableList(15) {
            HomeWelcome(
                title = "Actividad ${(1..100).random()}",
                imgUrl = "https://picsum.photos/200/300?random=${(1..100).random()}",
                description = "Descripción ${(1..100).random()}\n" +
                        "Descripción ${(1..100).random()}\n" +
                        "Descripción ${(1..100).random()}\n" +
                        "Descripción ${(1..100).random()}"
            )
        }

        adapterWelcome.submitList(listHomeWelcome)
        adapterWelcome.onItemClicked = { }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        with(binding) {
            incSectionWelcome.rvSliderWelcome.setHasFixedSize(true)
            incSectionWelcome.rvSliderWelcome.layoutManager = layoutManager
            incSectionWelcome.rvSliderWelcome.adapter = adapterWelcome
        }
    }

    /**
     * Setup recycler view slider testimonials
     */
    private fun setupRecyclerViewSilderTestimonials() {
        //TODO: Harcoded random list
        val listHomeTestimonials = MutableList(15) {
            HomeTestimonials(
                imgUrl = "https://picsum.photos/200/300?random=${(1..100).random()}",
                heading = "Descripción ${(1..100).random()}\n" +
                        "Descripción ${(1..100).random()}\n" +
                        "Descripción ${(1..100).random()}"
            )
        }

        adapterTestimonials.submitList(listHomeTestimonials.take(4).toMutableList())
        adapterTestimonials.onItemClicked = { }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        with(binding) {
            incSectionTestimonials.rvSliderTestimonials.setHasFixedSize(true)
            incSectionTestimonials.rvSliderTestimonials.layoutManager = layoutManager
            incSectionTestimonials.rvSliderTestimonials.adapter = adapterTestimonials
        }
    }

}