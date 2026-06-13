package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.PageData
import com.example.quiztech.model.PageResponse
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PolicyViewModel : ViewModel() {

    private val _pageData = MutableStateFlow<Map<String, PageData?>>(emptyMap())
    val pageData: StateFlow<Map<String, PageData?>> = _pageData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPage(pageName: String) {
        if (_pageData.value.containsKey(pageName)) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getPageList(object : Callback<PageResponse> {
                override fun onResponse(call: Call<PageResponse>, response: Response<PageResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        val data = response.body()?.data?.firstOrNull()
                        _pageData.value = _pageData.value + (pageName to data)
                    } else {
                        _error.value = "Failed to fetch $pageName: ${response.body()?.message ?: response.message()}"
                    }
                }

                override fun onFailure(call: Call<PageResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = "Error fetching $pageName: ${t.message}"
                }
            }, pageName)
        }
    }
}
