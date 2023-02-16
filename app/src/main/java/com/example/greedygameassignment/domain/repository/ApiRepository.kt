package com.example.greedygameassignment.domain.repository


import com.example.greedygameassignment.data.model.*
import com.example.greedygameassignment.data.network.DataApi
import com.example.greedygameassignment.data.response.Resourse
import com.example.greedygameassignment.data.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: DataApi
) {

    suspend fun getTags() : Resourse<Response<Tags>> {
        return safeApiCall {
            api.getTags()
        }
    }

    suspend fun getTagDetail(name: String) : Resourse<Response<TagDetail>> {
        return safeApiCall {
            api.getTagDetail(name)
        }
    }

    suspend fun getTopAlbums(albumName: String) : Resourse<Response<Albums>> {
        return safeApiCall {
            api.getTopAlbums(albumName)
        }
    }

    suspend fun getTopArtists(artistName: String) : Resourse<Response<Artists>> {
        return safeApiCall {
            api.getTopArtists(artistName)
        }
    }

    suspend fun getTopTracks(trackName: String) : Resourse<Response<Tracks>> {
        return safeApiCall {
            api.getTopTracks(trackName)
        }
    }

    suspend fun getAlbumDetail(artistName: String, albumName: String) : Resourse<Response<AlbumDetail>> {
        return safeApiCall {
            api.getAlbumDetail(artistName, albumName)
        }
    }

    suspend fun getArtistDetail(artistName: String) : Resourse<Response<ArtistDetail>> {
        return safeApiCall {
            api.getArtistDetail(artistName)
        }
    }

    suspend fun getTopTracksByArtist(artistName: String) : Resourse<Response<TopTracksByArtist>> {
        return safeApiCall {
            api.getTopTracksByArtist(artistName)
        }
    }

    suspend fun getTopAlbumsByArtist(artistName: String) : Resourse<Response<TopAlbumsByArtist>> {
        return safeApiCall {
            api.getTopAlbumsByArtist(artistName)
        }
    }

}

suspend fun <T> safeApiCall(
    apiCall: suspend() -> Response<T>
): Resourse<Response<T>>  {
    return withContext(Dispatchers.IO) {
        val response = apiCall()
        if (response.status) Resourse.SUCCESS(response)
        else Resourse.FAILURE(response.msg, "")
    }
}