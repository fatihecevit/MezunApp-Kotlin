package com.example.mezunapp

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class AnaEkran : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var MediaMezun: frag_ListeMediaMezun
    private lateinit var VideoMezun: freg_ListeVideoMezun
    private lateinit var MediaUser: frag_ListeMediaUser
    private lateinit var Mezunlar: frag_ListeMezunlar
    private lateinit var Duyurular: frag_ListeAnnouncement
    private lateinit var Profile: frag_EditProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_ekran)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentContainer = findViewById(R.id.fragment_container)

        MediaMezun = frag_ListeMediaMezun()
       //VideoMezun=freg_ListeVideoMezun()
        MediaUser = frag_ListeMediaUser()
        Mezunlar = frag_ListeMezunlar()
        Duyurular = frag_ListeAnnouncement()
        Profile = frag_EditProfile()


        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Mezunlar).commit()

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_Mezunlar -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Mezunlar).commit()
                    true
                }
                R.id.menu_Duyurular -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Duyurular).commit()
                    true
                }
                R.id.menu_media -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MediaMezun).commit()
                    true
                }
                R.id.menu_resimlerim -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MediaUser).commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Profile).commit()
                    true
                }
                else -> false
            }
        }
    }
}

