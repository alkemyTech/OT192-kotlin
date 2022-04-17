package com.melvin.ongandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.FragmentHomeBinding
import com.melvin.ongandroid.model.Novedades
import com.melvin.ongandroid.view.adapters.NovedadesAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

        return binding.root
    }


}