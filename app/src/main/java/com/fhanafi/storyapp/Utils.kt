package com.fhanafi.mycamera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val MAXIMAL_SIZE = 1000000

private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun getTempImageUri(context: Context): Uri {
    val tempFile = File(context.getExternalFilesDir(null), "temp_image_$timeStamp.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val tempFile = File(context.externalCacheDir, "temp_image_$timeStamp.jpg")
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            val buffer = ByteArray(1024)
            var length: Int
            while (input.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
        }
    }
    return tempFile
}

fun File.reduceFileImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path).getRotatedBitmap(this)
    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)

    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
    return this
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val exif = ExifInterface(file)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270f)
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
