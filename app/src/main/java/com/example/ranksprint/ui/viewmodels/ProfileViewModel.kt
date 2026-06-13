package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.UserInfo
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchProfile(userId: String) {
        if (userId.isEmpty()) return
        
        _isLoading.value = true
        _error.value = null
        ServiceManager.getDataManager().getProfile(object : Callback<OTPVerifyResponse> {
            override fun onResponse(call: Call<OTPVerifyResponse>, response: Response<OTPVerifyResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.status == 1) {
                    _userInfo.value = response.body()?.userInfo
                } else {
                    _error.value = response.body()?.message ?: "Failed to fetch profile"
                }
            }

            override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        }, userId)
    }
}
