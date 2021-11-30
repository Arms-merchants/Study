package com.arm.composechat.ui.weigets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.imePadding

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */

@Preview(showBackground = true)
@Composable
fun showTest() {
    Column {
        Text(text = "123123")
        CommonDivider()
        Text(text = "423424")
        CommonButton(text = "1231") {

        }
        CommonOutlinedTextField(
            modifier = Modifier,
            value = "test",
            label = "123",
        ) {

        }
        EmptyView()
    }

}

/**
 * 分割线
 */
@Composable
fun CommonDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.colors.secondary
    )
}

/**
 * 按钮
 */
@Composable
fun CommonButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp), text: String, onClick: () -> Unit
) {
    Button(modifier = modifier, onClick = {
        onClick()
    }) {
        Text(text = text)
    }
}

/**
 * 文本输入框
 */
@Composable
fun CommonOutlinedTextField(
    modifier: Modifier,
    value: String,
    label: String,
    singleLine: Boolean = false,
    maxLines: Int = 4,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = {
            Text(text = label)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
        )
    )
}

@Composable
fun CommonSnackbar(state: SnackbarHostState) {
    SnackbarHost(hostState = state) { data ->
        Snackbar(
            modifier = Modifier.imePadding(),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 0.dp,
            content = {
                Text(text = data.message, color = Color.White)
            }
        )
    }
}

@Composable
fun EmptyView() {
    Text(
        text = "Empty",
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .background(color = Color.Green)
        ,
        style = MaterialTheme.typography.subtitle2,
        fontWeight = FontWeight.Bold,
        fontSize = 49.sp,
    )
}




