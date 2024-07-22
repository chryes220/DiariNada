package com.example.diarinada

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView

// controller class for navigation bar
class NavigationViewController (private val context: Context) : NavigationView.OnNavigationItemSelectedListener{
    // function to set up navigation drawer

    // change font of navigation drawer items
    val menuTypeface = MenuTypeface(context)

    init {
        // set font for navigation drawer items
        menuTypeface.CustomTypefaceSpan("sans-serif", context.resources.getFont(R.font.diarinadafonts))
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
            R.id.nav_songs -> {
                val intent = Intent(context, SongListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
            R.id.nav_feedback -> {
                // open browser to feedback form
                val urlString = context.getString(R.string.feedback_form_link)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                context.startActivity(browserIntent)
            }
        }
        return true
    }

}