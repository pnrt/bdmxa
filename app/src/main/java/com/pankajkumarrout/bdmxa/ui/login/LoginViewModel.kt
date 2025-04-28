package com.pankajkumarrout.bdmxa.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankajkumarrout.bdmxa.model.ForAuthentication
import com.pankajkumarrout.bdmxa.model.User
import com.pankajkumarrout.bdmxa.network.LoginApiService
import kotlinx.coroutines.launch


object LogUser {
    var presentUser: User? = null
}

class LoginViewModel : ViewModel() {
    var isAuthorised: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var message by mutableStateOf("")

    private val loginApiService = LoginApiService()

    fun loginUser(username: String, password: String) {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val auth = ForAuthentication(username, password)
                val response = loginApiService.logUser(auth)
                LogUser.presentUser = response
                if (response.isAuthorized){
                    isAuthorised = true
                    message = "Login Successful ✅"
                } else {
                    message = "Unauthorised ❌"
                }
            } catch (e: Exception) {
                message = "Network error... ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}