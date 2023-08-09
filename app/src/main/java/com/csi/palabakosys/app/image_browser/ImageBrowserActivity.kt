package com.csi.palabakosys.app.image_browser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityImageBrowserBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.saveToGallery
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class ImageBrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBrowserBinding
    private val activityLauncher = ActivityLauncher(this)

    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_browser)
        subscribeEvents()

    }


    private fun subscribeEvents() {
        binding.buttonCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            } else {
                openCamera()
            }
        }
        activityLauncher.onOk = {
            val imageBitmap = getFullResolutionBitmapFromUri(photoUri) as? Bitmap
//            binding.imagePreview.setImageBitmap(imageBitmap)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageBitmap?.saveToGallery(applicationContext, "test.png")
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission is granted, call openCamera()
                    openCamera()
                } else {
                    // Camera permission is denied, handle it as needed
                    // For example, show a message or take alternative action
                }
                return
            }
        }
    }



    private lateinit var photoUri: Uri
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Handle error if file creation fails
            null
        }
        photoFile?.also {
            photoUri = FileProvider.getUriForFile(this, "com.csi.lms2020.fileprovider", it)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        activityLauncher.launch(cameraIntent)
//        if (cameraIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(cameraIntent, 1)
//        } else {
//            Toast.makeText(this, "No camera app available.", Toast.LENGTH_SHORT).show()
//        }
    }

//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val imageFileName = "JPEG_${timeStamp}_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file = File(storageDir, "$imageFileName.jpg")
//
//        // Save the file
//        file.createNewFile()
//
//        return file
//    }

    lateinit var currentPhotoPath: String
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun getFullResolutionBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val imageId = uri.lastPathSegment ?: ""
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val selection = "${MediaStore.Images.Media._ID} = ?"
            val selectionArgs = arrayOf(imageId)
            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            var imagePath: String? = null
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                imagePath = cursor.getString(columnIndex)
            }
            cursor?.close()

            if (imagePath != null) {
                BitmapFactory.decodeFile(imagePath)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}