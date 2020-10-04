package com.georgcantor.freemovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.georgcantor.freemovies.R
import com.georgcantor.freemovies.ui.fragment.videos.VideosFragment
import com.georgcantor.freemovies.util.*
import com.georgcantor.freemovies.util.NetworkUtils.getNetworkLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var backButtonPressedTwice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_movies -> {
                    replaceFragment(VideosFragment.create(0))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_tv -> {
                    replaceFragment(VideosFragment.create(1))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_sport -> {
                    replaceFragment(VideosFragment.create(2))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        nav_view.setOnNavigationItemSelectedListener(itemSelectedListener)

        nav_view.selectedItemId = R.id.nav_movies

        getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            no_internet_warning.apply {
                when (isConnected) {
                    true -> if (isVisible) gone()
                    false -> if (isGone) visible()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size < 3) {
            when (nav_view.selectedItemId) {
                R.id.nav_sport -> nav_view.selectedItemId = R.id.nav_tv
                R.id.nav_tv -> nav_view.selectedItemId = R.id.nav_movies
                R.id.nav_movies -> {
                    if (backButtonPressedTwice) {
                        super.onBackPressed()
                    } else {
                        backButtonPressedTwice = true
                        shortToast(getString(R.string.press_back))
                        2000L.runDelayed { backButtonPressedTwice = false }
                    }
                }
            }
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}