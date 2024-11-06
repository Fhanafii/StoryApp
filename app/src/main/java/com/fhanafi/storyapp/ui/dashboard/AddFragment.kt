package com.fhanafi.storyapp.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fhanafi.mycamera.getTempImageUri
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.databinding.FragmentAddBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val addViewModel: AddViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Set up button listeners
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        // Observe ViewModel LiveData
        addViewModel.isLoading.observe(viewLifecycleOwner, Observer { showLoading(it) })
        addViewModel.uploadResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Toast.makeText(requireContext(), "Upload successful: ${it.message}", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // Go back after successful upload
            }
        }

        return root
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getTempImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val description = binding.edAddDescription.text.toString()
            if (description.isNotEmpty()) {
                // Retrieve the token from DataStore using UserPreference
                lifecycleScope.launch {
                    val userPreference = UserPreference.getInstance(requireContext().dataStore)
                    val userSession = userPreference.getSession().first()
                    val token = userSession.token
                    if (token.isNotEmpty()) {
                        addViewModel.uploadStory(token, uri, description, requireContext())
                    } else {
                        Toast.makeText(requireContext(), "Failed to retrieve token", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Description is empty", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
