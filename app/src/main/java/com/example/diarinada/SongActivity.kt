package com.example.diarinada

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import kotlin.properties.Delegates

class SongActivity: AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private var songPlaying = false
    private var menuOpen = false

    private lateinit var playButton: ImageButton
    private lateinit var loopButton: ImageButton
    private lateinit var repeatButton: ImageButton
    private lateinit var menuButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var lyrics: TextView
    private lateinit var navigationView: NavigationView
    private lateinit var navigationController: NavigationViewController
    private lateinit var background: ImageView

    private var index by Delegates.notNull<Int>()
    private lateinit var songs: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_playing)

        index = intent.getIntExtra("index", 0)
        songs = intent.getStringArrayExtra("songs")!!

        playButton = findViewById(R.id.playButton)
        loopButton = findViewById(R.id.loopButton)
        repeatButton = findViewById(R.id.repeatButton)
        menuButton = findViewById(R.id.menuButton)
        nextButton = findViewById(R.id.nextButton)
        seekBar = findViewById(R.id.seekBar)
        lyrics = findViewById(R.id.lyrics)
        background = findViewById(R.id.bgImage)
        navigationView = findViewById(R.id.navigationView)
        navigationController = NavigationViewController(this)
        navigationView.setNavigationItemSelectedListener(navigationController)

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home)
        }

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.song_icon)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true)
        val newDrawable = BitmapDrawable(resources, scaledBitmap)
        seekBar.thumb = newDrawable

        val songId = songs[index]
        // get song from r.raw.songs.<song_id>
        println("Song ID: $songId")
        val song = resources.getIdentifier(songId, "raw", packageName)
        setAssets()
        controlButtons()

        mediaPlayer = MediaPlayer.create(this, song)
        mediaPlayerControl()
        initSeekBar()

        controlSong()
        playButton.performClick()
    }

    private fun setAssets() {
        // get lyrics from strings
        val lyricsId = resources.getIdentifier("lyrics_${index}", "string", packageName)
        val lyricsText = resources.getString(lyricsId)
        lyrics.text = lyricsText

        val songId = songs[index]
        val backgroundId = resources.getIdentifier("${songId}_background", "drawable", packageName)
        background.setImageResource(backgroundId)
        background.scaleType = ImageView.ScaleType.FIT_XY

        val playerBar = findViewById<ImageView>(R.id.playerBar)
        val colorId = resources.getIdentifier("song_${index}", "color", packageName)
        playerBar.setBackgroundResource(colorId)
        }

    private fun mediaPlayerControl() {
        // check if mediaPlayer.isLooping is false,
        // if false, change pause button to play button when song is finished
        mediaPlayer.setOnCompletionListener {
            val button = findViewById<ImageButton>(R.id.playButton)
            if (!mediaPlayer.isLooping) {
                button.setBackgroundResource(R.drawable.play_button_shadow)
                songPlaying = false
            }
        }
    }

    private fun controlSong() {
        mediaPlayer.isLooping = false
        playButton.setOnClickListener {
            val button = findViewById<ImageButton>(R.id.playButton)
            if (songPlaying) {
                mediaPlayer.pause()
                songPlaying = false
                // set button background
                button.setBackgroundResource(R.drawable.play_button_shadow)
            } else {
                mediaPlayer.start()
                songPlaying = true
                button.setBackgroundResource(R.drawable.pause_button_shadow)
            }
        }

        loopButton.setOnClickListener {
            mediaPlayer.isLooping = !mediaPlayer.isLooping
            if (!mediaPlayer.isLooping) {
                // change button background to inactive
                loopButton.setBackgroundResource(R.drawable.loop_button_inactive)
            } else {
                loopButton.setBackgroundResource(R.drawable.loop_button_shadow)
            }
        }

        repeatButton.setOnClickListener {
            mediaPlayer.seekTo(0)
        }

        menuButton.setOnClickListener {
            showNavigationView()
        }

        nextButton.setOnClickListener {
            val isLooping = mediaPlayer.isLooping
            mediaPlayer.stop()
            mediaPlayer.release()
            index = (index + 1) % songs.size
            val songId = songs[index]
            val song = resources.getIdentifier(songId, "raw", packageName)
            setAssets()
            mediaPlayer = MediaPlayer.create(this, song)
            mediaPlayerControl()
            initSeekBar()
            mediaPlayer.isLooping = isLooping
            if (songPlaying) {
                mediaPlayer.start()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (songPlaying) {
                    mediaPlayer.start()
                }
            }
        })

        // on click listener to close the navigation view if user touch outside the navigation view
        background.setOnClickListener {
            if (menuOpen) {
                hideNavigationView()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun controlButtons() {
        playButton.setOnTouchListener { v, event ->
            // check if song is playing
            if (songPlaying) {
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        playButton.setBackgroundResource(R.drawable.pause_button_plain)
                    }
                }
            } else {
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        playButton.setBackgroundResource(R.drawable.play_button_plain)
                    }
                }
            }
            false
        }

        loopButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    loopButton.setBackgroundResource(R.drawable.loop_button_plain)
                }
            }
            false
        }

        repeatButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    repeatButton.setBackgroundResource(R.drawable.replay_button_plain)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    repeatButton.setBackgroundResource(R.drawable.replay_button_shadow)
                }
            }
            false
        }

        val scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        nextButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // make it look smaller
                    nextButton.startAnimation(scaleDown)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    nextButton.startAnimation(scaleUp)
                }
            }
            false
        }

        menuButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    menuButton.startAnimation(scaleDown)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    menuButton.startAnimation(scaleUp)
                }
            }
            false
        }
    }

    private fun initSeekBar() {
        if (mediaPlayer == null) {
            return
        }

        seekBar.max = mediaPlayer.duration
        seekBar.progress = 0

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }, 0)
    }


    private fun showNavigationView() {
        if (navigationView.visibility == View.GONE) {
            navigationView.visibility = View.VISIBLE
            navigationView.translationX = -navigationView.width.toFloat()
        }
        navigationView.animate()
            .translationX(0f)
            .setDuration(300)
            .withEndAction(null)
        menuOpen = true

        // disable the buttons
        playButton.isEnabled = false
        loopButton.isEnabled = false
        repeatButton.isEnabled = false
        nextButton.isEnabled = false
        seekBar.isEnabled = false
    }

    private fun hideNavigationView() {
        navigationView.animate()
            .translationX(-navigationView.width.toFloat())
            .setDuration(300)
            .withEndAction { navigationView.visibility = View.GONE }
        menuOpen = false

        // enable the buttons
        playButton.isEnabled = true
        loopButton.isEnabled = true
        repeatButton.isEnabled = true
        nextButton.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        if (songPlaying) {
            playButton.performClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (menuOpen) {
            hideNavigationView()
        }
    }

}