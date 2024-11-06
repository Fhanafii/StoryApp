package com.fhanafi.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.storyapp.R
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.databinding.FragmentHomeBinding
import com.fhanafi.storyapp.ui.welcome.WelcomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Initialize HomeViewModel using by viewModels() with a custom factory
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    private fun observeStories(token: String) {
        homeViewModel.getStories(token).observe(viewLifecycleOwner) { stories ->
            storyAdapter = StoryAdapter(requireContext(), token, stories)
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
