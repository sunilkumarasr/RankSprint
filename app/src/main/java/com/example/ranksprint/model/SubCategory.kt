package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class SubCategoryMainRes (

    @SerializedName("status"         ) var status        : Int?                     = null,
    @SerializedName("message"        ) var message       : String?                  = null,
    @SerializedName("sub_categories" ) var subCategories : ArrayList<SubCategory> = arrayListOf()

)
data class SubCategory(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("has_topics") val has_topics: Int=0
)
