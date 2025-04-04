package com.colab.myfriend.activity

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import com.colab.myfriend.repository.PenolongLokasi
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityTrialMapBinding
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrialMapActivity : NoViewModelActivity<ActivityTrialMapBinding>(R.layout.activity_trial_map) {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        checkLocationPermission {
            listenLocationChange()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mapView.getMapAsync { googleMap ->
            googleMap.setOnCameraMoveListener {
                binding.ivTarget.alpha = 0.5f
            }

            googleMap.setOnCameraIdleListener {
                binding.ivTarget.alpha = 1f

                val curLocation = googleMap.cameraPosition.target
                binding.tvLocation.text = "Lat: ${curLocation.latitude} \nLng: ${curLocation.longitude}"

                PenolongLokasi(Geocoder(this)).getAddress(LatLng(curLocation.latitude, curLocation.longitude)) {
                    binding.tvAddress.text = it
                }
            }
        }
    }
}
