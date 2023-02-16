package com.example.greedygameassignment.data.model

data class Tracks(
    val tracks : TopTracks
){
    data class TopTracks(
        val track : List<Track>
    )

    data class Track(
        val name : String,
        val artist: Albums.Artist,
        val image : List<Albums.ImageApi>
    )
}
