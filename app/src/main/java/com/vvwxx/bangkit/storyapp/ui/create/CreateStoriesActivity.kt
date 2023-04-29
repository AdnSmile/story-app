package com.vvwxx.bangkit.storyapp.ui.create

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.vvwxx.bangkit.storyapp.databinding.ActivityCreateStoriesBinding
import com.vvwxx.bangkit.storyapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoriesBinding
    private lateinit var currentPhotoPath: String
    private lateinit var token: String
    private lateinit var finalFile: File

    private lateinit var factory: ViewModelFactory
    private val viewModel: CreateStoriesViewModel by viewModels { factory }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions( // melakukan permintaan izin (request permission)
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.getUser.observe(this) {
            token = it.token
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.message.observe(this) {
            showToast(it)
            if (it.equals("Uploading stories Story created successfully")) {
                finish()
            }
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnSend.setOnClickListener {
             uploadImage()
        }
    }

    private fun uploadImage() {

        val desc = binding.edDesc.text.toString()
        finalFile = reduceFileImage(finalFile)

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = finalFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            finalFile.name,
            requestImageFile
        )
        Log.d("CreateStories", desc)
        viewModel.uploadStories(imageMultipart, token, description)

    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.vvwxx.bangkit.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    // menampung hasil dari intent atau IntentResult
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            val result = rotatePhoto(myFile)
            finalFile = convertBitmapFile(result, myFile)
            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            currentPhotoPath = myFile.path

            val photo = rotatePhoto(myFile)
            finalFile = convertBitmapFile(photo, myFile)
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun rotatePhoto(myFile: File) : Bitmap {
        val exifInterface = ExifInterface(currentPhotoPath)
        val rotate: Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val photo = BitmapFactory.decodeFile(myFile.path)

        val result = when (rotate) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(photo, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(photo, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(photo, 270f)
            else -> photo
        }
        return result
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}