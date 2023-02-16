package com.example.greedygameassignment.features.tags.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.greedygameassignment.R
import com.example.greedygameassignment.data.model.Tags
import com.example.greedygameassignment.navigation.NavRoutes
import com.example.greedygameassignment.util.ProgressStatus
import com.example.greedygameassignment.util.Status


data class TagUIState(
    val status: MutableState<Status> = mutableStateOf(Status.IDLE),
    val progressStatus: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.IDLE),
    val data: SnapshotStateList<Tags.Tag> = mutableStateListOf(),
    val isExpended : MutableState<Boolean> = mutableStateOf(false),
    val dataList: SnapshotStateList<Tags.Tag> = mutableStateListOf(),
    val iconValue: MutableState<Int> = mutableStateOf(R.drawable.arrow_down)
)

@Composable
fun TagScreen(
    navController: NavHostController,
    viewModel: TagViewModel = hiltViewModel()
) {
    val uiState = viewModel.tagUIState
    LaunchedEffect(key1 = uiState.status.value){
        when (uiState.status.value) {
            Status.FAILURE -> {
                uiState.progressStatus.value = ProgressStatus.IDLE
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

    LaunchedEffect(key1 = Unit){
        if(uiState.data.size == 0) {
            viewModel.getTags()
        }
    }

    LaunchedEffect(key1 = uiState.isExpended.value) {
        viewModel.expandList()
    }

    Crossfade(targetState = uiState.progressStatus.value) {
        when(it) {
            ProgressStatus.SUCCESS -> {
                MainUI(uiState, navController)
            }
            ProgressStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            ProgressStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "There is something")
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainUI(uiState: TagUIState, navController: NavHostController) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .padding(24.dp, 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(24.dp, 34.dp),
            text = "musicwiki",
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 40.sp)
        )
        Text(
            modifier = Modifier.padding(24.dp, 14.dp),
            text = "Welcome!",
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 20.sp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Choose a genre to start with",
                textAlign = TextAlign.Center,
                style = TextStyle(color = Color.Black, fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            OutlinedButton(
                onClick = {

                    uiState.isExpended.value = !uiState.isExpended.value

                },
                modifier = Modifier.size(20.dp),  //avoid the oval shape
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.Black),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = uiState.iconValue.value),
                    contentDescription = null
                )
            }


        }
        
        LazyVerticalGrid(
            modifier = Modifier.padding(0.dp,24.dp),
            columns = GridCells.Adaptive(110.dp),
            contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp),
            content ={
                items(uiState.dataList.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp),
                        elevation = 8.dp,
                        border = BorderStroke(1.dp,Color.Gray),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            navController.navigate(NavRoutes.TagDetail.getRoute(uiState.dataList[index].name.toString()))
                        }
                    ) {
                        Text(
                            text = uiState.dataList[index].name.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF4f4f4f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }} )

    }
}