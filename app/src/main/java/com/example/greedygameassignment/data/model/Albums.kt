package com.example.greedygameassignment.data.model

import com.squareup.moshi.Json

data class Albums(
    val albums : TopAlbums
){
    data class TopAlbums(
        val album:List<Album>
    )
    data class Album(
        val name: String,
        val artist:Artist,
        val image : List<ImageApi>
    )

    data class Artist(
        val name:String
    )
    data class ImageApi(
        @Json(name="#text")
        val text:String
    )
}
