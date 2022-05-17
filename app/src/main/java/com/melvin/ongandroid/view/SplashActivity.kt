package com.melvin.ongandroid.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.ActivitySplashBinding
import com.melvin.ongandroid.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    // Properties
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the toolbar
        supportActionBar?.hide()

        // Add animation on the splash screen
        setAnimation()

        // Coroutine to start Activity specified and finish SplashActivity
        CoroutineScope(Dispatchers.Main).launch {
            setObservers()
            finish()
        }

    }

    // Set animation on the splash screen
    private fun setAnimation() {
        val animationToUp = AnimationUtils.loadAnimation(this, R.anim.move_to_down)
        val animationZoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        binding.splashImage.startAnimation(animationToUp)
        binding.circleBackground.startAnimation(animationZoom)
    }

    // Set observers for view model
    private fun setObservers() {
        /** Observe the userLoggedIn LiveData in the ViewModel. If the user is logged in, start the MainActivity.
         * Else, start the LoginActivity.
         */
        splashViewModel.userLoggedIn.observe(this){
            if (it == true){
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LogInActivity::class.java))
            }
        }
    }
}