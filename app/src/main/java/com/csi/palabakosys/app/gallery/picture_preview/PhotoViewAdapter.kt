package com.csi.palabakosys.app.gallery.picture_preview

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csi.palabakosys.R
import com.csi.palabakosys.util.loadThumbnailOrBitmap
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import java.util.logging.Handler

class PhotoViewAdapter(private val context: Context): RecyclerView.Adapter<PhotoViewAdapter.ViewHolder>() {

    private var uriIds = emptyList<String>()

    var onDeleteInitiate: ((String) -> Unit)? = null

    fun setData(uriIds: List<String>) {
        this.uriIds = uriIds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_photo_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = uriIds[position]
        holder.bind(uri)
    }

    override fun getItemCount(): Int = uriIds.size

    fun removeItem(uriId: String) {
        val index = uriIds.indexOf(uriId)
        if (index >= 0) {
            val newList = uriIds.toMutableList()
            newList.removeAt(index)
            uriIds = newList
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoView: PhotoView = itemView.findViewById(R.id.photoView)
        private val buttonDelete: MaterialCardView = itemView.findViewById(R.id.button_delete)

        fun bind(uriId: String) {
            buttonDelete.setOnClickListener {
                onDeleteInitiate?.invoke(uriId)
            }
//            var inputStream: InputStream? = null
            try {

                CoroutineScope(Dispatchers.Main).launch {
                    val name = uriId.toString()
                    val file = File(context.filesDir, "pictures/$name")
                    val uri = FileProvider.getUriForFile(context, "com.csi.lms2020.fileprovider", file)
                    photoView.setImageURI(uri)
                }

//                val file = File(dir, "my_image.jpg")
//                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                photoView.setImageBitmap(bitmap)
//                return

//                if(uri == null) {
//                    uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, uriId)
////                    uri = Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/1000000023")
////                    val internalStorageDir = context?.cacheDir
//
//                    val targetFile = File(dir, "temp")
//
//                    uri?.let {
//                        context.contentResolver?.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        photoView.setImageURI(it)
////                        val outputStream = FileOutputStream(targetFile)
////
////                        inputStream.use { input ->
////                            outputStream.use { output ->
////                                input?.copyTo(output)
////                            }
////                        }
//                    }
//
////                    println("URI*")
////                    println(uri)
//                    val bitmap = BitmapFactory.decodeFile(targetFile.absolutePath)
//                    photoView.setImageBitmap(bitmap)
//                    return
//                }
//                return
//
//                uri?.let {
//                    val projection = arrayOf(MediaStore.Images.Media._ID)
//                    val cursor = context.contentResolver.query(it, projection, null, null, null)
//                    var bitmap: Bitmap? = null
//
//                    cursor?.use {
//                        if (it.moveToFirst()) {
//                            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//                            val imagePath = it.getString(columnIndex)
//                            bitmap = BitmapFactory.decodeFile(imagePath)
//
//                            val id = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//                            println("id from _ID")
//                            println(id)
//                        }
//                    }
//
//                    cursor?.close()
//                    photoView.setImageBitmap(bitmap)
//                }
//
////                uri?.let {
////                    inputStream = context.contentResolver.openInputStream(it)
////                }
//
//
////                uri?.let {
////                    val thumbnail = context.loadThumbnailOrBitmap(it, 100)
////                }
//
////                val bitmap = BitmapFactory.decodeStream(inputStream)
////                inputStream?.close()
////                CoroutineScope(Dispatchers.Main).launch {
////                    photoView.setImageURI(uri)
//////                    photoView.setImageBitmap(bitmap)
//////                    Glide.with(itemView)
//////                        .load(bitmap)
//////                        .into(photoView)
////                }

                // implement the code here
            } catch (e: Exception) {
                e.printStackTrace()

                ContextCompat.getDrawable(context, R.drawable.image_deleted)?.let {
                    Glide.with(itemView)
                        .load(it)
                        .into(photoView)
                }
            } finally {
//                inputStream?.close()
            }
        }
    }
}