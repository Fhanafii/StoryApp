package com.fhanafi.storyapp.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fhanafi.storyapp.MainActivity
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.databinding.ActivityWelcomeBinding
import com.fhanafi.storyapp.ui.login.LoginActivity
import com.fhanafi.storyapp.ui.register.RegisterActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        //adding the android:alpha="0" in activity_welcome if you did not set this up the animation also didnt work
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500) // set duration to 500 if the animation didnt show up
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            showLoading(true)
            checkLoginStatus()
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkLoginStatus(){
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(dataStore)
            val userSession = userPreference.getSession().first()

            if (userSession.isLogin && userSession.token.isNotEmpty()){
                navigateToHome()
            } else{
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        showLoading(false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToHome() {
        showLoading(false)
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
        binding.signupButton.isEnabled = !isLoading
    }
}