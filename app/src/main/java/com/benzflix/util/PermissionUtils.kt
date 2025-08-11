package com.benzflix.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    private const val STORAGE_PERMISSION_CODE = 100

    // Check if the storage permission is granted
    fun isStoragePermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Request storage permission
    fun requestStoragePermission(activity: Activity) {
        if (!isStoragePermissionGranted(activity)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    // Handle the result of the permission request
    fun handlePermissionResult(requestCode: Int, grantResults: IntArray, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted()
                } else {
                    onPermissionDenied()
                }
            }
        }
    }
}