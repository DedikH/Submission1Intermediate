package com.example.bismillahbisaintermediate.View.AddStory

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.Response.ResponseAddStory
import com.example.bismillahbisaintermediate.View.ListStory.ListStoryViewModel
import com.example.bismillahbisaintermediate.ViewModelFactory
import com.example.bismillahbisaintermediate.databinding.ActivityAddStoryBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStory : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var viewModellAddStory: ViewModellAddStory
    private lateinit var authRepository: UserRepository
    private var CurrentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = APIConfig.postLogin()
        val userPreferenceDataStore = UserPreferenceDataStore(this)
        authRepository = UserRepository(apiService, userPreferenceDataStore)
        val factory = ViewModelFactory(authRepository)
        viewModellAddStory = ViewModelProvider(this, factory).get(ViewModellAddStory::class.java)

        startGallery()
        onClickCam()
        uploadBTN()
        checkStoragePermissions()
    }


//    Permission
private fun checkStoragePermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }
}

//    Gallery Start
    private fun startGallery() {
        binding.ListGalleryButton.setOnClickListener{
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            showLoading(false)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }
//    Gallery End

//    Camera Start
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private fun onClickCam(){
        binding.ListCameraButton.setOnClickListener{
            startCamera()
        }
    }
//    Camera End

//    Upload Stori
    private fun UploadStories(){
    currentImageUri?.let { uri ->
        val imageFile = uriToFile(uri, this).reduceFileImage()
        val description = binding.ListAddDescription.text.toString()
        showLoading(true)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        lifecycleScope.launch {
            try {
                val token = authRepository.getAuthToken().first()
                val lat = CurrentLocation?.latitude
                val lon = CurrentLocation?.longitude
                if (token != null) {
                    viewModellAddStory.uploadStoryImage(token, multipartBody, requestBody)
                }
                viewModellAddStory.liveDataStory.observe(this@AddStory){
                    Toast.makeText(this@AddStory, "Add Story Succes", Toast.LENGTH_SHORT).show()
                }
            }catch (e: HttpException){
                AlertDialog.Builder(this@AddStory).apply {
                    setTitle("Upload Gagal")
                    setMessage("Upload")
                    setPositiveButton("Ya") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }
    }
}
//    End Story

    private fun uploadBTN(){
        binding.buttonUpload.setOnClickListener{
            UploadStories()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }
}