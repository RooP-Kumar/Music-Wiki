package com.example.greedygameassignment.data.model

import com.squareup.moshi.Json

data class Artists (
    @Json(name = "topartists")
    val topArtists: TopArtists
        ){
    data class TopArtists(
        val artist: List<Artist>
    )
    data class Artist(
        val name:String,
        val image : List<Albums.ImageApi>
    )
}