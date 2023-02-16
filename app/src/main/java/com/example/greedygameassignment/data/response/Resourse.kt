package com.example.greedygameassignment.data.response

sealed class Resourse<out T> {
    data class SUCCESS<out T>(
        val value : T
    ) : Resourse<T>()

    data class FAILURE(
        val message: String,
        val error: String
    ) : Resourse<Nothing>()
}