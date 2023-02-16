package com.example.greedygameassignment.data.response

data class Response<T>(
    var document : T,
    var status: Boolean = false,
    var msg : String = ""
)
