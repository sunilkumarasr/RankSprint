package com.example.ranksprint.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztech.model.BannerList
import com.example.quiztech.model.BannerResponse
import com.example.quiztech.model.Category
import com.example.quiztech.model.CategoryResponse
import com.example.quiztech.model.LeaderBoardItem
import com.example.quiztech.model.LeaderBoardResponse
import com.example.quiztech.model.MockList
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.services.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _banners = MutableStateFlow<List<BannerList>>(emptyList())
    val banners: StateFlow<List<BannerList>> = _banners

    private val _popularMockTests = MutableStateFlow<List<MockList>>(emptyList())
    val popularMockTests: StateFlow<List<MockList>> = _popularMockTests

    private val _LeaderBoard =
        MutableStateFlow<List<LeaderBoardItem>>(emptyList())
    val LeaderBoard: StateFlow<List<LeaderBoardItem>> =
        _LeaderBoard

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var categoriesLoaded = false
    private var bannersLoaded = false
    private var mockTestsLoaded = false
    private var leaderBoardLoaded = false


    fun fetchData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            categoriesLoaded = false
            bannersLoaded = false
            mockTestsLoaded = false
            leaderBoardLoaded = false
            fetchCategories()
            fetchBanners()
            fetchPopularMockTests()
            fetchLeaderBoard(userId)
        }
    }

    private fun fetchCategories() {
        ServiceManager.getDataManager().getCategories(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                categoriesLoaded = true
                if (response.isSuccessful) {
                    _categories.value = response.body()?.categories ?: emptyList()
                } else {
                    _error.value = "Failed to fetch categories: ${response.message()}"
                }
                checkLoadingFinished()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                categoriesLoaded = true
                _error.value = "Error fetching categories: ${t.message}"
                checkLoadingFinished()
            }
        })
    }

    private fun fetchBanners() {
        ServiceManager.getDataManager().getBanners(object : Callback<BannerResponse> {
            override fun onResponse(call: Call<BannerResponse>, response: Response<BannerResponse>) {
                bannersLoaded = true
                if (response.isSuccessful) {
                    _banners.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to fetch banners: ${response.message()}"
                }
                checkLoadingFinished()
            }

            override fun onFailure(call: Call<BannerResponse>, t: Throwable) {
                bannersLoaded = true
                _error.value = "Error fetching banners: ${t.message}"
                checkLoadingFinished()
            }
        })
    }

    private fun fetchPopularMockTests() {
        ServiceManager.getDataManager().getPopularMockTests(object : Callback<MockListMainRes> {
            override fun onResponse(call: Call<MockListMainRes>, response: Response<MockListMainRes>) {
                mockTestsLoaded = true
                if (response.isSuccessful) {
                    _popularMockTests.value = response.body()?.mockList ?: emptyList()
                } else {
                    _error.value = "Failed to fetch popular tests: ${response.message()}"
                }
                checkLoadingFinished()
            }

            override fun onFailure(call: Call<MockListMainRes>, t: Throwable) {
                mockTestsLoaded = true
                _error.value = "Error fetching popular tests: ${t.message}"
                checkLoadingFinished()
            }
        })
    }

    private fun fetchLeaderBoard(userId: String) {

        ServiceManager.getDataManager().getLeaderBoard(
            object : Callback<LeaderBoardResponse> {

                override fun onResponse(
                    call: Call<LeaderBoardResponse>,
                    response: Response<LeaderBoardResponse>
                ) {

                    leaderBoardLoaded = true

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

                    checkLoadingFinished()
                }

                override fun onFailure(
                    call: Call<LeaderBoardResponse>,
                    t: Throwable
                ) {
                    leaderBoardLoaded = true
                    _error.value =
                        "Error fetching leaderboard: ${t.message}"
                    checkLoadingFinished()
                }
            },
            userId
        )
    }


    private fun checkLoadingFinished() {
        if (categoriesLoaded && bannersLoaded && mockTestsLoaded && leaderBoardLoaded) {
            _isLoading.value = false
        }
    }
}
