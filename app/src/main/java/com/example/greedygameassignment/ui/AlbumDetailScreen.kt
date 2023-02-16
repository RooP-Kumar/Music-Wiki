package com.example.greedygameassignment.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.greedygameassignment.R
import com.example.greedygameassignment.data.model.AlbumDetail
import com.example.greedygameassignment.data.model.TagDetail
import com.example.greedygameassignment.data.model.Tags
import com.example.greedygameassignment.features.tags.ui.TagViewModel
import com.example.greedygameassignment.navigation.NavRoutes
import com.example.greedygameassignment.util.GeneralIcon
import com.example.greedygameassignment.util.ProgressStatus
import com.example.greedygameassignment.util.Status

data class AlbumDetailUIState(
    val status: MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val flag: MutableState<Boolean> = mutableStateOf(false),
    val data: MutableState<AlbumDetail.Album> = mutableStateOf(AlbumDetail.Album(
        "",
        "",
        Tags.TopTags(listOf()),
        listOf(),
        TagDetail.Wiki("","")
    )),
)

@Composable
fun AlbumDetailScreen(
    navController: NavHostController,
    artistName: String,
    albumName  : String,
    viewModel: TagViewModel = hiltViewModel()
){
    val uiState = viewModel.albumDetailUIState

    LaunchedEffect(key1 = uiState.status.value) {
        when(uiState.status.value) {
            Status.LOADING -> {
                uiState.progressStatus.value = ProgressStatus.LOADING
            }
            Status.SUCCESS -> {
                uiState.progressStatus.value = ProgressStatus.SUCCESS
            }
            Status.FAILURE -> {
                uiState.progressStatus.value = ProgressStatus.ERROR
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!uiState.flag.value) {
            viewModel.getAlbumDetail(albumName, artistName)
        }
    }

    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.ERROR -> {
                uiState.flag.value = true
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "res.error")
                }
            }
            ProgressStatus.SUCCESS -> {
                uiState.flag.value = true
                MainUI(uiState.data.value, navController)
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {}
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainUI(
    data: AlbumDetail.Album,
    navController: NavHostController
)
{
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ){
            GeneralIcon(
                modifier = Modifier
                    .zIndex(4f)
                    .padding(16.dp),
                imageVector = Icons.Filled.ArrowBack,
                onClick = { navController.navigateUp() }
            )
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = data.image?.get(3)?.text)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .crossfade(true)
                        .build()
                ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.6f)

            )
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = data.name.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    fontSize = 20.sp,
                )
                Text(
                    text = data.artist.toString(),
                    style = TextStyle(textAlign = TextAlign.Center),
                    fontSize = 16.sp,
                )

            }

        }

        LazyRow(modifier = Modifier.padding(10.dp,10.dp),
            content = {
                val list = data.tags?.tag!!
                items(list.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(2.dp, 0.dp)
                            .width(130.dp),
                        elevation = 8.dp,
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(20.dp),
                        onClick = {
                            navController.navigate(NavRoutes.TagDetail.getRoute(list[index].name.toString())){
                                popUpTo(navController.graph.findStartDestination().id)
                        }
                        }
                    ) {
                        Text(
                            text = list[index].name!!,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
            }
        )

        Text(
            modifier = Modifier.padding(10.dp,10.dp),
            text = if (data.wiki!=null) data.wiki.summary.toString().substringBefore("<a") else "Sorry No Details Found",
            textAlign = TextAlign.Left,
            style = TextStyle(color = Color.Black, fontSize = 18.sp)
        )



}}