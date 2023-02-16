package com.example.greedygameassignment.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GeneralIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    iconSize: Dp = 24.dp,
    iconTint: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    val padding = (36.dp - iconSize) / 2
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(36.dp)
            .padding(padding)
            .background(color = backgroundColor, shape = CircleShape)
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            tint = iconTint,
            contentDescription = null
        )
    }
}