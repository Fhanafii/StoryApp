package com.fhanafi.storyapp.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.fhanafi.storyapp.R
import com.fhanafi.storyapp.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private val detailStoryViewModel: DetailStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val imageViewProfile = findViewById<ImageView>(R.id.iv_detail_photo)
        val textViewName = findViewById<TextView>(R.id.tv_detail_name)
        val textViewDescription = findViewById<TextView>(R.id.tv_detail_description)
        // Get data passed from previous activity (e.g., token and storyId)
        val token = intent.getStringExtra("token") ?: ""
        val storyId = intent.getStringExtra("storyId") ?: ""

        // Fetch story details using ViewModel
        detailStoryViewModel.getDetailStory(token, storyId)

        // Observe story details
        detailStoryViewModel.storyDetail.observe(this) { detailResponse ->
            val story = detailResponse.story
            if (story != null) {
                textViewName.text = story.name
                textViewDescription.text = story.description
                Glide.with(this).load(story.photoUrl).into(imageViewProfile)
            } else {
                Toast.makeText(this, "Story details not available", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe for any error messages
        detailStoryViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                detailStoryViewModel.clearError()
            }
        }
    }
}