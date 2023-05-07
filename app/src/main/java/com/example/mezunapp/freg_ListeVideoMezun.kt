package com.example.mezunapp

import AdapterVideo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import item_Media

class freg_ListeVideoMezun : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var adapter: AdapterVideo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_freg__liste_video_mezun, container, false)
        database = FirebaseDatabase.getInstance().getReference("media")

        val vrecyclerView = view.findViewById<RecyclerView>(R.id.vrecyclerView)
        vrecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AdapterVideo()
        vrecyclerView.adapter = adapter
        val currentUser = FirebaseAuth.getInstance().currentUser

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mediaList = ArrayList<item_Media>()
                val userId = FirebaseAuth.getInstance().uid

                for (userSnapshot in snapshot.children) {
                    val uid = userSnapshot.child("uid").value.toString()
                    val uidmedia = userSnapshot.child("uidmedia").value.toString()
                    val mediaUrl = userSnapshot.child("mediaUrl").value.toString()
                    val adSoyad = userSnapshot.child("adSoyad").value.toString()


                    if (uid != userId) {
                        val media = item_Media(uid, uidmedia, mediaUrl, adSoyad)
                        mediaList.add(media)
                    }
                }

                adapter.setVideoList(mediaList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Veri alınırken hata oluştuğunda yapılacak işlemler
                Toast.makeText(activity, "Veritabanından veriler okunamadı.", Toast.LENGTH_SHORT).show()
            }

        })

        return view
    }
}
