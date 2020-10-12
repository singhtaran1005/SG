package com.studgenie.app.ui.main.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.studgenie.app.R
import kotlinx.android.synthetic.main.activity_exo_player.*

class ExoPlayerActivity : AppCompatActivity() {

    private val videoURI: Uri =
        Uri.parse("https://s3.amazonaws.com/_bc_dml/example-content/sintel_dash/sintel_vod.mpd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)

        val player: SimpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        //Binding the player to the view
        player_view.player = player

        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "VideoPlayer")
        )
        val mediaSource =
            DashMediaSource.Factory(dataSourceFactory).createMediaSource(videoURI)
        player.prepare(mediaSource)
        player.playWhenReady = true
    }
}