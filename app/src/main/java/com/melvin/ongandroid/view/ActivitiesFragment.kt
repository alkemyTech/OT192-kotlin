package com.melvin.ongandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentActivitiesBinding
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.view.adapters.ActivitiesItemAdapter
import com.melvin.ongandroid.viewmodel.ActivitiesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivitiesFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentActivitiesBinding
    private val activitiesViewModel: ActivitiesViewModel by activityViewModels()

    // Recycler View adapter
    private val activitiesAdapter = ActivitiesItemAdapter()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivitiesBinding.inflate(layoutInflater, container, false)

        // Change toolbar title
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.actividades)

        // Configuration of recycler view
        setupRecyclerViewActivities()

        return binding.root
    }

    /**
     * Setup recycler view Activities
     * created on 1 May 2022 by Leonel Gomez
     */
    private fun setupRecyclerViewActivities() {
        activitiesAdapter.onItemClicked = { }
        // Choose to show some columns depending on whether the screen was rotated
        val layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        )

        // Initial configuration of recycler view
        with(binding) {
            sectionActivities.rvActivities.setHasFixedSize(true)
            sectionActivities.rvActivities.layoutManager = layoutManager
            sectionActivities.rvActivities.adapter = activitiesAdapter
        }

        // The list is populated from a repository asynchronously and updated in live data
        activitiesViewModel.activitiesState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    // If data is obtained, it is loaded into the recycler adapter
                    activitiesAdapter.submitList(result.data)
                    // Update adapter information
                    binding.sectionActivities.rvActivities.adapter = activitiesAdapter
                }
                else -> {

                }
            }
        }
    }
}