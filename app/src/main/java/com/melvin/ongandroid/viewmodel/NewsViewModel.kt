package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.repository.OngRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {
    // TODO: Implement the ViewModel
}