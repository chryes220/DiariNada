package com.example.diarinada

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class SongListActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var menuButton: ImageButton
    private lateinit var navigationView: NavigationView
    private lateinit var navigationController: NavigationViewController
    private lateinit var background: LinearLayout

    private var menuOpen = false
    private val songs = listOf("dua_mata_saya", "kepala_pundak_lutut_kaki", "nama_nama_jari")
    private val song_title = listOf("Dua Mata Saya", "Kepala, Pundak, Lutut, Kaki", "Nama-Nama Jari")
    private val list_pencipta = listOf("Pak Kasur", "Ibu Soed", "Agus Patub BN")
    private val list_penata_musik = listOf("Jan Hien", "Jan Hien", "Jan Hien")
    private val list_penata_vokal = listOf("Chittra S.", "Chittra S.", "Chittra S.")
    private val list_penyanyi = listOf("Joannavita A. L.", "Joannavita A. L.", "Joannavita A. L.")
    private val coverImages = mutableListOf<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_list)

        menuButton = findViewById(R.id.menuButton)
        navigationView = findViewById(R.id.navigationView)
        navigationController = NavigationViewController(this)
        navigationView.setNavigationItemSelectedListener(navigationController)
        background = findViewById(R.id.bg)

        for (song in songs) {
            // get cover images from r.raw.cover-images.<song_title>
            // song cover is song + _cover
            val coverImage = resources.getIdentifier("${song}_cover", "drawable", packageName)
            ContextCompat.getDrawable(this, coverImage)!!.let { coverImages.add(it) }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = SongsAdapter(this, songs, song_title, coverImages, list_pencipta, list_penata_musik, list_penata_vokal, list_penyanyi)

        controlButtons()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun controlButtons() {
        val scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        menuButton.setOnClickListener {
            showNavigationView()
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

        background.setOnClickListener {
            if (menuOpen) {
                hideNavigationView()
            }
        }
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

        // disable clicking on recyclerview items
        recyclerView.isClickable = false
    }

    private fun hideNavigationView() {
        navigationView.animate()
            .translationX(-navigationView.width.toFloat())
            .setDuration(300)
            .withEndAction { navigationView.visibility = View.GONE }
        menuOpen = false

        // enable clicking on recyclerview items
        recyclerView.isClickable = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (menuOpen) {
            hideNavigationView()
        }
    }
}