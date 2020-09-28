package com.example.myapplication


import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isBiometricEnrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)

        checkBiometricStatus(biometricManager)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast("Error")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showToast("Success")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setDescription("Use TouchId or FaceId for login")
            .setNegativeButtonText("Cancel")
            .build()

        tv_biometric_sign_in.setOnClickListener {
            if (isBiometricEnrolled) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                showToast("Please go to settings to setup Face/TouchId")
            }

        }
    }

    private fun checkBiometricStatus(biometricManager: BiometricManager) {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                tv_biometric_sign_in.visibility = VISIBLE
                isBiometricEnrolled = true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> tv_biometric_sign_in.visibility = GONE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> tv_biometric_sign_in.visibility =
                GONE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                tv_biometric_sign_in.visibility = VISIBLE
                isBiometricEnrolled = false
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}