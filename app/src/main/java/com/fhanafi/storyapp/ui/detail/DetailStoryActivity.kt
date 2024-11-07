package com.fhanafi.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fhanafi.storyapp.R
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.databinding.ActivityDetailBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailStoryViewModel: DetailStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail_story_title)
        }

        // Get data passed from previous activity (e.g., token and storyId)
        val token = intent.getStringExtra("token") ?: ""
        val storyId = intent.getStringExtra("storyId") ?: ""

        // Fetch story details using ViewModel
        detailStoryViewModel.getDetailStory(token, storyId)

        // Observe story details
        detailStoryViewModel.storyDetail.observe(this) { detailResponse ->
            val story = detailResponse.story
            if (story != null) {
                binding.tvDetailName.text = story.name
                binding.tvDetailDescription.text = story.description
                Glide.with(this).load(story.photoUrl).into(binding.ivDetailPhoto)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                @Suppress("DEPRECATION")
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
