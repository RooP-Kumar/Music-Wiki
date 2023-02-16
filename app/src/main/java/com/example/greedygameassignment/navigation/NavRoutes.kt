package com.example.greedygameassignment.navigation

sealed class NavRoutes(val route : String) {
    object Tag : NavRoutes("tag")

    object TagDetail : NavRoutes("tagDetail/{TAG_NAME}"){
        fun getRoute(tagName:String): String {
            return "tagDetail/$tagName"
        }
    }

    object AlbumDetail : NavRoutes("albumDetail/{ARTIST_NAME}/{ALBUM_NAME}"){
        fun getRoute(artistName:String,albumName:String):String{
            return "albumDetail/$artistName/$albumName"
        }
    }

    object ArtistDetail : NavRoutes("artistDetail/{ARTIST_NAME}"){
        fun getRoute(artistName:String):String{
            return "artistDetail/$artistName"
        }
    }
}