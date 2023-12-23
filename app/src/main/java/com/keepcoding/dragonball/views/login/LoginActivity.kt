package com.keepcoding.dragonball.views.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.ResponseToken
import com.keepcoding.dragonball.commons.SharedPreferencesKeys
import com.keepcoding.dragonball.databinding.ActivityLoginBinding
import com.keepcoding.dragonball.views.app.AppActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    companion object {
        fun go(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var shared : SharedPreferences
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        shared = getSharedPreferences(SharedPreferencesKeys.FILE_PREFERENCE, Context.MODE_PRIVATE)
        setContentView(binding.root)

        setListeners()
        setObservers()
    }

    private fun handleErrorState(error: Error) {
        Toast.makeText(this, error.msg, Toast.LENGTH_LONG).show()
    }

    private fun handleResponseTokenState(response: ResponseToken) {
        shared.edit().apply{
            putString(SharedPreferencesKeys.TOKEN, response.token)
            apply()
        }
        
        AppActivity.go(this)
    }

    private fun setListeners() {
        binding.login.setOnClickListener { 
            viewModel.login(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }
        
        binding.username.doAfterTextChanged { newText ->  
            viewModel.checkUsernameFormat(newText.toString())
        }

        binding.password.doAfterTextChanged { newText ->
            viewModel.checkPasswordFormat(newText.toString())
        }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) { 
            viewModel.usernameFormat.collect{ correctFormat ->
                binding.usernameFormat.visibility = if (correctFormat) View.GONE else View.VISIBLE
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.passwordFormat.collect{ correctFormat ->
                binding.passwordFormat.visibility = if (correctFormat) View.GONE else View.VISIBLE
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collect{ state ->
                when (state) {
                    is Error -> handleErrorState(state)
                    is ResponseToken -> handleResponseTokenState(state)
                    else -> {}
                }
            }
        }
    }
}