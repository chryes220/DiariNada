package com.example.diarinada

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var playSongsButton: ImageButton
    private lateinit var songsButton: ImageButton
    private lateinit var exitButton: ImageButton
    private val songs = listOf("dua_mata_saya", "kepala_pundak_lutut_kaki", "nama_nama_jari")

    @SuppressLint("ClickableViewAccessibility")
    private fun controlButtons() {
        playSongsButton.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("songs", songs.toTypedArray())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            Log.d("MainActivity", "index: 0")
            Log.d("MainActivity", "songs: $songs")
            startActivity(intent)
        }

        playSongsButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    playSongsButton.setBackgroundResource(R.drawable.play_button_plain)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    playSongsButton.setBackgroundResource(R.drawable.play_button_shadow)
                }
            }
            false
        }

        songsButton.setOnClickListener {
            val intent = Intent(this, SongListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        songsButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    songsButton.setBackgroundResource(R.drawable.songs_button_plain)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    songsButton.setBackgroundResource(R.drawable.songs_button_shadow)
                }
            }
            false
        }

        exitButton.setOnClickListener {
            finish()
        }

        exitButton.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    exitButton.setBackgroundResource(R.drawable.exit_button_plain)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    exitButton.setBackgroundResource(R.drawable.exit_button_shadow)
                }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playSongsButton = findViewById(R.id.playButton)
        songsButton = findViewById(R.id.songsButton)
        exitButton = findViewById(R.id.exitButton)

        controlButtons()
    }

}