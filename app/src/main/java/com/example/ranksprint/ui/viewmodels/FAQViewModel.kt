package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.Faqs
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQViewModel : ViewModel() {
    private val _faqs = MutableStateFlow<List<Faqs>>(emptyList())
    val faqs: StateFlow<List<Faqs>> = _faqs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchFAQs() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().faqs(object : Callback<FAQsMainResponse> {
                override fun onResponse(call: Call<FAQsMainResponse>, response: Response<FAQsMainResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        _faqs.value = response.body()?.data ?: emptyList()
                    } else {
                        _error.value = response.body()?.message ?: "Failed to fetch FAQs"
                    }
                }

                override fun onFailure(call: Call<FAQsMainResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message
                }
            })
        }
    }
    
    fun toggleExpand(faqId: String) {
        _faqs.value = _faqs.value.map {
            if (it.id == faqId) it.copy(isExpand = !it.isExpand) else it
        }
    }
}
