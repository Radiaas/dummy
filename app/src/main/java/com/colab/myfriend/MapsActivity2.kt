package com.colab.myfriend

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
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
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity2 : NoViewModelActivity<ActivityMapsBinding>(R.layout.activity_maps){

    @Inject
    lateinit var adrHelper : AddressHelper
    private val areaCenter = LatLng(-7.48258688, 109.29791201) // bisa juga dari server

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

        binding.mapView.getMapAsync { googleMap ->

            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@getMapAsync
            }

            googleMap.isMyLocationEnabled = true

            // Tambahkan Circle
            googleMap.addCircle(
                CircleOptions()
                    .center(areaCenter)
                    .radius(1000.0) // 1 km
                    .strokeColor(Color.parseColor("#FFC80000"))
                    .fillColor(Color.parseColor("#25C80000"))
            )

            // Batasi area agar user tidak bisa keluar dari circle
            val bounds = LatLngBounds.Builder()
                .include(LatLng(areaCenter.latitude + 0.006, areaCenter.longitude + 0.006)) // Kanan atas
                .include(LatLng(areaCenter.latitude - 0.006, areaCenter.longitude - 0.006)) // Kiri bawah
                .build()

            googleMap.setLatLngBoundsForCameraTarget(bounds) // Lock peta ke dalam area
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(areaCenter, 15f)) // Pusatkan kamera
        }

    }


    @SuppressLint("SetTextI18n")
    override fun retrieveLocationChange(location: Location) {
        super.retrieveLocationChange(location)
        val userLatLng = LatLng(location.latitude, location.longitude)

        binding.mapView.getMapAsync { googleMap ->
            googleMap.clear()

            // Tambahkan Circle
            googleMap.addCircle(
                CircleOptions()
                    .center(areaCenter)
                    .radius(1300.0) // 1 km
                    .strokeColor(Color.parseColor("#FFC80000"))
                    .fillColor(Color.parseColor("#25C80000"))
            )

            // Hitung apakah user di dalam area
            val isInside = LocationHelper.distance(userLatLng, areaCenter) < 1.0

            if (isInside) {
                binding.tvStatus.text = "Kamu berada di dalam area."
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
            } else {
                binding.tvStatus.text = "Kamu di luar area! Lokasimu dikunci kembali."
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(areaCenter, 15f))
            }
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