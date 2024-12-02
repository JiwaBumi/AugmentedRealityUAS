package com.uas.augmentedrealityproject.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun launchUnityAR(context: Context) {
    val unityApk = File(context.filesDir, "arMobile.apk")

    // Copy the APK from assets to internal storage if it doesn't exist
    if (!unityApk.exists()) {
        try {
            val assetManager = context.assets
            assetManager.open("arMobile.apk").use { inputStream ->
                FileOutputStream(unityApk).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return // Handle error gracefully
        }
    }

    // Use a FileProvider for compatibility with newer Android versions
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Add provider name in AndroidManifest
        unityApk
    )

    // Intent to install and launch the APK
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
}
