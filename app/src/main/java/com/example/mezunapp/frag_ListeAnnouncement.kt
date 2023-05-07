package com.example.mezunapp

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
import com.google.firebase.storage.FirebaseStorage
import item_Announcement
import java.text.SimpleDateFormat
import java.util.*

class frag_ListeAnnouncement : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: AdapterAnnouncement

    private lateinit var addAnnouncement: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_frag__liste_announcement, container, false)

        database = FirebaseDatabase.getInstance().getReference("announcement")
        addAnnouncement = view.findViewById(R.id.addAnnouncement)

        val arecyclerView = view.findViewById<RecyclerView>(R.id.arecyclerView)
        arecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AdapterAnnouncement(requireActivity())
        arecyclerView.adapter = adapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val announcementList = ArrayList<item_Announcement>()
                val userId = FirebaseAuth.getInstance().uid

                for (userSnapshot in snapshot.children) {

                    val uid = userSnapshot.child("uid").value.toString()
                    val uidmedia=userSnapshot.child("uidmedia").value.toString()
                    val imageurl = userSnapshot.child("imageurl").value.toString()
                    val baslik = userSnapshot.child("baslik").value.toString()
                    val icerik = userSnapshot.child("icerik").value.toString()
                    val sontarih=userSnapshot.child("sontarih").value.toString()
                    val adSoyad = userSnapshot.child("adSoyad").value.toString()

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(sontarih)
                    val currentDate = Date()

                    if (currentDate.before(date)) {
                        val announcement = item_Announcement(uid,uidmedia, imageurl, baslik,icerik,sontarih,adSoyad)
                        announcementList.add(announcement)
                    } else {
                        database.child(userSnapshot.key!!).removeValue()
                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageurl)
                        storageRef.delete().addOnSuccessListener {
                            // Silme başarılı
                        }
                    }
                }

                adapter.setAnnouncementList(announcementList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Veri alınırken hata oluştuğunda yapılacak işlemler
                Toast.makeText(activity, "Veritabanından veriler okunamadı.", Toast.LENGTH_SHORT).show()
            }

        })
        addAnnouncement.setOnClickListener {
            val intent = Intent(activity, AddAnnouncement::class.java)
            startActivity(intent)
        }
        return view
    }
}
