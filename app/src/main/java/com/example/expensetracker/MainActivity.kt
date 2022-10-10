package com.example.expensetracker

import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.expensetracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cancellationSignal:CancellationSignal? = null
    lateinit var sharedPreferences:SharedPreferences
    private val authenticationCallBack:BiometricPrompt.AuthenticationCallback
    get() =
        object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser(getString(R.string.auth_error) + errString)
                this@MainActivity.finish()
                exitProcess(0)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser(getString(R.string.auth_success))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =  baseContext.getSharedPreferences("setting",Context.MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", AppCompatDelegate.MODE_NIGHT_YES)
        AppCompatDelegate.setDefaultNightMode(theme)

        if (!checkBiometricSupport()) {
            this@MainActivity.finish()
            exitProcess(0)
        }

        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle(getString(R.string.expense_authentication))
            .setSubtitle(getString(R.string.subtitle))
            .setDescription(getString(R.string.auth_description))
            .setNegativeButton(getString(R.string.cancel),this.mainExecutor) { dialog, which ->
                notifyUser(getString(R.string.auth_cancelled))
                this@MainActivity.finish()
                exitProcess(0)
            }.build()

            biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallBack)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_statistic, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onPause() {
        super.onPause()
        with (sharedPreferences.edit()) {
            this?.putBoolean("Auth",true)
            this?.apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with (sharedPreferences.edit()) {
            this?.putBoolean("Auth",false)
            this?.apply()
        }
    }

    private fun notifyUser(message:String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    private fun getCancellationSignal():CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser(getString(R.string.auth_cancelled_user))
            this@MainActivity.finish()
            exitProcess(0)
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport():Boolean{
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if(!keyguardManager.isKeyguardSecure) {
            notifyUser(getString(R.string.finger_not_enabled))
            return false
        }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            notifyUser(getString(R.string.finger_not_enabled))
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true
        }
        else {
            true
        }
    }
}