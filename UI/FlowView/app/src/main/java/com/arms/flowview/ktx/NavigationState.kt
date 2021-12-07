package com.arms.flowview.ktx

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */
sealed class NavigationState {

    object EmptyNavigationState : NavigationState()

    object ViewNavigationState : NavigationState()

}