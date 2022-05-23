package com.melvin.ongandroid.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.melvin.ongandroid.databinding.FragmentNewsBinding
import com.melvin.ongandroid.model.News
import com.melvin.ongandroid.view.adapters.NewsItemAdapter
import androidx.appcompat.app.AppCompatActivity
import com.melvin.ongandroid.R
import com.melvin.ongandroid.utils.Resource
import com.melvin.ongandroid.utils.gone
import com.melvin.ongandroid.utils.visible
import com.melvin.ongandroid.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {


    // Properties
    private lateinit var binding: FragmentNewsBinding
    private val newsViewModel: NewsViewModel by activityViewModels()
    private val newsAdapter = NewsItemAdapter()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        //News title in top bar
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.novedades)

        //setupRecyclerView()
        initNewsRecyclerview()

        return binding.root
    }

    /**
     * Observes state in VM.
     * when:
     * [Resource.Success] -> Disable Spinner Loading, shows Recycler, submit list from API.
     * [Resource.Loading] -> Disable Recycler and Title, shows Spinner.
     * [Resource.ErrorApi] and [Resource.ErrorThrowable] -> Shows layout for errors. And has a button
     * listener that retry api request for news. If [Resource.Success], the same as before.
     */

    private fun initNewsRecyclerview() {
        // Choose to show 2 or 1 columns depending on whether the screen was rotated - 2022-05-22 L.Gomez
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2)
            } else {
                GridLayoutManager(context, 1)
            }

        newsViewModel.newsState.observe(viewLifecycleOwner) { resourceNews ->

            when (resourceNews) {
                is Resource.Success -> {
                    binding.apply {
                        rvNews.adapter = newsAdapter
                        rvNews.layoutManager = layoutManager
                        newsAdapter.submitList(resourceNews.data?.novedades?.toMutableList())
                        rvNews.visible()
                        progressLoader.root.gone()

                    }
                }
                is Resource.Loading -> {
                    binding.apply {
                        rvNews.gone()
                        progressLoader.root.visible()
                    }
                }

                is Resource.ErrorThrowable ->{
                    binding.apply {
                        progressLoader.root.gone()
                        fragmentNewsTitle.gone()
                        constraintError.root.visible()
                        binding.constraintError.buttonErrorRetry.setOnClickListener {
                            progressLoader.root.visible()
                            newsViewModel.fetchLatestNews()
                            constraintError.root.gone()
                        }
                    }

                }

                is Resource.ErrorApi ->{
                    binding.apply {
                        progressLoader.root.gone()
                        fragmentNewsTitle.gone()
                        constraintError.root.visible()
                        binding.constraintError.buttonErrorRetry.setOnClickListener {
                            progressLoader.root.visible()
                            newsViewModel.fetchLatestNews()
                            constraintError.root.gone()
                        }
                    }
                }
            }
        }
    }

    // Setup RecyclerView and Adapter for News
    private fun setupRecyclerView() {
        // Harcoded random list
        val listNews = MutableList(10) {
            News(
                image = "https://image.tmdb.org/t/p/w500_and_h282_face/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                name = "Name ${(1..10).random()}\n",
                content = "Descripción ${(1..10).random()}"
            )
        }

        binding.rvNews.layoutManager = GridLayoutManager(context, 2)

        newsAdapter.submitList(listNews)
        binding.rvNews.adapter = newsAdapter
    }

}