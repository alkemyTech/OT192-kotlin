package com.melvin.ongandroid.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.melvin.ongandroid.databinding.ActivityLogInBinding
import com.melvin.ongandroid.viewmodel.login.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private val logInViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityLogInBinding.inflate(layoutInflater)

        setContentView(binding.root)


    }

    /**
     * Override on Activiy for result to make an Intent for Log in with Google.
     * Has an argument [data] with the intent needed for a task.
     * This will be sent to the [LogInViewModel] to handle the response from fireBase.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 1){
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
            logInViewModel.finishLoginGoogle(task)
        }
    }
}