package com.example.greedygameassignment.data.network

import com.example.greedygameassignment.data.model.*
import com.example.greedygameassignment.data.response.Response
import javax.inject.Inject

class DataApi @Inject constructor(private val service: ApiService) {

    suspend fun getTags() : Response<Tags> {
        val response = Response(document = Tags(Tags.TopTags(listOf())))
        val res = service.getTags()
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTagDetail(tagName: String) : Response<TagDetail>{
        val response = Response(document = TagDetail(TagDetail.Tag("", 0, 0, TagDetail.Wiki("", ""))))
        val res = service.getTagDetail(tagName = tagName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTopAlbums(albumName: String) : Response<Albums> {
        val response = Response(document = Albums(Albums.TopAlbums(listOf())))
        val res = service.getTopAlbums(albumName = albumName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTopArtists(artistName: String) : Response<Artists>{
        val response = Response(document = Artists(Artists.TopArtists(listOf())))
        val res = service.getTopArtists(tagName = artistName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTopTracks(trackName: String) : Response<Tracks> {
        val response = Response(document = Tracks(Tracks.TopTracks(listOf())))
        val res = service.getTopTracks(tagName = trackName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getAlbumDetail(artistName: String, albumName: String) : Response<AlbumDetail> {
        val response = Response(document = AlbumDetail(AlbumDetail.Album("","",Tags.TopTags(listOf()), listOf(),TagDetail.Wiki("",""))))
        val res = service.getAlbumDetail(artistName = artistName, albumName = albumName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getArtistDetail(artistName: String) : Response<ArtistDetail> {
        val response = Response(document = ArtistDetail(ArtistDetail.Artist("", listOf(),ArtistDetail.Stats("", ""), Tags.TopTags(listOf()), TagDetail.Wiki("", ""))))
        val res = service.getArtistDetail(artistName = artistName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTopTracksByArtist(artistName: String) : Response<TopTracksByArtist> {
        val response = Response(document = TopTracksByArtist(Tracks.TopTracks(listOf())))
        val res = service.getTopTracksByArtist(artistName = artistName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }

    suspend fun getTopAlbumsByArtist(artistName: String) : Response<TopAlbumsByArtist> {
        val response = Response(document = TopAlbumsByArtist(Albums.TopAlbums(listOf())))
        val res = service.getTopAlbumsByArtist(artistName = artistName)
        if (res.isSuccessful){
            response.document = res.body()!!
            response.status = true
            response.msg = "Success"
        } else {
            response.status = false
            response.msg = "Failed"
        }
        return response
    }
}