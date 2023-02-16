package com.example.greedygameassignment.features.tags.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greedygameassignment.R
import com.example.greedygameassignment.data.response.Resourse
import com.example.greedygameassignment.domain.repository.ApiRepository
import com.example.greedygameassignment.ui.AlbumDetailUIState
import com.example.greedygameassignment.ui.ArtistDetailUiState
import com.example.greedygameassignment.ui.TopAlbumArtistUIState
import com.example.greedygameassignment.ui.TopTrackArtistUIState
import com.example.greedygameassignment.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel(){

    val albumDetailUIState by lazy { AlbumDetailUIState() }
    val tagUIState by lazy { TagUIState() }
    val tagDetailUIState by lazy { TagDetailUIState() }
    val albumUIState by lazy { AlbumUIState() }
    val topTrackUiState by lazy { TopTrackUiState() }
    val topArtistUiState by lazy { TopArtistUiState() }
    val artistDetailUiState by lazy { ArtistDetailUiState() }
    val topAlbumArtistUIState by lazy { TopAlbumArtistUIState() }
    val topTrackArtistUIState by lazy { TopTrackArtistUIState() }

    fun getTags() {
        tagUIState.apply {
            status.value = Status.LOADING
            viewModelScope.launch {
                when(val res = repository.getTags()){
                    is Resourse.SUCCESS -> {
                        data.addAll(res.value.document.toptags?.tag!!)
                        dataList.addAll(data.toList().subList(0, 10))
                        status.value = Status.SUCCESS
                    }

                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }
    }

    fun getTopAlbumByArtist(artistName: String){
        viewModelScope.launch {
            topAlbumArtistUIState.apply {
                status.value = Status.LOADING
                when(val res = repository.getTopAlbumsByArtist(artistName)){
                    is Resourse.SUCCESS -> {
                        data.addAll(res.value.document.topalbums.album)
                        status.value = Status.SUCCESS
                    }
                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }

    }

    fun getTopTrackByArtist(artistName: String){
        viewModelScope.launch {
            topTrackArtistUIState.apply {
                status.value = Status.LOADING
                when(val res = repository.getTopTracksByArtist(artistName)){
                    is Resourse.SUCCESS -> {
                        data.addAll(res.value.document.toptracks.track)
                        status.value = Status.SUCCESS
                    }
                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }

        }

    }

    fun getTopTrack(tagName:String){
        viewModelScope.launch(Dispatchers.IO) {
            topTrackUiState.apply {
                status.value = Status.LOADING
                when(val res = repository.getTopTracks(tagName)){
                    is Resourse.SUCCESS -> {
                        data.addAll(res.value.document.tracks.track)
                        status.value = Status.SUCCESS
                    }
                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }
    }

    fun getTopArtist(tagName:String){
        viewModelScope.launch {
            topArtistUiState.apply {
                status.value = Status.LOADING
                when(val res = repository.getTopArtists(tagName)){
                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                    is Resourse.SUCCESS -> {
                        data.addAll(res.value.document.topArtists.artist)
                        status.value = Status.SUCCESS

                    }
                }
            }

        }
    }

    fun getTopAlbum(albumName:String){
        albumUIState.apply {
            status.value = Status.LOADING
            viewModelScope.launch(Dispatchers.IO) {
                when(val res = repository.getTopAlbums(albumName)){
                    is Resourse.SUCCESS -> {
                        data.value = res.value.document.albums
                        status.value = Status.SUCCESS
                    }

                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }
    }

    fun getAlbumDetail(albumName: String,artistName:String){
        albumDetailUIState.apply {
            status.value = Status.LOADING
            viewModelScope.launch(Dispatchers.IO) {
                when(val res = repository.getAlbumDetail(artistName, albumName)){
                    is Resourse.SUCCESS -> {
                        data.value = res.value.document.album
                        status.value = Status.SUCCESS
                    }

                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }

    }

    fun getArtistDetail(artistName:String){
        viewModelScope.launch {
            artistDetailUiState.apply {
                status.value = Status.LOADING
                when(val res = repository.getArtistDetail(artistName)){
                    is Resourse.SUCCESS -> {
                        data.value = res.value.document.artist
                        status.value = Status.SUCCESS
                    }
                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }
    }

    fun getTagDetail(tagName:String){
        tagDetailUIState.apply {
            viewModelScope.launch {
                when(val res = repository.getTagDetail(tagName)){
                    is Resourse.SUCCESS -> {
                        data.value = res.value.document.tag
                        status.value = Status.SUCCESS
                    }

                    is Resourse.FAILURE -> {
                        status.value = Status.FAILURE
                    }
                }
            }
        }
    }

    fun expandList() {
        tagUIState.apply {
            if (isExpended.value){
                dataList.clear()
                dataList.addAll(data)
                iconValue.value = R.drawable.arrow_up
            } else {
                dataList.clear()
                if(data.size != 0) dataList.addAll(data.toList().subList(0, 10))
                iconValue.value = R.drawable.arrow_down
            }
        }
    }
}
