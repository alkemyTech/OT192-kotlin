package com.melvin.ongandroid.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.res.Configuration
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentAboutUsBinding
import com.melvin.ongandroid.utils.convertHtmlToString
import com.melvin.ongandroid.model.Members
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.gone
import com.melvin.ongandroid.utils.visible
import com.melvin.ongandroid.view.adapters.MemberListAdapter
import com.melvin.ongandroid.viewmodel.AboutUsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentAboutUsBinding
    private val aboutUsViewModel: AboutUsViewModel by activityViewModels()

    // Recycler View adapter
    private val memberListAdapter:MemberListAdapter by lazy{ MemberListAdapter() }

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
        initRecyclerView()

        return binding.root
    }

    /**
     * Initializes RecyclerView For Members.
     * Sets Grid Layout According Orientation.
     * Then, handles The State of the response showing or hiding elements.
     * When:
     * [Resource.Loading] -> Default behaviour. Hides Sections Abouts Us and shows progress Loader.
     * [Resource.Success] -> Hides progress Loader, submit list to adapter and shows RecyclerView.
     * [Resource.ErrorThrowable] -> Will be implemented in # 47
     * [Resource.ErrorApi] ->  Will be implemented in # 47
     */
    private fun initRecyclerView(){
        // Show Dialog with member details
        memberListAdapter.onItemClicked = { showMemberDetails(it) }
        // Choose to show 3 or 2 columns depending on whether the screen was rotated
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 3)
            } else {
                GridLayoutManager(context, 2)
            }

        with(binding) {
            sectionAboutUs.rvAboutUs.setHasFixedSize(true)
            sectionAboutUs.rvAboutUs.layoutManager = layoutManager
            sectionAboutUs.rvAboutUs.adapter = memberListAdapter

            aboutUsViewModel.membersState.observe(viewLifecycleOwner){ resourceMembers->
                when(resourceMembers){
                    is Resource.Loading ->{
                        scvAboutUs.gone()
                        progressLoader.root.visible()
                    }

                    is Resource.Success ->{
                        scvAboutUs.visible()
                        progressLoader.root.gone()
                        memberListAdapter.submitList(resourceMembers.data?.data?.toMutableList())
                    }

                    is Resource.ErrorApi ->{
                        // Show Dialog with error message when the API returns an error
                        showDialog(
                            title = getString(R.string.dialog_error),
                            message = getString(R.string.dialog_error_getting_info),
                            positive = getString(R.string.btn_retry),
                            callback = { aboutUsViewModel.fetchMembers() }
                        )
                    }

                    is Resource.ErrorThrowable ->{
                        // Show Dialog with error message when an error occurs
                        showDialog(
                            title = getString(R.string.dialog_error),
                            message = getString(R.string.dialog_error_getting_info),
                            positive = getString(R.string.btn_retry),
                            callback = { aboutUsViewModel.fetchMembers() }
                        )
                    }
                }
            }
        }
    }

    /**
     * Show Member Details
     *
     * @param member is a object with data of the person of About Us
     */
    @SuppressLint("InflateParams")
    private fun showMemberDetails(member: Members) {
        // Update title of the section
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.details)

        // Show dialog with member details
        val dialog = Dialog(requireContext())
        dialog.setContentView(layoutInflater.inflate(R.layout.dialog_member_detail, null))
        dialog.onBackPressed()
        val image = dialog.findViewById(R.id.member_detail_image) as ImageView
        image.load(member.image)
        val name = dialog.findViewById(R.id.member_detail_name) as TextView
        name.text = member.name
        val description = dialog.findViewById(R.id.member_detail_description) as TextView
        description.text = member.description.convertHtmlToString().trim()
        val linkFacebook = dialog.findViewById(R.id.member_detail_facebook) as TextView
        linkFacebook.text = member.facebookUrl
        val linkLinkedin = dialog.findViewById(R.id.member_detail_linkedin) as TextView
        linkLinkedin.text = member.linkedinUrl
        dialog.setOnCancelListener{
            // Update title of the previous section
            actionBar?.title = getString(R.string.nosotros)
        }
        dialog.show()
    }

    /**
     * Show dialog
     * created on 1 may 2022 by Jonathan Rodriguez
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