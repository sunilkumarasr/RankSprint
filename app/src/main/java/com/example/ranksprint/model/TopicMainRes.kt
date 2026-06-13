package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class TopicMainRes(
    @SerializedName("status") val status: Int,
    @SerializedName("topics") val topics: List<Topic>
)
