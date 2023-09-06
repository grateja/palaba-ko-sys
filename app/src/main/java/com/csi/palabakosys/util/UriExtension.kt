import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import java.io.InputStream

fun Uri.getThumbnail(context: Context, size: Int): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this)
        val options = BitmapFactory.Options()
        options.inSampleSize = calculateInSampleSize(inputStream, size)

        // Decode the bitmap with the specified options
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)

        // Rotate the bitmap if needed based on Exif orientation
//        val orientation = getExifOrientation(context)
//        val rotatedBitmap = rotateBitmap(bitmap, orientation)

        inputStream?.close()

        bitmap
//        rotatedBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun calculateInSampleSize(inputStream: InputStream?, reqSize: Int): Int {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    // Decode image dimensions without loading the whole image
    BitmapFactory.decodeStream(inputStream, null, options)

    // Calculate the inSampleSize to scale down the image to the desired size
    var inSampleSize = 1
    val width = options.outWidth
    if (width > reqSize) {
        val halfWidth = width / 2
        while ((halfWidth / inSampleSize) >= reqSize) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

//private fun Uri.getExifOrientation(context: Context): Int {
//    val exif = androidx.media.ExifInterface(this.toFile())
//    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//
//    // Convert Exif orientation to degrees
//    return when (orientation) {
//        ExifInterface.ORIENTATION_ROTATE_90 -> 90
//        ExifInterface.ORIENTATION_ROTATE_180 -> 180
//        ExifInterface.ORIENTATION_ROTATE_270 -> 270
//        else -> 0
//    }
//}

private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
