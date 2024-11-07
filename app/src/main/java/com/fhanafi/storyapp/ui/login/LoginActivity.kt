package com.fhanafi.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fhanafi.storyapp.MainActivity
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.databinding.ActivityLoginBinding
import com.fhanafi.storyapp.ui.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {
    private val LoginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        LoginViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginButton.isEnabled = !isLoading
        })
    }

    private fun playAnimation() {
        // ImageView Animation (translation left to right)
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Sequential fade-in animations for each element
        val titleAnim = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val messageAnim = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val emailLabelAnim = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputAnim = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passwordLabelAnim = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputAnim = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val loginButtonAnim = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)

        // Play animations sequentially
        AnimatorSet().apply {
            playSequentially(
                titleAnim,
                messageAnim,
                emailLabelAnim,
                emailInputAnim,
                passwordLabelAnim,
                passwordInputAnim,
                loginButtonAnim
            )
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
            Log.d("LoginActivity", "Login button clicked")
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            LoginViewModel.login(email, password) { isSuccess ->
                if (isSuccess) {
                    Log.d("LoginActivity", "Login successful")
                    // Note: The token is saved inside `LoginViewModel` when successful
                    AlertDialog.Builder(this).apply {
                        setTitle("Success!")
                        setMessage("Login successful. Welcome!")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    Log.d("LoginActivity", "Login failed")
                    AlertDialog.Builder(this).apply {
                        setTitle("Error")
                        setMessage("Login failed. Please check your email and password.")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
        // Navigate back to WelcomeActivity
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()  // Close LoginActivity
    }
}