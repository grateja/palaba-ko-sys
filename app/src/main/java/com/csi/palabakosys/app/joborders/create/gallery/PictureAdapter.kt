package com.csi.palabakosys.app.joborders.create.gallery

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.RecyclerItemJobOrderPictureBinding
import com.csi.palabakosys.room.entities.EntityJobOrderPictures
import com.csi.palabakosys.util.DataState
import getThumbnail
import java.io.File
import java.io.FileNotFoundException

class PictureAdapter(private val context: Context) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {
    private var list: List<EntityJobOrderPictures> = emptyList()
    inner class ViewHolder(private val binding: RecyclerItemJobOrderPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.imageViewJobOrderPicture
        fun bind(model: EntityJobOrderPictures) {
            binding.setVariable(BR.viewModel, model)
            try {
                val name = model.id.toString()
                val file = File(context.filesDir, "pictures/$name")
                val uri = FileProvider.getUriForFile(context, "com.csi.lms2020.fileprovider", file)

//                val bitmap = uri.getThumbnail(context, 100)

                val requestOptions = RequestOptions()
                    .override(100) // Set the size for the thumbnail
                    .centerCrop() // You can use other transformations like .fitCenter() based on your needs

                Glide.with(binding.root)
                    .load(uri)
                    .apply(requestOptions)
                    .into(imageView)

//                imageView.setImageBitmap(bitmap)

                return

//                context.contentResolver.loadThumbnail(uri, Size(100, 100), null)

//                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, model.id)

                val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(uri, Size(100, 100), null)
                } else {

                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context.contentResolver.query(uri, projection, null, null, null)

                    var bitmap: Bitmap? = null

                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            val imagePath = it.getString(columnIndex)
                            bitmap = BitmapFactory.decodeFile(imagePath)
                        }
                    }

                    cursor?.close()
                    bitmap
                }

                imageView.setImageBitmap(thumbnail)

            } catch (ioEx: FileNotFoundException) {
                model.fileDeleted = true
                println("File not found or deleted")
                ioEx.printStackTrace()
                ContextCompat.getDrawable(context, R.drawable.image_deleted)?.let {
                    imageView.setImageDrawable(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("general exception")
            }
        }
    }

    var onItemClick: ((EntityJobOrderPictures) -> Unit) ? = null

    fun setData(pictures: List<EntityJobOrderPictures>) {
        list = pictures
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemJobOrderPictureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
//            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, r.uriId)
//            context.contentResolver.delete(uri, null, null)
            onItemClick?.invoke(r)
        }
//        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, r.uriId)
//        val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            println("get thumbnail")
//            context.contentResolver.loadThumbnail(uri, Size(100, 100), null)
//        } else {
//
//            val projection = arrayOf(MediaStore.Images.Media.DATA)
//            val cursor = context.contentResolver.query(uri, projection, null, null, null)
//
//            var bitmap: Bitmap? = null
//
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                    val imagePath = it.getString(columnIndex)
//                    bitmap = BitmapFactory.decodeFile(imagePath)
//                }
//            }
//
//            cursor?.close()
//            bitmap
//        }
//        holder.imageView.setImageBitmap(thumbnail)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}