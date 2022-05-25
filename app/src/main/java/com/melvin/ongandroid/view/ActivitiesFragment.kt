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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                    // Hide Progress bar
                    enableUI(true)
                    // If data is obtained, it is loaded into the recycler adapter
                    activitiesAdapter.submitList(result.data)
                    // Update adapter information
                    binding.sectionActivities.rvActivities.adapter = activitiesAdapter
                }
                is Resource.Loading -> {
                    // Show Progress bar
                    enableUI(false)
                }
                is Resource.ErrorThrowable -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Show Dialog with error message when an error occurs
                    showDialog(
                        title = getString(R.string.dialog_error),
                        message = getString(R.string.dialog_error_getting_info),
                        positive = getString(R.string.btn_retry),
                        callback = { activitiesViewModel.fetchActivities() }
                    )

                }
                is Resource.ErrorApi -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Show Dialog with error message when an error occurs
                    showDialog(
                        title = getString(R.string.dialog_error),
                        message = getString(R.string.dialog_error_getting_info),
                        positive = getString(R.string.btn_retry),
                        callback = { activitiesViewModel.fetchActivities() }
                    )

                }
            }
        }
    }


    /**
     * Enable UI when loading data is finished
     * created on 01 May 2022 by Leonel Gomez
     *
     * @param enable
     */
    private fun enableUI(enable: Boolean) {
        binding.progressLoader.root.visibility = if (enable) View.GONE else View.VISIBLE
    }

    /**
     * Show dialog
     * created on 2 may 2022 by Jonathan Rodriguez
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

}