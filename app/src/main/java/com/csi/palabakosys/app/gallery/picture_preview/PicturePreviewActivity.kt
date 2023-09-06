package com.csi.palabakosys.app.gallery.picture_preview

import android.content.ContentUris
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityPicturePreviewBinding
import com.csi.palabakosys.util.showDeleteConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicturePreviewActivity : AppCompatActivity() {
    companion object {
        const val URI_IDS_EXTRA = "uriIds"
        const val URI_ID_EXTRA = "uriId"
    }

    private lateinit var binding: ActivityPicturePreviewBinding
    private val viewModel: PicturePreviewViewModel by viewModels()

    private lateinit var adapter: PhotoViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_preview)
        binding.lifecycleOwner = this

        adapter = PhotoViewAdapter(applicationContext)
        binding.mainContainer.adapter = adapter

        subscribeListeners()
        subscribeEvents()

        intent.getStringArrayExtra(URI_IDS_EXTRA)?.let {
            viewModel.setUriIds(it.toList())
        }
    }

    private fun subscribeEvents() {
        adapter.onDeleteInitiate = {
            showDeleteConfirmationDialog("You have unsaved picture", "Do you want to discard them?") {
                viewModel.deletePicture(it)
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.uriIds.observe(this, Observer {
            adapter.setData(it)
        })

        viewModel.dataState.observe(this, Observer {
            when(it) {
                is PicturePreviewViewModel.DataState.RemovePicture -> {
                    try {
//                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, it.uriId)
//                        contentResolver.delete(uri, null, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        adapter.removeItem(it.uriId)
                    }
                    viewModel.resetState()
                }
            }
        })
    }
}