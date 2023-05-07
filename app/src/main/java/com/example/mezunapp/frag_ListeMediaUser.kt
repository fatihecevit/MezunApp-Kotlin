package com.example.mezunapp

import AdapterEditMedia
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import item_Media

class frag_ListeMediaUser : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: AdapterEditMedia
    private lateinit var erecyclerView: RecyclerView
    private lateinit var addPicture: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_frag__liste_media_user, container, false)

        database = FirebaseDatabase.getInstance().getReference("media")
        erecyclerView = view.findViewById(R.id.erecyclerView)
        erecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AdapterEditMedia()
        erecyclerView.adapter = adapter
        addPicture = view.findViewById(R.id.addPicture)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mediaList = ArrayList<item_Media>()
                val userId = FirebaseAuth.getInstance().uid

                for (userSnapshot in snapshot.children) {
                    val uid = userSnapshot.child("uid").value.toString()
                    val uidmedia=userSnapshot.child("uidmedia").value.toString()
                    val mediaUrl = userSnapshot.child("mediaUrl").value.toString()
                    val adSoyad = userSnapshot.child("adSoyad").value.toString()

                    if (uid == userId) {
                        print("Mesaj: ")
                        val media = item_Media(uid, uidmedia, mediaUrl, adSoyad)
                        mediaList.add(media)
                    }
                }

                adapter.setMediaList(mediaList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Veri alınırken hata oluştuğunda yapılacak işlemler
                Toast.makeText(activity, "Veritabanından veriler okunamadı.", Toast.LENGTH_SHORT).show()
            }
        })
        addPicture.setOnClickListener {
            val intent = Intent(activity, AddMediaActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
