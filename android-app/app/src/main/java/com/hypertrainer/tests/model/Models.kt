package com.hypertrainer.tests.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("question") val question: String,
    @SerializedName("answers") val answers: List<Answer>,
    @SerializedName("correct_answer") val correctAnswer: String? = null
) {
    fun getCorrectAnswerIndex(): Int {
        return answers.indexOfFirst { it.correct }
    }
    
    fun isAnswerCorrect(index: Int): Boolean {
        return answers.getOrNull(index)?.correct == true
    }
}

data class Answer(
    @SerializedName("answer") val answer: String,
    @SerializedName("correct") val correct: Boolean
)

data class TestFile(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("filename") val filename: String
)

data class TestContent(
    @SerializedName("title") val title: String,
    @SerializedName("questions") val questions: List<Question>
)

data class IndexEntry(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("filename") val filename: String
)

data class IndexFile(
    @SerializedName("tests") val tests: List<IndexEntry>
)
