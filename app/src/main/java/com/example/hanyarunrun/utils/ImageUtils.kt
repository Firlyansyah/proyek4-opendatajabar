package com.example.hanyarunrun.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream
import java.util.*

fun saveImageToInternalStorage(context: Context, uri: Uri?): String? {
    if (uri == null) return null

    val contentResolver: ContentResolver = context.contentResolver

    try {
        // Generate a unique file name
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val outputStream: OutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

        // Copy the input stream from the Uri to the output stream
        val inputStream: InputStream = contentResolver.openInputStream(uri)!!
        inputStream.copyTo(outputStream)

        // Return the path to the saved image
        return context.filesDir.absolutePath + "/" + fileName

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
