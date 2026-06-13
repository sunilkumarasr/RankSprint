package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.SubCategory
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryViewModel : ViewModel() {

    private val _subCategories = MutableStateFlow<List<SubCategory>>(emptyList())
    val subCategories: StateFlow<List<SubCategory>> = _subCategories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchSubCategories(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getSubCategories(object : Callback<SubCategoryMainRes> {
                override fun onResponse(call: Call<SubCategoryMainRes>, response: Response<SubCategoryMainRes>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == 1) {
                        _subCategories.value = response.body()?.subCategories ?: emptyList()
                    } else {
                        _error.value = response.body()?.message ?: "Failed to fetch subcategories"
                    }
                }

                override fun onFailure(call: Call<SubCategoryMainRes>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            }, categoryId)
        }
    }
}
