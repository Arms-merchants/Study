package com.arm.hetao

import android.os.Bundle
import androidx.core.view.isVisible
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 *    author : heyueyang
 *    time   : 2022/02/17
 *    desc   :
 *    version: 1.0
 */
class PlayActivity : GSYBaseActivityDetail<StandardGSYVideoPlayer>() {

    private lateinit var player: StandardGSYVideoPlayer
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        player = findViewById(R.id.player)
        player.titleTextView.isVisible = false
        url = intent.getStringExtra("URL") ?: ""
        initVideoBuilderMode()
        player.backButton.setOnClickListener {
            finish()
        }
        player.startPlayLogic()
    }

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
        return player
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        return GSYVideoOptionBuilder()
            .setUrl(url)
            .setAutoFullWithSize(true)
            .setCacheWithPlay(false)
            .setLockLand(true)
            .setStartAfterPrepared(true)
    }

    override fun clickForFullScreen() {
    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true
    }
}