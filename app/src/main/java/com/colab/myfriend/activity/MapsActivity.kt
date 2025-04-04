package com.colab.myfriend.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.colab.myfriend.repository.PenolongLokasi
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityTrialMapBinding
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : NoViewModelActivity<ActivityTrialMapBinding>(R.layout.activity_trial_map) {

    @Inject
    lateinit var adrHelper : PenolongLokasi

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        checkLocationPermission {
            listenLocationChange()
        }
        binding.mapView.getMapAsync {googleMap ->
            googleMap.setOnCameraMoveListener {
                binding.ivTarget.alpha = 0.5f
            }

            googleMap.setOnCameraIdleListener {
                binding.ivTarget.alpha = 1f

                val curLocation = googleMap.cameraPosition.target
                binding.tvLocation.text = "Lat: ${curLocation.latitude} \nLng: ${curLocation.longitude}"

                adrHelper.getAddress(LatLng(curLocation.latitude, curLocation.longitude)) {
                    binding.tvAddress.text = it
                }
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