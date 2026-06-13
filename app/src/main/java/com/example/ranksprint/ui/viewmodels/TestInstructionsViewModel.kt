package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.TestDetailsData
import com.example.quiztech.model.TestDetailsResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import com.example.quiztech.model.EnrollResponse
import com.example.quiztech.model.MainResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestInstructionsViewModel : ViewModel() {

    private val _testDetails = MutableStateFlow<TestDetailsData?>(null)
    val testDetails: StateFlow<TestDetailsData?> = _testDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isEnrolling = MutableStateFlow(false)
    val isEnrolling: StateFlow<Boolean> = _isEnrolling

    private val _enrollmentSuccess = MutableStateFlow<Boolean?>(null)
    val enrollmentSuccess: StateFlow<Boolean?> = _enrollmentSuccess

    fun fetchTestDetails(testId: String) {
        val userId = Utils.user_id ?: ""
        if (userId.isEmpty()) {
            _error.value = "User not logged in"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getTestDetails(object : Callback<TestDetailsResponse> {
                override fun onResponse(call: Call<TestDetailsResponse>, response: Response<TestDetailsResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == 1) {
                        _testDetails.value = response.body()?.mockData
                    } else if (response.body()?.status == 0 && response.body()?.message?.contains("already attempted", ignoreCase = true) == true) {
                        _error.value = "ALREADY_ATTEMPTED"
                    } else {
                        val message = response.body()?.message ?: "Failed to fetch test details"
                        if (message.contains("login", ignoreCase = true) || response.code() == 401) {
                            _error.value = "NOT_LOGGED_IN"
                        } else {
                            _error.value = message
                        }
                    }
                }

                override fun onFailure(call: Call<TestDetailsResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            }, testId, userId)
        }
    }

    fun enrollInTest(productId: String) {
        val userId = Utils.user_id ?: ""
        if (userId.isEmpty()) {
            _error.value = "NOT_LOGGED_IN"
            return
        }

        viewModelScope.launch {/*
            _isEnrolling.value = true
            ServiceManager.getDataManager().enrollProduct(object : Callback<EnrollResponse> {
                override fun onResponse(call: Call<EnrollResponse>, response: Response<EnrollResponse>) {
                    _isEnrolling.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        _enrollmentSuccess.value = true
                        // Refresh details to update is_enrolled status
                        val testId = _testDetails.value?.id ?: ""
                        if (testId.isNotEmpty()) {
                            fetchTestDetails(testId)
                        }
                    } else {
                        _error.value = response.body()?.message ?: "Enrollment failed"
                    }
                }

                override fun onFailure(call: Call<EnrollResponse>, t: Throwable) {
                    _isEnrolling.value = false
                    _error.value = t.message
                }
            }, userId, productId)*/
        }
    }

    fun resetEnrollmentStatus() {
        _enrollmentSuccess.value = null
    }
}
