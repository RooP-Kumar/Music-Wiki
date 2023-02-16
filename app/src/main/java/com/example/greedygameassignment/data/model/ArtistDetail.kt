package com.example.greedygameassignment.data.model

data class ArtistDetail(
    val artist:Artist
){
    data class Artist(
        val name:String?,
        val image : List<Albums.ImageApi?>,
        val stats : Stats?,
        val tags:Tags.TopTags?,
        val bio: TagDetail.Wiki?
    )
    data class Stats(
        val listeners:String?,
        val playcount : String?
    )
}
