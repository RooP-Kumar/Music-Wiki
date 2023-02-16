package com.example.greedygameassignment.features.tags.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.greedygameassignment.R
import com.example.greedygameassignment.data.model.Albums
import com.example.greedygameassignment.data.model.Artists
import com.example.greedygameassignment.data.model.TagDetail
import com.example.greedygameassignment.data.model.Tracks
import com.example.greedygameassignment.navigation.NavRoutes
import com.example.greedygameassignment.util.GeneralIcon
import com.example.greedygameassignment.util.ProgressStatus
import com.example.greedygameassignment.util.Status
import com.example.greedygameassignment.util.TabRowItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

data class TagDetailUIState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: MutableState<TagDetail.Tag?> = mutableStateOf(null),
)

@Composable
fun TagDetailScreen(
    navController: NavHostController,
    tagName: String,
    viewModel: TagViewModel = hiltViewModel()
) {
    val uiState = viewModel.tagDetailUIState
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
        if (uiState.data.value == null) viewModel.getTagDetail(tagName)
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
                viewModel.getTagDetail(tagName)
            }
            else -> {}
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainUI(
    uiState: TagDetailUIState,
    navController: NavHostController,
    viewModel: TagViewModel
) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabRowItems = listOf(
        TabRowItem(
            title = "ALBUMS",
            screen = { TopAlbumScreen(navController,viewModel, uiState.data.value?.name.toString()) }
        ),
        TabRowItem(
            title = "ARTISTS",
            screen = { TopArtistScreen(navController,viewModel,uiState.data.value?.name.toString()) }
        ),
        TabRowItem(
            title = "TRACKS",
            screen = { TopTrackScreen(viewModel,uiState.data.value?.name.toString()) }
        )

    )

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
    ) {
        GeneralIcon(
            modifier = Modifier
                .zIndex(4f)
                .padding(16.dp),
            imageVector = Icons.Filled.ArrowBack,
            onClick = { navController.navigateUp() }
        )

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(50.dp))

            Text(
                text = uiState.data.value?.name.toString(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 60.sp
                )
            )

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = uiState.data.value?.wiki!!.summary.toString().substringBefore("<a"),
                textAlign = TextAlign.Left,
                style = TextStyle(color = Color.Black, fontSize = 18.sp)
            )

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                        color = MaterialTheme.colors.primary,
                        height = 2.dp

                    )
                },
            ) {
                tabRowItems.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        icon = {},
                        text = {
                            Text(
                                text = item.title,
                                color = if (pagerState.currentPage == index) Color.Black else Color.LightGray
                            )
                        }
                    )
                }

            }
            HorizontalPager(
                count = tabRowItems.size,
                state = pagerState,
            ) {
                tabRowItems[pagerState.currentPage].screen()
            }
        }
    }
}

// Top Tag Screen
data class TopTrackUiState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: SnapshotStateList<Tracks.Track> = mutableStateListOf(),
)

@Composable
fun TopTrackScreen(viewModel: TagViewModel, tag: String) {

    val uiState = viewModel.topTrackUiState

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
        if (uiState.data.size == 0) viewModel.getTopTrack(tag)
    }


    @Composable
    fun InnerUI() {
        Box(
            modifier = Modifier.padding(0.dp, 20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
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
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = uiState.data[index].name,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontSize = 20.sp,
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
                viewModel.getTopTrack(tag)
            }
            else -> {}
        }
    }
}

// Top Artist Screen
data class TopArtistUiState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: SnapshotStateList<Artists.Artist> = mutableStateListOf(),
)


@Composable
fun TopArtistScreen(navController: NavHostController, viewModel: TagViewModel, tag: String)
{

    val uiState = viewModel.topArtistUiState
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
        if (uiState.data.size == 0) viewModel.getTopArtist(tag)
    }

    @Composable
    fun InnerUI() {
        Box(
            modifier = Modifier.padding(0.dp, 20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                content = {
                    items(uiState.data.size) { index ->
                        Box(modifier = Modifier.clickable {
                            navController.navigate(NavRoutes.ArtistDetail.getRoute(
                                uiState.data[index].name))
                        }) {
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
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = uiState.data[index].name,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontSize = 20.sp,
                                )


                            }


                        }
                    }
                })
        }
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
                viewModel.getTopArtist(tag)
            }
            else -> {}
        }
    }
}

// Album Screen
data class AlbumUIState(
    val status : MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: MutableState<Albums.TopAlbums?> = mutableStateOf(null),
)

@Composable
fun TopAlbumScreen(
    navController: NavHostController,
    viewModel: TagViewModel,
    tag: String
) {

    val uiState = viewModel.albumUIState
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
        if (uiState.data.value == null) viewModel.getTopAlbum(tag)
    }


    @Composable
    fun AlbumScreenUi() {
        Box(
            modifier = Modifier.padding(0.dp, 20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                content = {
                    items(uiState.data.value?.album!!) { album ->
                        Box(
                            modifier = Modifier.clickable {
                                navController.navigate(NavRoutes.AlbumDetail.getRoute(
                                    album.artist.name, album.name))}
                        ) {
                            val imageData =
                                if (album.image.isNotEmpty()) album.image[3].text else R.drawable.broken_image_24
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
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = album.name,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontSize = 20.sp,
                                )
                                Text(
                                    text = album.artist.name,
                                    style = TextStyle(textAlign = TextAlign.Center),
                                    fontSize = 16.sp,
                                )

                            }


                        }
                    }
                })
        }
    }

    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.SUCCESS -> {
                AlbumScreenUi()
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            ProgressStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "something went wrong")
                }
                viewModel.getTopAlbum(tag)
            }
            else -> {}
        }
    }
}



