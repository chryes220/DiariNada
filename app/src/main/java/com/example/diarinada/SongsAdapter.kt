package com.example.diarinada

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongsAdapter(
    private val context: Context,
    private val songs: List<String>,
    private val songTitles: List<String>,
    private val coverImages: List<Drawable>,
    private val list_pencipta: List<String>,
    private val list_penata_musik: List<String>,
    private val list_penata_vokal: List<String>,
    private val list_penyanyi: List<String>) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val songTitle: TextView = itemView.findViewById(R.id.songNameTextView)
            val songImage: ImageView = itemView.findViewById(R.id.coverImageView)
            val pencipta: TextView = itemView.findViewById(R.id.pencipta)
            val penata_musik: TextView = itemView.findViewById(R.id.penata_musik)
            val penata_vokal: TextView = itemView.findViewById(R.id.penata_vokal)
            val penyanyi: TextView = itemView.findViewById(R.id.penyanyi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.song_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val songTitle = songTitles[position]
            val coverImage = coverImages[position]
            val pencipta = list_pencipta[position]
            val penata_musik = list_penata_musik[position]
            val penata_vokal = list_penata_vokal[position]
            val penyanyi = list_penyanyi[position]

            holder.songTitle.text = songTitle
            holder.songImage.setImageDrawable(coverImage)
            holder.pencipta.text = pencipta
            holder.penata_musik.text = penata_musik
            holder.penata_vokal.text = penata_vokal
            holder.penyanyi.text = penyanyi

            // Set the onClickListener for the item
            holder.itemView.setOnClickListener {
                // Create a new intent to start the SongActivity
                val intent = Intent(context, SongActivity::class.java)
                intent.putExtra("index", position)
                intent.putExtra("songs", songs.toTypedArray())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // Start the SongActivity
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return songs.size
        }
    }
