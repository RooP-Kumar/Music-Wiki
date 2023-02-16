package com.example.greedygameassignment.data.model

data class Tags(
    val toptags:TopTags?

){
    data class TopTags(
        val tag:List<Tag>
    )
    data class Tag(
        val name: String?,
        val count: Int?,
        val reach : Int?,
    )


}
