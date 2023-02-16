package com.example.greedygameassignment.data.model

data class AlbumDetail(
    val album: Album
){
    data class Album(
        val name:String?,
        val artist:String?,
        val tags:Tags.TopTags?,
        val image: List<Albums.ImageApi>?,
        val wiki: TagDetail.Wiki?
    )
}