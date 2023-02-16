package com.example.greedygameassignment.util

object Constants {
    val BASE_URL = "https://ws.audioscrobbler.com"
    val API_KEY = "1a424d1a211a65f63bf84c0ab7adc935"
    val SHARED_KEY = "e9e73be2dc6b15f6140cdfc941b44e9e"
}


enum class Status {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class ProgressStatus{
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}