package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class ExamQuestionsResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Map<String, List<Question>>? = null
)

data class Question(
    @SerializedName("id") var id: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("question_text") var questionText: String? = null,
    @SerializedName("question_image") var questionImage: String? = null,
    @SerializedName("section_id") var sectionId: String? = null,
    @SerializedName("section_name") var sectionName: String? = null,
    @SerializedName("options") var options: List<Option>? = null
)

data class Option(
    @SerializedName("id") var id: String? = null,
    @SerializedName("option_text") var optionText: String? = null,
    @SerializedName("option_image") var optionImage: String? = null
)

data class SubmitExamRequest(
    @SerializedName("user_id") var userId: String,
    @SerializedName("product_id") var productId: String,
    @SerializedName("answers") var answers: List<Answer>
)

data class Answer(
    @SerializedName("question_id") var questionId: String,
    @SerializedName("selected_option_id") var selectedOptionId: String
)

data class SubmitExamResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: SubmitData? = null
)

data class SubmitData(
    @SerializedName("score") var score: String? = null,
    @SerializedName("total_questions") var totalQuestions: Int? = null,
    @SerializedName("correct_answers") var correctAnswers: Int? = null,
    @SerializedName("wrong_answers") var wrongAnswers: Int? = null,
    @SerializedName("score_percentage") var scorePercentage: String? = null
)
