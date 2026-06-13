package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.*
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MockTestViewModel : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _sections = MutableStateFlow<Map<String, List<Question>>>(emptyMap())
    val sections: StateFlow<Map<String, List<Question>>> = _sections

    private val _testDetails = MutableStateFlow<TestDetailsData?>(null)
    val testDetails: StateFlow<TestDetailsData?> = _testDetails

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _selectedOptions = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedOptions: StateFlow<Map<String, String>> = _selectedOptions

    private val _timeLeft = MutableStateFlow(0L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _submitSuccess = MutableStateFlow<SubmitExamResponse?>(null)
    val submitSuccess: StateFlow<SubmitExamResponse?> = _submitSuccess

    private var currentTestId: String? = null
    private var timerJob: kotlinx.coroutines.Job? = null
    private var isSubmitting = false

    fun startTest(testId: String) {
        currentTestId = testId
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // 1. Fetch Test Details for duration
            val userId = Utils.user_id ?: ""
            ServiceManager.getDataManager().getTestDetails(object : Callback<TestDetailsResponse> {
                override fun onResponse(call: Call<TestDetailsResponse>, response: Response<TestDetailsResponse>) {
                    if (response.isSuccessful && response.body()?.status == 1) {
                        val details = response.body()?.mockData
                        _testDetails.value = details
                        val duration = details?.pDuration?.toLongOrNull() ?: 0L
                        startTimer(duration * 60)
                        
                        // 2. Fetch Questions
                        fetchQuestions(testId)
                    } else if (response.body()?.status == 0 && response.body()?.message?.contains("already attempted", ignoreCase = true) == true) {
                        _isLoading.value = false
                        _error.value = "ALREADY_ATTEMPTED"
                    } else {
                        _isLoading.value = false
                        _error.value = response.body()?.message ?: "Failed to fetch test details"
                    }
                }

                override fun onFailure(call: Call<TestDetailsResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message ?: "An error occurred"
                }
            }, testId, userId)
        }
    }

    private fun fetchQuestions(testId: String) {
        ServiceManager.getDataManager().getExamQuestions(object : Callback<ExamQuestionsResponse> {
            override fun onResponse(call: Call<ExamQuestionsResponse>, response: Response<ExamQuestionsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.status == true) {
                    val dataMap = response.body()?.data ?: emptyMap()
                    _sections.value = dataMap
                    val allQuestions = dataMap.values.flatten()
                    _questions.value = allQuestions
                } else {
                    _error.value = response.body()?.message ?: "Failed to fetch questions"
                }
            }

            override fun onFailure(call: Call<ExamQuestionsResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message ?: "An error occurred"
            }
        }, testId)
    }

    private fun startTimer(seconds: Long) {
        if (seconds <= 0) return
        _timeLeft.value = seconds
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            // Auto-submit when time ends
            currentTestId?.let { submitExam(it) }
        }
    }

    fun selectOption(questionId: String, optionId: String) {
        val currentAnswers = _selectedOptions.value.toMutableMap()
        if (currentAnswers[questionId] == optionId) {
            currentAnswers.remove(questionId)
        } else {
            currentAnswers[questionId] = optionId
        }
        _selectedOptions.value = currentAnswers
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun setCurrentQuestion(index: Int) {
        _currentQuestionIndex.value = index
    }

    fun submitExam(productId: String) {
        if (isSubmitting) return
        val userId = Utils.user_id ?: ""
        if (userId.isEmpty()) {
            _error.value = "User not logged in"
            return
        }
        
        val answers = _selectedOptions.value.map { (qId, oId) ->
            Answer(qId, oId)
        }
        val request = SubmitExamRequest(userId, productId, answers)

        viewModelScope.launch {
            isSubmitting = true
            _isLoading.value = true
            ServiceManager.getDataManager().submitExam(object : Callback<SubmitExamResponse> {
                override fun onResponse(call: Call<SubmitExamResponse>, response: Response<SubmitExamResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        timerJob?.cancel()
                        _submitSuccess.value = response.body()
                    } else {
                        isSubmitting = false
                        _error.value = response.body()?.message ?: "Submission failed"
                    }
                }

                override fun onFailure(call: Call<SubmitExamResponse>, t: Throwable) {
                    _isLoading.value = false
                    isSubmitting = false
                    _error.value = t.message
                }
            }, request)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
