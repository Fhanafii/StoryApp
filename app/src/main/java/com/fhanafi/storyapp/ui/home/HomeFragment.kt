package com.fhanafi.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.databinding.FragmentHomeBinding
import com.fhanafi.storyapp.ui.welcome.WelcomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize HomeViewModel with ViewModelFactory
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)


        // Setup logout button action
        setupAction()

        return binding.root
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            homeViewModel.logout()
            // Navigate back to WelcomeActivity
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish() // Close the current activity
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}