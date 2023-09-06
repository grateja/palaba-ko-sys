package com.csi.palabakosys.app.joborders.create.gallery

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.gallery.picture_browser.PictureCaptureActivity
import com.csi.palabakosys.app.gallery.picture_preview.PicturePreviewActivity
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.databinding.FragmentJobOrderGalleryBinding
import com.csi.palabakosys.util.FragmentLauncher
import com.csi.palabakosys.util.showDeleteConfirmationDialog
import java.io.File
import java.io.FileOutputStream
import java.util.*

class JobOrderGalleryFragment : Fragment() {
    private val viewModel: CreateJobOrderViewModel by activityViewModels()
    private lateinit var binding: FragmentJobOrderGalleryBinding
    private lateinit var adapter: PictureAdapter

    private val cameraLauncher = FragmentLauncher(this)
    private val fileLauncher = FragmentLauncher(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentJobOrderGalleryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = PictureAdapter(requireContext())

        binding.recyclerJobOrderGallery.adapter = adapter

        viewModel.jobOrderPictures.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)

            println("total count")
            println(it.size)
        })

        subscribeEvents()
        subscribeListener()
        return binding.root
    }

    private fun subscribeListener() {
        viewModel.dataState().observe(viewLifecycleOwner, Observer {
            when(it) {
                is CreateJobOrderViewModel.DataState.OpenCamera -> {
                    openCamera()
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenPictures -> {
                    openPreview(it.ids, it.currentId)
                }
            }
        })
    }

    private fun subscribeEvents() {
        binding.buttonAddPicture.setOnClickListener {
            viewModel.openCamera()
        }

        binding.buttonBrowsePicture.setOnClickListener {
            browsePictures()
        }

        adapter.onItemClick = {
            if(it.fileDeleted) {
                requireContext().showDeleteConfirmationDialog("File deleted or corrupted", "Delete this file permanently?") {
                    viewModel.removePicture(it.id)
                }
            } else {
                viewModel.openPictures(it.id)
            }
        }

        cameraLauncher.onOk = {
//            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                it?.getStringExtra(PictureCaptureActivity.URI_EXTRA, Uri::class.java)
//            } else {
//                it?.getStringExtra(PictureCaptureActivity.URI_EXTRA)
//            }
            val uri = it?.getStringExtra(PictureCaptureActivity.URI_EXTRA)
            uri?.let {
                viewModel.attachPicture(UUID.fromString(uri))
            }
        }
        fileLauncher.onOk = {


            val imageUris = mutableListOf<Uri>()
            val names = mutableListOf<UUID>()

            it?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    imageUris.add(uri)
                    println("uri")
                    println(uri)
                }
            }

            println("image uri")
            println(it?.data)

            val mediaDir = File(context?.filesDir, "pictures")


            imageUris.forEach {
//                val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
//                    .format(System.currentTimeMillis()) + ".jpg"
                val name = UUID.randomUUID().toString()
                val targetFile = File(mediaDir, name)
                val inputStream = context?.contentResolver?.openInputStream(it)
                val outputStream = FileOutputStream(targetFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input?.copyTo(output)
                        names.add(UUID.fromString(name))
                    }
                }
            }


//            it?.data?.let { uri ->
//                imageUris.add(uri)
//            }
            println("Image uris")
            println(imageUris)

            viewModel.attachPictures(names)
        }
    }

    private fun openCamera() {
        val intent = Intent(context, PictureCaptureActivity::class.java)
        cameraLauncher.launch(intent)
    }

    private fun browsePictures() {
//        val intent = Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
//        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            Intent(MediaStore.ACTION_PICK_IMAGES).apply {
//                type = "image/*"
//                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            }
//        } else {
//            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
//                type = "image/*"
//                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            }
//        }
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        fileLauncher.launch(intent)
    }

    private fun openPreview(uriIds: List<String>, uriId: String) {
        val intent = Intent(context, PicturePreviewActivity::class.java).apply {
            putExtra(PicturePreviewActivity.URI_IDS_EXTRA, uriIds.toTypedArray())
            putExtra(PicturePreviewActivity.URI_ID_EXTRA, uriId)
        }
        startActivity(intent)
    }
}