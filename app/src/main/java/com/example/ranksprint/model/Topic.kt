package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)
