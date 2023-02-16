package com.example.greedygameassignment.data.model

data class TagDetail(
    val tag:Tag
){
    data class Tag(
        val name: String?,
        val total: Int?,
        val reach : Int?,
        val wiki : Wiki
    )
    data class Wiki(
        val summary : String?,
        val content:String
    )
}

