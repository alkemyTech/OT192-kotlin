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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.model.toUI
import com.melvin.ongandroid.services.firebase.FirebaseEvent
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


        /* Observe changes in the errorTestimonials. If there is an error, display a Snackbar
        * indicating a problem with the query*/
        homeViewModel.errorTestimonials.observe(viewLifecycleOwner) { error ->
            if (error != "") {
                // This line, logs the "testimonies_retrieve_error" event when the query to the
                // "api/testimonials" endpoint fails.
                FirebaseEvent.setEvent(requireContext(),"testimonies_retrieve_error")
                showDialog(
                    title = getString(R.string.dialog_error),
                    message = getString(R.string.dialog_error_getting_info),
                    positive = getString(R.string.btn_retry),
                    callback = { homeViewModel.getTestimonials() }
                )
            }
        }

        /* Observe changes in the slideError. If there is an error, display a dialog
        * with a retry button */
        homeViewModel.slideError.observe(viewLifecycleOwner) { error ->
            if (error != "") {
                // This line, logs the "slider_retrieve_error" event when the query to the
                // "api/slides" endpoint fails.
                FirebaseEvent.setEvent(requireContext(),"slider_retrieve_error")
                showDialog(
                    title = getString(R.string.dialog_error),
                    message = getString(R.string.dialog_error_getting_info),
                    positive = getString(R.string.btn_retry),
                    callback = { homeViewModel.fetchSlides() }
                )
            }
        }

        /* Observe if any of the three search is still in loading state
        * The spinner is hidden when all three sections (welcome, latest news, and testimonials)
        * have finished fetching their data. */
        homeViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if(loading != null)
                enableUI(!loading)
        }


        //Configuration of Welcome section
        binding.incSectionWelcome.tvTitleWelcome.text =
            getString(R.string.fragment_home_title_welcome)
        setupRecyclerViewSliderWelcome()

        setUpRecyclerViewNews()

        //Configuration of Testimonials section
        binding.incSectionTestimonials.tvTitleTestimonials.apply {
            text = getString(R.string.fragment_home_title_testimonials)
            gone()
        }
        setupRecyclerViewSilderTestimonials()

        homeViewModel.massiveFailure.observe(viewLifecycleOwner) {
            checkMassiveFailure(it)
        }

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
                // This line, logs the "slider_retrieve_success" event when the query to the
                // "api/slides" endpoint is successful.
                FirebaseEvent.setEvent(requireContext(),"slider_retrieve_success")
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
     * Else -> Make Invisible RecylerView
     */

    private fun setUpRecyclerViewNews() {
        recyclerViewNovedades = binding.recyclerNovedadesHome

        recyclerViewNovedades.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewNovedades.adapter = adapter

        homeViewModel.newsState.observe(viewLifecycleOwner) { estadoNoticias ->
            binding.apply {
                when (estadoNoticias) {
                    is Resource.Success -> {
                        // This line, logs the "last_news_retrieve_success" event when the query to the
                        // "api/news" endpoint is successful.
                        FirebaseEvent.setEvent(requireContext(),"last_news_retrieve_success")
                        adapter.submitList(estadoNoticias.data!!.novedades.take(5))

                        adapter.onClickArrow = {
                            // This line, logs the "last_news_see_more_pressed" event when the arrow to see more news is clicked
                            FirebaseEvent.setEvent(requireContext(), "last_news_see_more_pressed")
                            //Navigation to News Fragment
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, NewsFragment())
                                .commit()
                        }
                        textViewNovedadesHome.visible()
                        recyclerNovedadesHome.visible()
                    }
                    else -> {
                        // This line, logs the "last_news_retrieve_error" event when the query to the
                        // "api/news" endpoint fails.
                        FirebaseEvent.setEvent(requireContext(),"last_news_retrieve_error")
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

        // Set the adapter
        binding.incSectionTestimonials.rvSliderTestimonials.adapter = adapterTestimonials

        homeViewModel.testimonials.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (!response.data.isNullOrEmpty()) {
                    true -> {
                        //Show Testimonials title when response is OK
                        incSectionTestimonials.tvTitleTestimonials.visible()

                        // This line, logs the "testimonios_retrieve_success" event when the query to the
                        // "api/testimonials" endpoint is successful.
                        FirebaseEvent.setEvent(requireContext(), "testimonios_retrieve_success")
                        adapterTestimonials.submitList(response.data.take(5).toMutableList())

                        // This line, logs the "testimonies_see_more_pressed" event when the arrow to see more testimonials is clicked
                        adapterTestimonials.onClickArrow = {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, TestimonialFragment())
                                .commit()
                            FirebaseEvent.setEvent(requireContext(), "testimonies_see_more_pressed")
                        }
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

    /*
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

    /**
     * Enable UI when loading data is finished
     * created on 25 April 2022 by Leonel Gomez
     *
     * @param enable
     */
    private fun enableUI(enable : Boolean) {
        if(enable)
            binding.progressLoader.root.visibility = View.GONE
        else
            binding.progressLoader.root.visibility = View.VISIBLE
    }


    /**
     * Show dialog
     * created on 25 April 2022 by Leonel Gomez
     *
     * @param title string title text
     * @param message string message text
     * @param negative string text in the negative button, default null (not showed)
     * @param positive string text in the positive button, default null (not showed)
     * @param callback function that is called when positive button is clicked, default null (no action)
     */
    private fun showDialog(
        title: String,
        message: String,
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

    /*
     * When all api calls are unsuccesfull hide current view, and sow new view
     * with Error Button to retry ApiCalls
     */

    private fun checkMassiveFailure(massiveFailure: Boolean = false) {
        binding.apply {
            when (massiveFailure) {
                true -> {
                    constraintLayoutError.visibility = View.VISIBLE
                    scvHome.visibility = View.GONE

                    buttonError.setOnClickListener {
                        homeViewModel.retryApiCallsHome()
                    }
                }
                false -> {
                    constraintLayoutError.visibility = View.GONE
                    scvHome.visibility = View.VISIBLE

                }
            }
        }
    }

}
