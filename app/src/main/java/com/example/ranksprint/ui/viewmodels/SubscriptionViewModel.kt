package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.SubscriptionPlan
import com.example.quiztech.model.SubscriptionDetail
import com.example.quiztech.model.SubscriptionDetailRes
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.ActivePlan
import com.example.quiztech.services.ServiceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.quiztech.model.EnrollResponse
import com.example.ranksprint.common.Utils
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionViewModel : ViewModel() {
    private val _plans = MutableStateFlow<List<SubscriptionPlan>>(emptyList())
    val plans: StateFlow<List<SubscriptionPlan>> = _plans

    private val _activePlan = MutableStateFlow<ActivePlan?>(null)
    val activePlan: StateFlow<ActivePlan?> = _activePlan

    private val _isActivePlan = MutableStateFlow(false)
    val isActivePlan: StateFlow<Boolean> = _isActivePlan

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _subscribeResult = MutableStateFlow<EnrollResponse?>(null)
    val subscribeResult = _subscribeResult.asStateFlow()

    private val _selectedPlanDetail = MutableStateFlow<SubscriptionDetail?>(null)
    val selectedPlanDetail: StateFlow<SubscriptionDetail?> = _selectedPlanDetail

    fun fetchSubscriptionPlans(user_id:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            ServiceManager.getDataManager().getSubscriptionPlans(object : Callback<SubscriptionMainRes> {
                override fun onResponse(call: Call<SubscriptionMainRes>, response: Response<SubscriptionMainRes>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            _isActivePlan.value = body.isActivePlan == true
                            if (body.isActivePlan == true) {
                                try {
                                    val gson = Gson()
                                    val json = gson.toJson(body.data)
                                    val activePlan: ActivePlan = gson.fromJson(json, ActivePlan::class.java)
                                    _activePlan.value = activePlan
                                    _plans.value = emptyList()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else {
                                try {
                                    val gson = Gson()
                                    val json = gson.toJson(body.data)
                                    val listType = object : TypeToken<ArrayList<SubscriptionPlan>>() {}.type
                                    val plans: ArrayList<SubscriptionPlan> = gson.fromJson(json, listType)
                                    _plans.value = plans
                                    _activePlan.value = null
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            _error.value = body?.message ?: "Failed to load plans"
                        }
                    } else {
                        _error.value = "Server error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<SubscriptionMainRes>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            },user_id)
        }
    }

    fun fetchSubscriptionPlanDetail(planId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getSubscriptionPlanDetail(object : Callback<SubscriptionDetailRes> {
                override fun onResponse(call: Call<SubscriptionDetailRes>, response: Response<SubscriptionDetailRes>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            _selectedPlanDetail.value = body.data
                        } else {
                            _error.value = body?.message ?: "Failed to load plan details"
                        }
                    } else {
                        _error.value = "Server error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<SubscriptionDetailRes>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            }, planId)
        }
    }

    fun subscribePackage(userId: String, productId: String, paymentId:String,onResult: (Boolean, String) -> Unit,) {
        viewModelScope.launch {
            _isLoading.value = true
            ServiceManager.getDataManager().enrollProduct(object : Callback<EnrollResponse> {
                override fun onResponse(call: Call<EnrollResponse>, response: Response<EnrollResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            _subscribeResult.value = body
                            onResult(true, body.message ?: "Subscription successful")
                        } else {
                            onResult(false, body?.message ?: "Failed to subscribe")
                        }
                    } else {
                        onResult(false, "Server error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EnrollResponse>, t: Throwable) {
                    _isLoading.value = false
                    onResult(false, t.message ?: "An error occurred")
                }
            }, userId, productId,paymentId)
        }
    }
}
