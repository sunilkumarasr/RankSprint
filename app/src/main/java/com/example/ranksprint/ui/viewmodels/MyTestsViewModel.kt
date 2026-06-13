package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.MyExamsResponse
import com.example.quiztech.model.MyExamItem
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTestsViewModel : ViewModel() {

    private val _completedTests = MutableStateFlow<List<MyExamItem>>(emptyList())
    val completedTests: StateFlow<List<MyExamItem>> = _completedTests

    private val _newTests = MutableStateFlow<List<MyExamItem>>(emptyList())
    val newTests: StateFlow<List<MyExamItem>> = _newTests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMyExams(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Fetch completed exams (type 1)
            ServiceManager.getDataManager().getMyExams(object : Callback<MyExamsResponse> {
                override fun onResponse(call: Call<MyExamsResponse>, response: Response<MyExamsResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        _completedTests.value = response.body()?.data ?: emptyList()
                    }
                    // Fetch new exams (type 0) - assuming type 0 is for "New/Ongoing"
                    fetchNewExams(userId)
                }

                override fun onFailure(call: Call<MyExamsResponse>, t: Throwable) {
                    _error.value = t.message
                    fetchNewExams(userId)
                }
            }, userId, "1")
        }
    }

    private fun fetchNewExams(userId: String) {
        ServiceManager.getDataManager().getMyExams(object : Callback<MyExamsResponse> {
            override fun onResponse(call: Call<MyExamsResponse>, response: Response<MyExamsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.status == true) {
                    _newTests.value = response.body()?.data ?: emptyList()
                }
            }

            override fun onFailure(call: Call<MyExamsResponse>, t: Throwable) {
                _isLoading.value = false
                if (_error.value == null) _error.value = t.message
            }
        }, userId, "0")
    }
}
