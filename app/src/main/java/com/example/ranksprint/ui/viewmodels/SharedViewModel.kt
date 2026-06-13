package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.quiztech.model.UserInfo
import com.example.quiztech.model.SubmitExamResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _lastSubmitResponse = MutableStateFlow<SubmitExamResponse?>(null)
    val lastSubmitResponse: StateFlow<SubmitExamResponse?> = _lastSubmitResponse

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    fun setSubmitResponse(response: SubmitExamResponse?) {
        _lastSubmitResponse.value = response
    }

    fun setUserInfo(userInfo: UserInfo?) {
        _userInfo.value = userInfo
    }
}
