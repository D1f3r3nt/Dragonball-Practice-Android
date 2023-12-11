package com.keepcoding.dragonball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.Idle
import com.keepcoding.dragonball.commons.Response
import com.keepcoding.dragonball.commons.State
import com.keepcoding.dragonball.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    
    private val networkConnection = NetworkConnection()
    
    private val _usernameFormat = MutableStateFlow(false)
    private val _passwordFormat = MutableStateFlow(false)
    private val _state = MutableStateFlow<State>(Idle())
    
    val usernameFormat: StateFlow<Boolean> = _usernameFormat
    val passwordFormat: StateFlow<Boolean> = _passwordFormat
    val state: StateFlow<State> = _state
    
    fun checkUsernameFormat(text: String) {
       _usernameFormat.value = text.length > 3
    }

    fun checkPasswordFormat(text: String) {
        _passwordFormat.value = text.length > 3
    }
    
    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_usernameFormat.value || !_passwordFormat.value) return@launch
            
            _state.value = networkConnection.getLogin(username, password)
        }
    }
}