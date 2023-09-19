package com.csi.palabakosys.app.joborders.create.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.RecyclerItemJobOrderPictureBinding
import com.csi.palabakosys.room.entities.EntityJobOrderPictures
import com.csi.palabakosys.util.Constants.Companion.FILE_PROVIDER
import com.csi.palabakosys.util.Constants.Companion.PICTURES_DIR
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
                val file = File(context.filesDir, PICTURES_DIR + name)
                val uri = FileProvider.getUriForFile(context, FILE_PROVIDER, file)

                val requestOptions = RequestOptions()
                    .override(100)
                    .centerCrop()

                Glide.with(binding.root)
                    .load(uri)
                    .apply(requestOptions)
                    .into(imageView)
            } catch (ioEx: FileNotFoundException) {
                model.fileDeleted = true
                ioEx.printStackTrace()
                ContextCompat.getDrawable(context, R.drawable.image_deleted)?.let {
                    imageView.setImageDrawable(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
            onItemClick?.invoke(r)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}