package com.example.greedygameassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.greedygameassignment.features.tags.ui.TagDetailScreen
import com.example.greedygameassignment.features.tags.ui.TagScreen
import com.example.greedygameassignment.ui.AlbumDetailScreen
import com.example.greedygameassignment.ui.ArtistDetailScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination =  NavRoutes.Tag.route){
        composable(NavRoutes.Tag.route){
            TagScreen(navController = navController)
        }

        composable(route =NavRoutes.TagDetail.route,
        arguments = listOf(navArgument("TAG_NAME") { type = NavType.StringType })
        ){
            val arg = it.arguments?.getString("TAG_NAME")
            val tagName = arg.toString()
           TagDetailScreen(navController = navController,tagName)
        }

        composable(route = NavRoutes.AlbumDetail.route,
        arguments = listOf(
            navArgument("ARTIST_NAME"){type =  NavType.StringType},
            navArgument("ALBUM_NAME"){type = NavType.StringType}
        )){
            val albumName = it.arguments?.getString("ALBUM_NAME").toString()
            val artistName = it.arguments?.getString("ARTIST_NAME").toString()
            AlbumDetailScreen(
                navController = navController,
                artistName =artistName ,
                albumName = albumName )
        }


        composable(route = NavRoutes.ArtistDetail.route,
            arguments = listOf(
                navArgument("ARTIST_NAME"){type =  NavType.StringType}
            )){
            val artistName = it.arguments?.getString("ARTIST_NAME").toString()
            ArtistDetailScreen(
                navController = navController,
                artistName =artistName)
        }
    }
}