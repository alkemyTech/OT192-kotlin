package com.melvin.ongandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentHomeBinding
import com.melvin.ongandroid.model.HomeWelcome
import com.melvin.ongandroid.view.adapters.HomeWelcomeItemAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melvin.ongandroid.model.Novedades
import com.melvin.ongandroid.view.adapters.HomeTestimonialsItemAdapter
import com.melvin.ongandroid.view.adapters.NovedadesAdapter
import com.melvin.ongandroid.viewmodel.HomeViewModel


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

        recyclerViewNovedades = binding.recyclerNovedadesHome

        recyclerViewNovedades.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewNovedades.adapter = adapter

        val listaNovedades = listOf(
            Novedades(
                image = "https://d2gg9evh47fn9z.cloudfront.net/1600px_COLOURBOX32490000.jpg",
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Donec condimentum ex ut varius interdum"
            ),
            Novedades(
                image = "https://molpsg.com/wp-content/uploads/2016/07/chairty.jpg",
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Donec condimentum ex ut varius interdum"
            ),
            Novedades(
                image ="https://media.bizj.us/view/img/10273770/howtovounteer.jpg",
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Donec condimentum ex ut varius interdum"
            ),
            Novedades(
                image = "https://603200.smushcdn.com/1038623/wp-content/uploads/2019/08/IMG_9691-1080x675.jpg?lossy=1&strip=1&webp=1",
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Donec condimentum ex ut varius interdum"
            ),
            Novedades(
                image = "https://603200.smushcdn.com/1038623/wp-content/uploads/2019/08/IMG_9691-1080x675.jpg?lossy=1&strip=1&webp=1",
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Donec condimentum ex ut varius interdum"
            )
        ).toMutableList()

        adapter.submitList(listaNovedades.take(5))

        //Configuration of Welcome section
        binding.incSectionWelcome.tvTitleWelcome.text = getString(R.string.fragment_home_title_welcome)
        setupRecyclerViewSliderWelcome()

        //Configuration of Testimonials section
        binding.incSectionTestimonials.tvTitleTestimonials.text = getString(R.string.fragment_home_title_testimonials)
        homeViewModel.onCreate()
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

        homeViewModel.testimonials.observe(viewLifecycleOwner, Observer {
            adapterTestimonials.submitList(it.data.take(4).toMutableList())
            //mutableListOf(HomeTestimonials(2,"ed","https://d2gg9evh47fn9z.cloudfront.net/1600px_COLOURBOX32490000.jpg","sa"))
            adapterTestimonials.onItemClicked = { }
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            with(binding) {
                incSectionTestimonials.rvSliderTestimonials.setHasFixedSize(true)
                incSectionTestimonials.rvSliderTestimonials.layoutManager = layoutManager
                incSectionTestimonials.rvSliderTestimonials.adapter = adapterTestimonials
            }
        })


    }

}