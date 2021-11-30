package com.arm.composechat.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.arm.composechat.logic.AppViewModel
import com.arm.composechat.model.AppTheme
import com.arm.composechat.model.Screen
import com.arm.composechat.ui.login.LoginScreen
import com.arm.composechat.ui.theme.ChatTheme
import com.arm.composechat.ui.weigets.SetSystemBarsColor
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.material.navigation.NavigationView


/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */
class HomeActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val appViewModel = viewModel<AppViewModel>()
            val appTheme by appViewModel.appTheme.collectAsState()
            val switchToNextTheme = remember {
                {
                    appViewModel.switchToNextTheme()
                }
            }

            val navController = rememberAnimatedNavController()
            ChatTheme(appTheme = appTheme) {
                SetSystemBarsColor(
                    statusBarColor = Color.Transparent,
                    navigationBarColor = MaterialTheme.colors.background,
                    statusBarDarkIcons = appTheme == AppTheme.Light,
                    navigationDarkIcons = appTheme != AppTheme.Dark
                )
                NavigationView(
                    navController = navController,
                    appTheme = appTheme,
                    switchToNextTheme = switchToNextTheme
                )
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    private fun NavigationView(
        navController: NavHostController,
        appTheme: AppTheme,
        switchToNextTheme: () -> Unit
    ) {
        ProvideWindowInsets {
            AnimatedNavHost(navController = navController,
                startDestination = Screen.LoginScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = {
                            -it
                        },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = {
                            it
                        },
                        animationSpec = tween(300)
                    )
                }) {
                animatedComposable(
                    screen = Screen.LoginScreen,
                ) {
                    LoginScreen(navController = navController)
                }
            }
        }

    }

    @ExperimentalAnimationApi
    private fun NavGraphBuilder.animatedComposable(
        screen: Screen,
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
    ) {
        composable(
            route = screen.route
        ) { backStackEntry ->
            content(backStackEntry)
        }
    }

}