package com.keepcoding.dragonball.views.root

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keepcoding.dragonball.commons.SharedPreferencesKeys
import com.keepcoding.dragonball.databinding.ActivityRootBinding
import com.keepcoding.dragonball.views.app.AppActivity
import com.keepcoding.dragonball.views.login.LoginActivity

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private lateinit var shared : SharedPreferences 
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        shared = getSharedPreferences(SharedPreferencesKeys.FILE_PREFERENCE, Context.MODE_PRIVATE)
        setContentView(binding.root)
        
        checkSession()
    }

    private fun checkSession() {
        val token = shared.getString(SharedPreferencesKeys.TOKEN, "")
        val alreadyLogged = token?.let {
            it.isNotEmpty()
        } ?: false

        if (alreadyLogged) {
            AppActivity.go(this)
        } else {
            LoginActivity.go(this)
        }
    }
}