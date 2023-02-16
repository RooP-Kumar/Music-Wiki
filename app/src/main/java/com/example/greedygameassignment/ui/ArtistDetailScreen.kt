package com.example.greedygameassignment.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.greedygameassignment.data.model.Albums
import com.example.greedygameassignment.data.model.ArtistDetail
import com.example.greedygameassignment.data.model.Tracks
import com.example.greedygameassignment.features.tags.ui.TagViewModel
import com.example.greedygameassignment.navigation.NavRoutes
import com.example.greedygameassignment.util.GeneralIcon
import com.example.greedygameassignment.util.ProgressStatus
import com.example.greedygameassignment.util.Status

data class ArtistDetailUiState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: MutableState<ArtistDetail.Artist?> = mutableStateOf(null),
)

@Composable
fun ArtistDetailScreen(
    navController: NavHostController,
    artistName: String,
    viewModel: TagViewModel = hiltViewModel())
{
    val uiState = viewModel.artistDetailUiState
    LaunchedEffect(key1 = uiState.status.value){
        when (uiState.status.value) {
            Status.FAILURE -> {
                uiState.progressStatus.value = ProgressStatus.ERROR
            }
            Status.LOADING -> {
                uiState.progressStatus.value = ProgressStatus.LOADING
            }
            Status.SUCCESS -> {
                uiState.progressStatus.value = ProgressStatus.SUCCESS
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (uiState.data.value == null) viewModel.getArtistDetail(artistName)
    }


    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.SUCCESS -> {
                MainUI(uiState, navController, viewModel)
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            ProgressStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Something went wrong.")
                }
                viewModel.getArtistDetail(artistName)
            }
            else -> {}
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainUI(
    uiState: ArtistDetailUiState,
    navController: NavHostController,
    viewModel: TagViewModel)
{
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                        .data(data = uiState.data.value?.image!![3]?.text)
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
                    text = uiState.data.value?.name!!.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    fontSize = 60.sp,
                )
                Spacer(modifier = Modifier.size(30.dp))
                Row(
                    modifier = Modifier.padding(48.dp,0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ){

                    val playCount = (uiState.data.value?.stats?.playcount?.toInt())?.div(1000)
                    val followers = (uiState.data.value?.stats?.listeners?.toInt())?.div(1000)
                    Text(
                        text = "${playCount.toString()}K",
                        style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                        fontSize = 20.sp,
                    )
                    Spacer(modifier =Modifier.weight(1f))
                    Text(
                        text = "${followers.toString()}K",
                        style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                        fontSize = 20.sp,
                    )
                }

                Spacer(modifier = Modifier.size(3.dp))

                Row(
                    modifier = Modifier.padding(48.dp,0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ){

                    Text(
                        text = "PlayCount",
                        style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                        fontSize = 15.sp,
                    )
                    Spacer(modifier =Modifier.weight(1f))
                    Text(
                        text = "Followers",
                        style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                        fontSize = 15.sp,
                    )
                }


            }

        }

        LazyRow(modifier = Modifier.padding(10.dp,10.dp),
            content = {
                val list = uiState.data.value?.tags?.tag!!
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
            text = if (uiState.data.value?.bio!=null) uiState.data.value?.bio!!.summary.toString().substringBefore("<a") else "Sorry No Details Found",
            textAlign = TextAlign.Left,
            style = TextStyle(color = Color.Black, fontSize = 18.sp)
        )

        TopTrackArtistScreen(viewModel,uiState.data.value?.name.toString())

        TopAlbumArtistScreen(viewModel,uiState.data.value?.name.toString(), navController)

    }
}

data class TopAlbumArtistUIState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: SnapshotStateList<Albums.Album> = mutableStateListOf(),
)
@Composable
fun TopAlbumArtistScreen(viewModel: TagViewModel, artistName: String, navController: NavHostController)
{

    val uiState = viewModel.topAlbumArtistUIState

    LaunchedEffect(key1 = uiState.status.value){
        when (uiState.status.value) {
            Status.FAILURE -> {
                uiState.progressStatus.value = ProgressStatus.ERROR
            }
            Status.LOADING -> {
                uiState.progressStatus.value = ProgressStatus.LOADING
            }
            Status.SUCCESS -> {
                uiState.progressStatus.value = ProgressStatus.SUCCESS
            }
            else -> {}
        }
    }

    @Composable
    fun InnerUI() {
        Column(modifier = Modifier.padding(12.dp,2.dp)){
            Text(text = "Top Albums" , style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 18.sp )
            Box(
                modifier = Modifier.padding(0.dp, 0.dp)
            ) {
                LazyRow(
                    content = {
                        items(uiState.data.size) { index ->
                            Box(
                                Modifier.clickable {
                                    navController.navigate(NavRoutes.AlbumDetail.getRoute(uiState.data[index].artist.name, uiState.data[index].name))
                                }
                            ) {
                                val imageData =
                                    if (uiState.data[index].image.isNotEmpty()) uiState.data[index].image[3].text else R.drawable.broken_image_24
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageData)
                                            .placeholder(R.drawable.ic_launcher_foreground)
                                            .crossfade(true)
                                            .build()
                                    ),
                                    contentScale = ContentScale.FillHeight,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .alpha(0.6f)

                                )
                                Column(
                                    modifier = Modifier
                                        .padding(1.dp,2.dp)
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        modifier = Modifier.width(200.dp),
                                        text = uiState.data[index].name,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        ),
                                        fontSize = 16.sp,
                                    )
                                    Text(
                                        text = uiState.data[index].artist.name,
                                        style = TextStyle(textAlign = TextAlign.Center),
                                        fontSize = 16.sp,
                                    )

                                }


                            }
                        }
                    })
            }

        }
    }

    LaunchedEffect(key1 = Unit) {
        if (uiState.data.size == 0) viewModel.getTopAlbumByArtist(artistName)
    }

    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.SUCCESS -> {
                InnerUI()
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            ProgressStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Something went wrong.")
                }
                viewModel.getArtistDetail(artistName)
            }
            else -> {}
        }
    }

}


data class TopTrackArtistUIState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: SnapshotStateList<Tracks.Track> = mutableStateListOf(),
)
@Composable
fun TopTrackArtistScreen(
    viewModel: TagViewModel,
    artistName: String
) {

    val uiState = viewModel.topTrackArtistUIState

    LaunchedEffect(key1 = uiState.status.value){
        when (uiState.status.value) {
            Status.FAILURE -> {
                uiState.progressStatus.value = ProgressStatus.ERROR
            }
            Status.LOADING -> {
                uiState.progressStatus.value = ProgressStatus.LOADING
            }
            Status.SUCCESS -> {
                uiState.progressStatus.value = ProgressStatus.SUCCESS
            }
            else -> {}
        }
    }

    @Composable
    fun InnerUI() {
        Column(modifier = Modifier.padding(12.dp,2.dp)) {
            Text(text = "Top Tracks" , style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 18.sp )
            Box(
                modifier = Modifier.padding(0.dp, 0.dp)
            ) {
                LazyRow(
                    content = {
                        items(uiState.data.size) { index ->
                            Box() {
                                val imageData =
                                    if (uiState.data[index].image.isNotEmpty()) uiState.data[index].image[3].text else R.drawable.broken_image_24
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageData)
                                            .placeholder(R.drawable.ic_launcher_foreground)
                                            .crossfade(true)
                                            .build()
                                    ),
                                    contentScale = ContentScale.FillHeight,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .alpha(0.6f)

                                )
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        modifier = Modifier.width(200.dp),
                                        text = uiState.data[index].name,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        ),
                                        fontSize = 16.sp,
                                    )
                                    Text(
                                        text = uiState.data[index].artist.name,
                                        style = TextStyle(textAlign = TextAlign.Center),
                                        fontSize = 16.sp,
                                    )

                                }


                            }
                        }
                    })
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (uiState.data.size == 0) viewModel.getTopTrackByArtist(artistName)
    }

    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.SUCCESS -> {
                InnerUI()
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            ProgressStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Something went wrong.")
                }
                viewModel.getArtistDetail(artistName)
            }
            else -> {}
        }
    }

}
