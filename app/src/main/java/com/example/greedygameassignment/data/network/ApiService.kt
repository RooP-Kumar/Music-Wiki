package com.example.greedygameassignment.data.network

import com.example.greedygameassignment.data.model.*
import com.example.greedygameassignment.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/2.0/")
    suspend fun getTags(
        @Query(value = "method") method:String= "tag.getTopTags",
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<Tags>


    @GET("/2.0/")
    suspend fun getTagDetail(
        @Query(value = "method") method:String= "tag.getinfo",
        @Query(value = "tag") tagName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<TagDetail>

    @GET("/2.0/")
    suspend fun getTopAlbums(
        @Query(value = "method") method:String= "tag.gettopalbums",
        @Query(value = "tag") albumName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<Albums>

    @GET("/2.0/")
    suspend fun getTopArtists(
        @Query(value = "method") method:String= "tag.gettopartists",
        @Query(value = "tag") tagName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<Artists>

    @GET("/2.0/")
    suspend fun getTopTracks(
        @Query(value = "method") method:String= "tag.gettoptracks",
        @Query(value = "tag") tagName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<Tracks>

    @GET("/2.0/")
    suspend fun getAlbumDetail(
        @Query(value = "method") method:String= "album.getinfo",
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "artist") artistName:String ,
        @Query(value = "album") albumName: String,
        @Query(value = "format") format:String = "json"
    ):Response<AlbumDetail>


    @GET("/2.0/")
    suspend fun getArtistDetail(
        @Query(value = "method") method:String= "artist.getinfo",
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "artist") artistName:String ,
        @Query(value = "format") format:String = "json"
    ):Response<ArtistDetail>


    @GET("/2.0/")
    suspend fun getTopTracksByArtist(
        @Query(value = "method") method:String= "artist.gettoptracks",
        @Query(value = "artist") artistName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<TopTracksByArtist>


    @GET("/2.0/")
    suspend fun getTopAlbumsByArtist(
        @Query(value = "method") method:String= "artist.gettopalbums",
        @Query(value = "artist") artistName:String,
        @Query(value="api_key") api_key:String = API_KEY,
        @Query(value = "format") format:String = "json"
    ):Response<TopAlbumsByArtist>

}