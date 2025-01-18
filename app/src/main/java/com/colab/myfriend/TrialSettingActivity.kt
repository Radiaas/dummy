package com.colab.myfriend

import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.colab.myfriend.activity.LoginActivity
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.clearNotification
import com.crocodic.core.extension.openActivity
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityTrialSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrialSettingActivity :
    CoreActivity<ActivityTrialSettingBinding, TrialSettingViewModel>(R.layout.activity_trial_setting) {

    @Inject
    lateinit var session: CoreSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        checkBiometricCapability()

        binding.swBiometric.apply {
            isChecked = session.getBoolean(BIOMETRIC_STATUS)
            setOnCheckedChangeListener { _, isChecked ->
                session.setValue(BIOMETRIC_STATUS, isChecked)
            }
        }


//        binding.tvToken.text = session.getString(CoreSession.PREF_UID)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        if (it.status == ApiStatus.LOADING) {
                            loadingDialog.show("Logout")
                        } else {
                            loadingDialog.dismiss()
                        }
                        if (it.status == ApiStatus.SUCCESS) {
                            loadingDialog.dismiss()
                            expiredDialog.dismiss()
                            clearNotification()
                            openActivity<LoginActivity>()
                            finishAffinity()
                        }
                    }
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun checkBiometricCapability() {
        val biometricManager = BiometricManager.from(this)
        val canUseBiometric =
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    // App can authenticate using biometrics.
                    true
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    // No biometric features available on this device.
                    false
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    // Biometric features are currently unavailable.
                    false
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Prompts the user to create credentials that your app accepts.

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                        }
                        activityLauncher.launch(enrollIntent) {
                            checkBiometricCapability()
                        }
                    }*/
                    false

                }

                else -> false
            }

        binding.swBiometric.isVisible = canUseBiometric
    }

    companion object {
        const val BIOMETRIC_STATUS = "biometric_status"
    }
}