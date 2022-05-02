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
import com.melvin.ongandroid.databinding.FragmentAboutUsBinding
import com.melvin.ongandroid.model.MemberUI
import com.melvin.ongandroid.view.adapters.MemberItemAdapter
import com.melvin.ongandroid.viewmodel.AboutUsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentAboutUsBinding
    private val aboutUsViewModel: AboutUsViewModel by activityViewModels()

    // Recycler View adapter
    private val memberAdapter = MemberItemAdapter()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)

        // Change toolbar title
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.nosotros)

        // Configuration of recycler view
        setupRecyclerViewMembers()

        return binding.root
    }

    /**
     * Setup recycler view Members
     * created on 30 April 2022 by Leonel Gomez
     */
    private fun setupRecyclerViewMembers() {
        // Show Dialog with member details
        memberAdapter.onItemClicked = { showMemberDetails(it) }
        // Choose to show 3 or 2 columns depending on whether the screen was rotated
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 3)
            } else {
                GridLayoutManager(context, 2)
            }

        // Initial configuration of recycler view
        with(binding) {
            sectionAboutUs.rvAboutUs.setHasFixedSize(true)
            sectionAboutUs.rvAboutUs.layoutManager = layoutManager
            sectionAboutUs.rvAboutUs.adapter = memberAdapter
        }

        // The list is populated from a repository asynchronously and updated in live data
        aboutUsViewModel.memberList.observe(viewLifecycleOwner) { result ->
            if (!result.isNullOrEmpty()) {

                //If data is obtained, it is loaded into the recycler adapter
                memberAdapter.submitList(result)
                binding.sectionAboutUs.rvAboutUs.adapter = memberAdapter

            }
        }
    }

    /**
     * Show Member Details
     *
     * @param member is a object with data of the person of About Us
     */
    private fun showMemberDetails(member: MemberUI) {
        // Update title of the section
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.details)

        // Show dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle(member.name)
        dialog.setMessage(member.description)
        dialog.setOnCancelListener {
            // Update title of the previous section
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = getString(R.string.nosotros)
        }
        dialog.show()
    }
}