package com.colab.myfriend.activity

import android.os.Bundle
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityTrialMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : NoViewModelActivity<ActivityTrialMapBinding>(R.layout.activity_trial_map) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        checkLocationPermission {
            listenLocationChange()
        }
        binding.mapView.getMapAsync { _ ->

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