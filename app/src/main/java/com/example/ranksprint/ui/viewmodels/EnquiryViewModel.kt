package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnquiryViewModel : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting

    private val _submitResult = MutableStateFlow<Result<String>?>(null)
    val submitResult: StateFlow<Result<String>?> = _submitResult

    fun submitEnquiry(name: String, phone: String, subject: String, message: String, user_id: String, email: String) {
        _isSubmitting.value = true
        _submitResult.value = null
        
        ServiceManager.getDataManager().enquiryForm(
            object : Callback<FAQsMainResponse> {
                override fun onResponse(call: Call<FAQsMainResponse>, response: Response<FAQsMainResponse>) {
                    _isSubmitting.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        _submitResult.value = Result.success(response.body()?.message ?: "Enquiry submitted successfully")
                    } else {
                        _submitResult.value = Result.failure(Exception(response.body()?.message ?: "Failed to submit enquiry"))
                    }
                }

                override fun onFailure(call: Call<FAQsMainResponse>, t: Throwable) {
                    _isSubmitting.value = false
                    _submitResult.value = Result.failure(t)
                }
            },
            name, phone, subject, message, user_id,email
        )
    }

    fun resetResult() {
        _submitResult.value = null
    }
}
