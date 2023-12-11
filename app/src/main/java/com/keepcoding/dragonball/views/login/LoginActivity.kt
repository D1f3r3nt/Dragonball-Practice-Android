package com.keepcoding.dragonball.views.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.Response
import com.keepcoding.dragonball.commons.SharedPreferencesKeys
import com.keepcoding.dragonball.databinding.ActivityLoginBinding
import com.keepcoding.dragonball.views.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkSession()

        setListeners()
        setObservers()
    }

    private fun handleErrorState(error: Error) {
        Toast.makeText(this, error.msg, Toast.LENGTH_LONG).show()
    }

    private fun handleResponseState(response: Response) {
        getPreferences(Context.MODE_PRIVATE).edit().apply{
            putString(SharedPreferencesKeys.TOKEN, response.response)
            apply()
        }
        
        HomeActivity.go(this)
    }

    private fun checkSession() {
        val token = getPreferences(MODE_PRIVATE).getString(SharedPreferencesKeys.TOKEN, "")
        val alreadyLogged = token?.let {
            it.isNotEmpty()
        } ?: false

        if (alreadyLogged) {
            HomeActivity.go(this)
        }
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
                    is Response -> handleResponseState(state)
                    else -> {}
                }
            }
        }
    }
}