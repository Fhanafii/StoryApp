package com.fhanafi.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.storyapp.R
=======
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.databinding.FragmentHomeBinding
import com.fhanafi.storyapp.ui.welcome.WelcomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

<<<<<<< HEAD
    // Initialize HomeViewModel using by viewModels() with a custom factory
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var storyAdapter: StoryAdapter
=======
    private lateinit var homeViewModel: HomeViewModel
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

<<<<<<< HEAD
        // Enable options menu in fragment
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the session to get the token and then fetch stories
        homeViewModel.getSession().observe(viewLifecycleOwner) { userSession ->
            userSession?.let {
                observeStories(it.token)
            }
        }
=======
        // Initialize HomeViewModel with ViewModelFactory
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)


        // Setup logout button action
        setupAction()
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328

        return binding.root
    }

<<<<<<< HEAD
    private fun observeStories(token: String) {
        homeViewModel.getStories(token).observe(viewLifecycleOwner) { stories ->
            storyAdapter = StoryAdapter(stories)
            binding.recyclerView.adapter = storyAdapter
        }

        // Observe for any error messages
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                homeViewModel.clearError()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Perform logout action here
                homeViewModel.logout()

                // Navigate to WelcomeActivity after logout
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish() // Close the current activity
                true
            }
            else -> super.onOptionsItemSelected(item)
=======
    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            homeViewModel.logout()
            // Navigate back to WelcomeActivity
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish() // Close the current activity
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
