package com.colab.myfriend.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.biometric.BiometricPrompt
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.colab.myfriend.adapter.UserDao
import com.colab.myfriend.viewmodel.LoginViewModel
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : CoreActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    var inputEmail = ""
    var inputPassword = ""

    @Inject
    lateinit var session: CoreSession

    @Inject
    lateinit var userDao: UserDao

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.activity = this
        binding.btnLoginBiometric.setOnClickListener(this)

        binding.btnLoginBiometric.isVisible =
            session.getBoolean(TrialSettingActivity.BIOMETRIC_STATUS)

        binding.btnLogin.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundColor(Color.BLACK) // Saat ditekan
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.setBackgroundColor(Color.GRAY) // Kembali ke warna awal
            }
            false // Biarkan event tetap diteruskan ke onClickListener
        }

        binding.btnLogin.setOnClickListener {
            Toast.makeText(this, "Login button clicked!", Toast.LENGTH_SHORT).show()
            validateLogin()
        }


        lifecycleScope.launch {
            loadingDialog.show("Check Status")
            if (userDao.checkLogin() != null) {
                openActivity<MenuHomeActivity>()
                finish()
            }
            loadingDialog.dismiss()
        }

        observe()

        initBiometric()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        if (it.status == ApiStatus.LOADING) {
                            loadingDialog.show("Login")
                        } else {
                            loadingDialog.dismiss()
                        }
                        if (it.status == ApiStatus.SUCCESS) {
                            openActivity<MenuHomeActivity>()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun validateLogin() {
        inputEmail = binding.etEmail.text.toString().trim()
        inputPassword = binding.etPass.text.toString().trim()

        if (inputEmail.isEmpty()) {
            binding.inputPhone.error = "Isi Email"
            return
        }

        if (inputPassword.isEmpty()) {
            binding.inputPassword.error = "Isi Password"
            return
        }

        viewModel.login(inputEmail, inputPassword)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> validateLogin()
            binding.btnLoginBiometric -> biometricLogin()
        }
    }

    private fun initBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    binding.root.snacked("Biometric error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    binding.root.snacked("Biometric succeeded!")
                    viewModel.login(session.getString(EMAIL), session.getString(PASS))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.root.snacked("Biometric failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account instead")
            .build()
    }

    private fun biometricLogin() {
        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        const val EMAIL = "email"
        const val PASS = "password"
    }

}