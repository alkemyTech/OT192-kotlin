package com.melvin.ongandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentHomeBinding
import com.melvin.ongandroid.model.HomeWelcome
import com.melvin.ongandroid.view.adapter.HomeWelcomeItemAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapterWelcome = HomeWelcomeItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.inicio)

        binding.textView.text = getString(R.string.inicio)

        //Configuration of Welcome section
        binding.incSectionWelcome.tvTitleWelcome.text = getString(R.string.fragment_home_title_welcome)
        setupRecyclerViewSliderWelcome()

        return binding.root
    }

    /**
     * Setup recycler view slider welcome
     */
    private fun setupRecyclerViewSliderWelcome() {
        //TODO: Harcoded list
        val listHomeWelcome = MutableList(15) {
            HomeWelcome(
                title = "Actividad ${(1..100).random()}",
                imgUrl = "https://picsum.photos/${(1..100).random()}",
                description = "Descripción ${(1..100).random()} \n" +
                        "Descripción ${(1..100).random()} \n" +
                        "Descripción ${(1..100).random()} \n" +
                        "Descripción ${(1..100).random()} \n"
            )
        }

        adapterWelcome.setData(listHomeWelcome)
        adapterWelcome.onItemClicked = { }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        with(binding) {
            incSectionWelcome.rvSliderWelcome.setHasFixedSize(true)
            incSectionWelcome.rvSliderWelcome.layoutManager = layoutManager
            incSectionWelcome.rvSliderWelcome.adapter = adapterWelcome
        }
    }

}