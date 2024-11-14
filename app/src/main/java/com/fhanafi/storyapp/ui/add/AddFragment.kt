package com.fhanafi.storyapp.ui.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fhanafi.mycamera.getTempImageUri
import com.fhanafi.storyapp.ViewModelFactory
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.databinding.FragmentAddBinding
import com.fhanafi.storyapp.ui.home.HomeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val addViewModel: AddViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    private var currentImageUri: Uri? = null

    private val requestCameraPermissionLauncher  =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show()
                uploadImage() // Proceed with image upload if permission granted
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
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
        } else{
            currentImageUri = null
            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
            Log.d("AddFragment", "Camera capture failed")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!hasCameraPermission()) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { handleUploadClick() }

        addViewModel.isLoading.observe(viewLifecycleOwner, Observer { showLoading(it) })
        addViewModel.uploadResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Toast.makeText(requireContext(), "Upload successful: ${it.message}", Toast.LENGTH_SHORT).show()
                homeViewModel.refreshStories()
                parentFragmentManager.popBackStack()
            }
        }

        return root
    }

    // Check for camera permission
    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    // Check for location permission
    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    // Handle upload button click, requesting location permission if needed
    private fun handleUploadClick() {
        if (hasLocationPermission()) {
            uploadImage()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

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
                val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var location: Location? = null
                try {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } catch (e: SecurityException) {
                    Log.e("AddFragment", "Location permission not granted", e)
                }
                val latitude = location?.latitude ?: 0.0
                val longitude = location?.longitude ?: 0.0

                lifecycleScope.launch {
                    val userPreference = UserPreference.getInstance(requireContext().dataStore)
                    val userSession = userPreference.getSession().first()
                    if (userSession.token.isNotEmpty()) {
                        addViewModel.uploadStory(uri, description, latitude, longitude, requireContext())
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
