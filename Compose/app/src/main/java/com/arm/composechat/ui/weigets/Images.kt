package com.arm.composechat.ui.weigets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.Coil
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.arm.composechat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */

@Preview
@Composable
fun test() {
    Column {
        CoilImage(
            Modifier.size(44.dp),
            R.drawable.ic_crane_logo
        )

        OutLineAvatar(data = "")
    }

}

object CoilImageLoader {

    fun init(context: Context) {
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(false)
            .allowHardware(false)
            .placeholder(R.drawable.icon_logo_round)
            .fallback(R.drawable.icon_logo_round)
            .error(R.drawable.icon_logo_round)
            .build()
        Coil.setImageLoader(imageLoader = imageLoader)
    }
}

@Composable
fun CoilImage(
    modifier: Modifier = Modifier,
    data: Any,
    contentScale: ContentScale = ContentScale.Crop,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    val imagePainter = rememberImagePainter(data = data, builder = builder)
    Image(
        modifier = modifier,
        painter = imagePainter,
        contentDescription = null,
        contentScale = contentScale
    )
}

@Composable
fun CoilCircleImage(
    modifier: Modifier = Modifier,
    data: Any
) {
    CoilImage(data = data,
        modifier = modifier.clip(shape = CircleShape),
        builder = {

        }
    )
}

@Composable
fun OutLineAvatar(
    modifier: Modifier = Modifier,
    data: String,
    outlineSize: Dp = 4.dp,
    outlineColor: Color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f)
) {
    Box(
        modifier = modifier.background(
            color = outlineColor,
            shape = CircleShape
        ),
        contentAlignment = Alignment.Center
    ) {
        CoilCircleImage(
            data = data,
            modifier = Modifier
                .padding(outlineSize)
                .fillMaxSize()
        )
    }
}


