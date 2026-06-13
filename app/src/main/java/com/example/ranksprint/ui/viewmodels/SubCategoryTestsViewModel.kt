package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.MockList
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryTestsViewModel : ViewModel() {

    private val _mockTests = MutableStateFlow<List<MockList>>(emptyList())
    val mockTests: StateFlow<List<MockList>> = _mockTests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMockTestsBySubCategory(subCategoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getMockTestBySubCategory(object : Callback<MockListMainRes> {
                override fun onResponse(call: Call<MockListMainRes>, response: Response<MockListMainRes>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == 1) {
                        _mockTests.value = response.body()?.mockList ?: emptyList()
                    } else {
                        _error.value = response.body()?.message ?: "Failed to fetch mock tests"
                    }
                }

                override fun onFailure(call: Call<MockListMainRes>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            }, subCategoryId)
        }
    }
}
