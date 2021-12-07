package com.arms.flowview.ktx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */
class NewsRemoteDateSource(private val externalScope: CoroutineScope) {

    val lastestNews: Flow<String> = flow {
        emit("1")
    }.shareIn(externalScope, replay = 1, started = SharingStarted.WhileSubscribed())
}