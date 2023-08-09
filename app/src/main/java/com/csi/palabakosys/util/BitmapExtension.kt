package com.csi.palabakosys.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.Q)
fun Bitmap.saveToGallery(context: Context, fileName: String): Boolean {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    var imageOutStream: OutputStream? = null

    return try {
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        imageOutStream = resolver.openOutputStream(resolver.insert(contentUri, contentValues)!!)

        this.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream)
        imageOutStream?.close()

        true // Image saved successfully
    } catch (e: IOException) {
        e.printStackTrace()
        false // Image save failed
    } finally {
        imageOutStream?.close()
    }
}