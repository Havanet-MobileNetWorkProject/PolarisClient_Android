package com.example.myapplication2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onLocationReceived: (Location?) -> Unit) {
        if (!PermissionsUtils.hasAllPermissions(context)) {
            onLocationReceived(null)
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationReceived(location)
            }
            .addOnFailureListener {
                onLocationReceived(null)
            }
    }
}
