package com.arm.composechat.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.arm.composechat.extend.navigateWithBack
import com.arm.composechat.logic.LoginViewModel
import com.arm.composechat.model.Screen
import com.arm.composechat.ui.weigets.CommonSnackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.arm.composechat.R
import com.arm.composechat.ui.weigets.CommonButton
import com.arm.composechat.ui.weigets.CommonOutlinedTextField
import com.arm.composechat.utils.log

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */

@Composable
fun LoginScreen(navController: NavHostController) {
    val loginViewModel = viewModel<LoginViewModel>()
    val loginScreenState by loginViewModel.loginScreenState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val textInputService = LocalTextInputService.current
    LaunchedEffect(key1 = Unit) {
        launch {
            loginViewModel.loginScreenState.collect {
                if (it.loginSuccess) {
                    navController.navigateWithBack(
                        screen = Screen.HomeScreen
                    )
                    return@collect
                }
            }
        }
        loginViewModel.autoLogin()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = {
            CommonSnackbar(state = it)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (loginScreenState.showLogo) {
                    log(log = "showLogo")
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Green)
                            .fillMaxHeight(fraction = 0.4f)
                            .wrapContentSize(align = Alignment.Center),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 34.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                }
                if (loginScreenState.showInput) {
                    //这里的userId就是状态提升的一个概念，因为输入框没修改一层都要触发重组操作
                    var userId by remember { mutableStateOf(loginScreenState.lastLoginUserId) }
                    log(log = "showInput")
                    CommonOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        value = userId,
                        label = "UserId",
                        onValueChange = { value ->
                            val realValue = value.trimStart().trimEnd()
                            if (realValue.length <= 12 &&
                                //通过判断是否大小写来限定英文输入
                                realValue.all { it.isLowerCase() || it.isUpperCase() }) {
                                userId = realValue
                            }
                        },
                        maxLines = 1,
                        singleLine = true
                    )
                    CommonButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp, top = 30.dp),
                        text = "登陆"
                    ) {
                        if (userId.isBlank()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(message = "请输入 UserId")
                            }
                        } else {
                            textInputService?.hideSoftwareKeyboard()
                            loginViewModel.goToLogin(userId = userId)
                        }
                    }
                }
            }
            if (loginScreenState.showLoading) {
                log(log = "showLoading")
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(size = 60.dp)
                        .wrapContentSize(align = Alignment.Center),
                    strokeWidth = 4.dp
                )
            }
        }

    }

}