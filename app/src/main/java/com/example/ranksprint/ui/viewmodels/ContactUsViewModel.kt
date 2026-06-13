package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.quiztech.model.ContactUs
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsViewModel : ViewModel() {

    private val _contactInfo = MutableStateFlow<ContactUs?>(null)
    val contactInfo: StateFlow<ContactUs?> = _contactInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchContactDetails() {
        _isLoading.value = true
        _error.value = null
        ServiceManager.getDataManager().contactUs(object : Callback<ContactUsMain> {
            override fun onResponse(call: Call<ContactUsMain>, response: Response<ContactUsMain>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.status == true) {
                    _contactInfo.value = response.body()?.data?.firstOrNull()
                } else {
                    _error.value = "Failed to fetch contact details"
                }
            }

            override fun onFailure(call: Call<ContactUsMain>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        })
    }
}
