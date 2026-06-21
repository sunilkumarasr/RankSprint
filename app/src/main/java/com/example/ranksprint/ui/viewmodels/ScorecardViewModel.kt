package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.LeaderBoardItem
import com.example.quiztech.model.LeaderBoardResponse
import com.example.quiztech.model.MyExamItem
import com.example.quiztech.model.MyExamsResponse
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScorecardViewModel : ViewModel() {
    private val _scorecardData = MutableStateFlow<MyExamItem?>(null)
    val scorecardData: StateFlow<MyExamItem?> = _scorecardData

    private val _LeaderBoard = MutableStateFlow<List<LeaderBoardItem>>(emptyList())
    val LeaderBoard: StateFlow<List<LeaderBoardItem>> = _LeaderBoard

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchScorecard(userId: String, productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            ServiceManager.getDataManager().getMyExams(object : Callback<MyExamsResponse> {
                override fun onResponse(
                    call: Call<MyExamsResponse>,
                    response: Response<MyExamsResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.status == true) {
                        val exams = response.body()?.data ?: emptyList()
                        _scorecardData.value = exams.find { it.productId == productId }
                        if (_scorecardData.value == null) {
                            _error.value = "Test details not found"
                        }
                    } else {
                        _error.value = "Failed to fetch details: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<MyExamsResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message
                }
            }, userId, "1")
        }
    }

    fun fetchWinningData(userId: String, productId: String) {
        ServiceManager.getDataManager().getWinningData(
            object : Callback<LeaderBoardResponse> {
                override fun onResponse(
                    call: Call<LeaderBoardResponse>,
                    response: Response<LeaderBoardResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.status == 1) {
                            _LeaderBoard.value = body.leaderboard ?: emptyList()
                        } else {
                            _LeaderBoard.value = emptyList()
                            _error.value =
                                body.message ?: "No leaderboard found"
                        }
                    } else {
                        _error.value =
                            "Failed to fetch leaderboard: ${response.message()}"
                    }
                }

                override fun onFailure(
                    call: Call<LeaderBoardResponse>,
                    t: Throwable
                ) {
                    _isLoading.value = false
                    _error.value = t.message
                }
            },
            userId, productId
        )
    }


}
