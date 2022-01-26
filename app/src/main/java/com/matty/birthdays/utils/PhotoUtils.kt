package com.matty.birthdays.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private const val PHOTO_WIDTH = 150.0
private const val PHOTO_HEIGHT = 150.0

fun compressPhoto(context: Context, uri: Uri, newUri: Uri = uri) {
    var bitmapOptions: BitmapFactory.Options
    context.contentResolver.openInputStream(uri).use {
        bitmapOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(it, null, this)

            inSampleSize = max(
                1,
                min(
                    outWidth / PHOTO_WIDTH,
                    outHeight / PHOTO_HEIGHT
                ).roundToInt()
            )

            inJustDecodeBounds = false
        }
    }

    var bitmap: Bitmap
    context.contentResolver.openInputStream(uri).use {
        bitmap = BitmapFactory.decodeStream(it, null, bitmapOptions)!!
    }

    context.openFileOutput(getFileName(newUri), Context.MODE_PRIVATE).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

fun createInternalPhotoUri(context: Context): Uri {
    val fileName = "IMG_${UUID.randomUUID()}.jpg"
    val file = File(context.applicationContext.filesDir, fileName)
    return FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file
    )
}

fun deletePhoto(context: Context, uri: Uri): Boolean {
    val file = File(uri.path!!)
    return context.deleteFile(file.name)
}

private fun getFileName(uri: Uri): String {
    val uriString = uri.toString()
    val startIndex = uriString.lastIndexOf('/') + 1
    return uriString.substring(startIndex)
}