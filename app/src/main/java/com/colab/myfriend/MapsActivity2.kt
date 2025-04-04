package com.colab.myfriend

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.helper.LocationHelper
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity2 : NoViewModelActivity<ActivityMapsBinding>(R.layout.activity_maps){

    @Inject
    lateinit var adrHelper : AddressHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mapView.onCreate(savedInstanceState)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        checkLocationPermission {
            listenLocationChange()
        }

        binding.mapView.getMapAsync{ googleMap ->

            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("LocationError", "Permission tidak diberikan!")
                return@getMapAsync
            }

            googleMap.isMyLocationEnabled = true

            val area = googleMap.addCircle(CircleOptions()
                .center(LatLng(-7.48258688,109.29791201))
                .radius(1_000.0)
                .strokeColor(Color.parseColor("#FFC80000"))
                .fillColor(Color.parseColor("#25C80000"))
            )

        }
    }

    private fun isInsideLocation(area: LatLng, position: LatLng): Boolean {
        val result = FloatArray(1)
        Location.distanceBetween(
            area.latitude, area.longitude,
            position.latitude, position.longitude,
            result
        )
        return result[0] < 1000 // 1000 meter = 1 km
    }


    @SuppressLint("SetTextI18n")
    override fun retrieveLocationChange(location: Location) {
        super.retrieveLocationChange(location)
        Log.d("deviceLocation", "latitude: ${location.latitude}, longitude: ${location.longitude}")

        binding.mapView.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

            val isInside = isInsideLocation(LatLng(-7.48258688, 109.29791201), LatLng(location.latitude, location.longitude))

            val status = if (isInside) "dalam" else "luar"

            Log.d("GeofenceStatus", "User berada di $status area")  // Tambahkan log ini untuk melihat hasilnya
            binding.tvStatus.text = "Kamu berada di $status area."
        }
    }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}